import { CommonModule } from '@angular/common';
import { Component, computed, signal } from '@angular/core';

import { WingSection } from '../../core/models/wing-section.model';
import { WingSectionsApiService } from '../../core/api/wing-sections-api.service';

import { JobsApiService, JobResponse } from '../../core/api/jobs-api.service';
import { FormsModule } from '@angular/forms';

type StatusBucket = 'empty' | 'created' | 'in_work' | 'inspection' | 'ready_for_final' | 'completed';



@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {


  // ---------- state ----------
  wingSections = signal<WingSection[]>([]);
  selectedId = signal<string | null>(null);

  jobsLoading = signal<boolean>(false);
  allJobs = signal<JobResponse[]>([]);

  selectedJobId = signal<number | null>(null);
  actionLoading = signal<boolean>(false);
  actionError = signal<string | null>(null);
  // ---------- update ----------
  
  creating = signal(false);
  editingJobId = signal<number | null>(null);

  formWingSectionId = signal<string>('');
  formEmployeeId = signal<number>(1);
  formTitle = signal<string>('');
  formDescription = signal<string>('');

  // search bar

  searchWingSectionId = '';
  searchStatus = '';



  // ---------- lookups ----------
  // svgRegionId -> WingSection
  bySvgId = computed(() => {
    const map = new Map<string, WingSection>();
    for (const ws of this.wingSections()) map.set(ws.svgRegionId, ws);
    return map;
  });

  // wingSectionId -> Job[]
  jobsByWingSectionId = computed(() => {
    const map = new Map<string, JobResponse[]>();
    for (const job of this.allJobs()) {
      const key = job.wingSectionId;
      if (!key) continue;

      if (!map.has(key)) map.set(key, []);
      map.get(key)!.push(job);
    }
    return map;
  });

  // jobs for currently selected wing section
  selectedJobs = computed(() => {
    const id = this.selectedId();
    if (!id) return [];
    return this.jobsByWingSectionId().get(id) ?? [];
  });

  constructor(
    private wingSectionService: WingSectionsApiService,
    private jobsApi: JobsApiService
  ) {
    // 1) load wing sections
    this.wingSectionService.getAll().subscribe({
      next: (data: WingSection[]) => {
        this.wingSections.set(data);
        if (data.length && !this.selectedId()) {
          this.selectedId.set(data[0].id);
        }
      },
      error: (err) => console.error('Failed to load wing sections', err),
    });

    // 2) load ALL jobs once ( coloring all rectangles)
    this.jobsLoading.set(true);
    this.jobsApi.list().subscribe({
      next: (jobs: JobResponse[]) => {
        this.allJobs.set(jobs);
        this.jobsLoading.set(false);
      },
      error: (err) => {
        console.error('Failed to load jobs', err);
        this.allJobs.set([]);
        this.jobsLoading.set(false);
      },
    });
  }

  // ---------- SVG helpers ----------
  row(i: number): number {
    return Math.floor(i / 4);
  }

  // ---------- selection handlers ----------
  selectById(id: string) {
    this.selectedId.set(id);
  }

  selectBySvg(svgRegionId: string) {
    const ws = this.bySvgId().get(svgRegionId);
    if (ws) this.selectedId.set(ws.id);
  }

  isSelected(ws: WingSection): boolean {
    return this.selectedId() === ws.id;
  }

  // ---------- status bucketing ----------
private bucket(statusRaw: string | null | undefined): StatusBucket {
  const s = (statusRaw ?? '').toUpperCase();

  if (s === 'COMPLETED' || s === 'FINAL_APPROVED') return 'completed';
  if (s === 'READY_FOR_FINAL') return 'ready_for_final';
  if (s === 'READY_FOR_INSPECTION') return 'inspection';
  if (s === 'IN_WORK') return 'in_work';
  if (s === 'CREATED') return 'created';

  return 'created';
}




private priority(b: StatusBucket): number {
  switch (b) {
    case 'ready_for_final':
      return 5;
    case 'inspection':
      return 4;
    case 'in_work':
      return 3;
    case 'created':
      return 2;
    case 'completed':
      return 1;
    default:
      return 0;
  }
}


  private rollupBucket(jobs: JobResponse[]): StatusBucket {
    if (!jobs || jobs.length === 0) return 'empty';

    let best: StatusBucket = 'empty';
    let bestP = 0;

    for (const j of jobs) {
      const b = this.bucket(j.status);
      const p = this.priority(b);
      if (p > bestP) {
        bestP = p;
        best = b;
      }
    }
    return best;
  }

  // Used by template for rectangle CSS classes
  statusClassFor(wsId: string): string {
    const jobs = this.jobsByWingSectionId().get(wsId) ?? [];
    const bucket = this.rollupBucket(jobs);
    return `status-${bucket}`; // status-created, status-in_work, status-inspection, status-completed, status-empty
  }

  selectJob(id: number) {
  this.selectedJobId.set(id);
  this.actionError.set(null);
}

isJobSelected(job: JobResponse): boolean {
  return this.selectedJobId() === job.id;
}

 refreshJobs() {
  this.jobsLoading.set(true);
  this.jobsApi.list().subscribe({
    next: (jobs) => {
      this.allJobs.set(jobs);
      this.jobsLoading.set(false);
    },
    error: (err) => {
      console.error('Failed to reload jobs', err);
      this.jobsLoading.set(false);
    },
  });
}

updateJobStatus(jobId: number, status: string) {
  this.actionLoading.set(true);
  this.actionError.set(null);

  this.jobsApi.updateStatus(jobId, status).subscribe({
    next: () => {
      this.actionLoading.set(false);
      this.refreshJobs();
    },
    error: (err) => {
      console.error('Status update failed', err);
      this.actionLoading.set(false);
      // show backend message if it exists
      const msg =
        err?.error?.message ||
        err?.error?.error ||
        'Status update failed (check console/server logs).';
      this.actionError.set(msg);
    },
  });
}

requestInspection(jobId: number) {
  this.actionLoading.set(true);
  this.actionError.set(null);

  this.jobsApi.requestInspection(jobId).subscribe({
    next: () => {
      this.actionLoading.set(false);
      this.refreshJobs();
    },
    error: (err) => {
      console.error('Request inspection failed', err);
      this.actionLoading.set(false);
      const msg = err?.error?.message || 'Request inspection failed.';
      this.actionError.set(msg);
    },
  });
}

requestFinal(jobId: number) {
  this.actionLoading.set(true);
  this.actionError.set(null);

  this.jobsApi.requestFinal(jobId).subscribe({
    next: () => {
      this.actionLoading.set(false);
      this.refreshJobs();
    },
    error: (err) => {
      console.error('Request final failed', err);
      this.actionLoading.set(false);
      const msg = err?.error?.message || 'Request final failed.';
      this.actionError.set(msg);
    },
  });
}

// ---- simplified work flow ----
// Flow: CREATED -> IN_WORK -> READY_FOR_INSPECTION -> READY_FOR_FINAL -> FINAL_APPROVED

canStartWork(job: JobResponse): boolean {
  return (job.status ?? '').toUpperCase() === 'CREATED';
}

canRequestInspection(job: JobResponse): boolean {
  return (job.status ?? '').toUpperCase() === 'IN_WORK';
}

canRequestFinal(job: JobResponse): boolean {
  return (job.status ?? '').toUpperCase() === 'READY_FOR_INSPECTION';
}

canApproveFinal(job: JobResponse): boolean {
  return (job.status ?? '').toUpperCase() === 'READY_FOR_FINAL';
}


// canComplete(_job: JobResponse): boolean {
//   return false; // optional: keep "Complete" disabled for now
// }

nextStepLabel(job: JobResponse): string {
  const s = (job.status ?? '').toUpperCase();

  switch (s) {
    case 'CREATED':
      return 'Next: Start Work';
    case 'IN_WORK':
      return 'Next: Request Inspection';
    case 'READY_FOR_INSPECTION':
      return 'Next: Request Final';
    case 'READY_FOR_FINAL':
      return 'Next: Approve Final';
    case 'FINAL_APPROVED':
      return '✅ Done';
    default:
      return '';
  }
}

openCreate() {
  const wsId = this.selectedId();
  if (!wsId) return;

  this.creating.set(true);
  this.editingJobId.set(null);
  this.actionError.set(null);

  this.formWingSectionId.set(wsId);
  this.formEmployeeId.set(3); // employeeid 3
  this.formTitle.set(`Job - ${wsId}`);
  this.formDescription.set('Work package description...');
}

cancelForm() {
  this.creating.set(false);
  this.editingJobId.set(null);
}

submitCreate() {
  this.actionLoading.set(true);
  this.actionError.set(null);

  this.jobsApi.create({
    wingSectionId: this.formWingSectionId(),
    assignedEmployeeId: this.formEmployeeId(),
    title: this.formTitle(),
    description: this.formDescription(),
  }).subscribe({
    next: () => {
      this.actionLoading.set(false);
      this.creating.set(false);
      this.refreshJobs();
    },
    error: (err) => {
      this.actionLoading.set(false);
      this.actionError.set(err?.error?.message || 'Create failed.');
    },
  });
}

startEdit(job: JobResponse) {
  this.creating.set(false);
  this.editingJobId.set(job.id);
  this.actionError.set(null);

  this.formWingSectionId.set(job.wingSectionId);
  this.formEmployeeId.set(job.assignedEmployeeId ?? 3);
  this.formTitle.set(job.title);
  this.formDescription.set(job.description);
}

submitEdit(jobId: number) {
  this.actionLoading.set(true);
  this.actionError.set(null);

  this.jobsApi.updateDetails(jobId, {
    assignedEmployeeId: this.formEmployeeId(),
    title: this.formTitle(),
    description: this.formDescription(),
  }).subscribe({
    next: () => {
      this.actionLoading.set(false);
      this.editingJobId.set(null);
      this.refreshJobs();
    },
    error: (err) => {
      this.actionLoading.set(false);
      this.actionError.set(err?.error?.message || 'Update failed.');
    },
  });
}

deleteJob(jobId: number) {
  const ok = confirm(`Delete job #${jobId}?`);
  if (!ok) return;

  this.actionLoading.set(true);
  this.actionError.set(null);

  this.jobsApi.delete(jobId).subscribe({
    next: () => {
      this.actionLoading.set(false);
      if (this.selectedJobId() === jobId) this.selectedJobId.set(null);
      this.refreshJobs();
    },
    error: (err) => {
      this.actionLoading.set(false);
      this.actionError.set(err?.error?.message || 'Delete failed.');
    },
  });
}

// search bar method

searchJobs() {
  this.jobsLoading.set(true);

  this.jobsApi
    .list(
      this.searchStatus || undefined,
      this.searchWingSectionId || undefined
    )
    .subscribe({
      next: (jobs) => {
        this.allJobs.set(jobs);
        this.jobsLoading.set(false);
      },
      error: (err) => {
        console.error('Search failed', err);
        this.jobsLoading.set(false);
      },
    });
}

clearSearch() {
  this.searchWingSectionId = '';
  this.searchStatus = '';
  this.refreshJobs();
}

// report feature

downloadReport() {
  this.jobsApi.downloadJobCycleTimeReport().subscribe({
    next: (blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'job-cycle-time-report.csv';
      a.click();
      window.URL.revokeObjectURL(url);
    },
    error: (err) => console.error('Report download failed', err)
  });
}


}
