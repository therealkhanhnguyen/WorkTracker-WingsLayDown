import { CommonModule } from '@angular/common';
import { Component, computed, effect, signal } from '@angular/core';
import { WingSection } from '../../core/models/wing-section.model';
import { WingSectionsApiService } from '../../core/api/wing-sections-api.service';
import { JobResponse, JobsApiService } from '../../core/api/jobs-api.service';



@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  wingSections = signal<WingSection[]>([]);
  selectedId = signal<string | null>(null);
  jobsLoading = signal(false);
  readonly Math = Math;
  
  // row(i: number): number {
  // return Math.floor(i / 4);
  // }

  // Jobs currently shown (for the selected wing section)
  selectedJobs = signal<JobResponse[]>([]);

    // helper for SVG lookup
  bySvgId = computed(() => {
    const map = new Map<string, WingSection>();
    for (const ws of this.wingSections()) map.set(ws.svgRegionId, ws);
    return map;
  });

    // ---- Status priority (higher = more urgent) ----
  // Adjust if you want different behavior.
  private readonly statusPriority: Record<string, number> = {
    REWORK_REQUESTED: 90,
    INSPECTION_IN_PROGRESS: 80,
    READY_FOR_INSPECTION: 70,
    IN_WORK: 60,
    CREATED: 50,
    READY_FOR_FINAL: 40,
    FINAL_APPROVED: 20,
    COMPLETED: 10,
  };

  // quick lookup: svgRegionId -> wingSection
//   bySvgId = computed(() => {
//     const map = new Map<string, WingSection>();
//     for (const ws of this.wingSections()) map.set(ws.svgRegionId, ws);
//     return map;

// });

    constructor(
      private wingSectionService: WingSectionsApiService,
      private jobsApi : JobsApiService
    ) {
    this.wingSectionService.getAll().subscribe({
      next: (data) => {
        this.wingSections.set(data);
        // auto-select first one
        if (data.length && !this.selectedId()) this.selectedId.set(data[0].id);
      },
      error: (err) => console.error('Failed to load wing sections', err),
    });

    // Whenever selectedId changes -> load jobs for that wing section
   effect(() => {
      const wsId = this.selectedId();
      if (!wsId) return;

      this.jobsLoading.set(true);
      this.jobsApi.list(undefined, wsId).subscribe({
        next: (jobs) => {
          this.selectedJobs.set(jobs);
          this.jobsLoading.set(false);
        },
        error: (err) => {
          console.error('Failed to load jobs', err);
          this.selectedJobs.set([]);
          this.jobsLoading.set(false);
        },
      });
    });
  }

    // used by template (Math is blocked in templates sometimes)
  row(i: number): number {
    return Math.floor(i / 4);
  }

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

  // ----- NEW: compute worst status for a wing section -----
  private worstStatusForWingSection(wingSectionId: string, jobs: JobResponse[]): string | null {
    const related = jobs.filter((j) => j.wingSectionId === wingSectionId);
    if (!related.length) return null;

    let worst = related[0].status;
    let worstScore = this.statusPriority[worst] ?? 0;

    for (const j of related) {
      const score = this.statusPriority[j.status] ?? 0;
      if (score > worstScore) {
        worst = j.status;
        worstScore = score;
      }
    }
    return worst;
  }

  // ----- NEW: map wingSectionId -> worstStatus (for all sections) -----
  // If you only load selected wing’s jobs, this will color only the selected wing.
  // Next step we'll load jobs for ALL wing sections at once (optional).
  wingStatusMap = computed(() => {
    const map = new Map<string, string | null>();

    const sections = this.wingSections();
    const jobs = this.selectedJobs(); // currently only selected wing’s jobs

    for (const ws of sections) {
      map.set(ws.id, this.worstStatusForWingSection(ws.id, jobs));
    }
    return map;
  });

  // ----- NEW: status -> CSS class -----
  statusClass(ws: WingSection): string {
    const status = this.wingStatusMap().get(ws.id);

    if (!status) return 'status-none';

    if (status === 'REWORK_REQUESTED') return 'status-red';
    if (status === 'READY_FOR_INSPECTION' || status === 'INSPECTION_IN_PROGRESS') return 'status-yellow';
    if (status === 'IN_WORK' || status === 'CREATED') return 'status-gray';
    if (status === 'COMPLETED' || status === 'FINAL_APPROVED') return 'status-green';

    return 'status-gray';
  }

}
