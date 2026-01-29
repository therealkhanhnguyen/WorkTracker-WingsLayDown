import { CommonModule } from '@angular/common';
import { Component, computed, signal } from '@angular/core';
import { WingSection } from '../../core/models/wing-section.model';
import { WingSectionsApiService } from '../../core/api/wing-sections-api.service';



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
  readonly Math = Math;
row(i: number): number {
  return Math.floor(i / 4);
}


  // quick lookup: svgRegionId -> wingSection
  bySvgId = computed(() => {
    const map = new Map<string, WingSection>();
    for (const ws of this.wingSections()) map.set(ws.svgRegionId, ws);
    return map;

});

    constructor(private wingSectionService: WingSectionsApiService) {
    this.wingSectionService.getAll().subscribe({
      next: (data) => {
        this.wingSections.set(data);
        // auto-select first one
        if (data.length && !this.selectedId()) this.selectedId.set(data[0].id);
      },
      error: (err) => console.error('Failed to load wing sections', err)
    });
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

}
