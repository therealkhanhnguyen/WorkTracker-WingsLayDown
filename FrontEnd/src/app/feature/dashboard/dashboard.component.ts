import { CommonModule } from '@angular/common';
import { Component, computed, signal } from '@angular/core';

import { WingSection } from '../../core/models/wing-section.model';
import { WingSectionsApiService } from '../../core/api/wing-sections-api.service';

import { JobsApiService, JobResponse } from '../../core/api/jobs-api.service';

type StatusBucket = 'empty' | 'created' | 'in_work' | 'inspection' | 'completed';



@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
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

    // 2) load ALL jobs once (best for coloring all rectangles)
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

    // Completed bucket (FINAL_APPROVED treated like completed per request)
    if (s === 'COMPLETED' || s === 'FINAL_APPROVED') return 'completed';

    // Inspection bucket
    if (
      s === 'READY_FOR_INSPECTION' ||
      s === 'INSPECTION_IN_PROGRESS' ||
      s === 'READY_FOR_FINAL'
    ) {
      return 'inspection';
    }

    // In-work bucket
    if (s === 'IN_WORK' || s === 'REWORK_REQUESTED') return 'in_work';

    // Created bucket
    if (s === 'CREATED') return 'created';

    // Anything unknown -> treat as created (safer than empty)
    return 'created';
  }

  private priority(b: StatusBucket): number {
    // "most urgent wins"
    // inspection > in_work > created > completed > empty
    switch (b) {
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

private refreshJobs() {
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


}
