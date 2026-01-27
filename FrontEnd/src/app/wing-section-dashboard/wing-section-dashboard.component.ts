import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WingSectionsApiService, WingSectionResponse } from '../core/api/wing-sections-api.service';


type SectionColor = 'red' | 'yellow' | 'gray' | 'green';

@Component({
  selector: 'app-wing-section-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wing-section-dashboard.component.html',
  styleUrl: './wing-section-dashboard.component.css'
})
export class WingSectionDashboardComponent implements OnInit {
  sections: WingSectionResponse[] = [];

    // temp: all gray until we wire jobs → status
  sectionColor: Record<string, SectionColor> = {};

  selectedSectionId: string | null = null;

  constructor(private wingSectionsApi: WingSectionsApiService) {}

  ngOnInit(): void {
    this.wingSectionsApi.list().subscribe({
      next: (data) => {
        this.sections = data;
        // default color for each svg region
        for (const s of data) this.sectionColor[s.svgRegionId] = 'gray';
      },
      error: (err) => console.error('Wing sections API error', err)
    });
}

  onRegionClick(svgRegionId: string) {
    const section = this.sections.find(s => s.svgRegionId === svgRegionId);
    this.selectedSectionId = section?.id ?? null;
  }

    fillFor(svgRegionId: string): string {
    const c = this.sectionColor[svgRegionId] ?? 'gray';
    // you can tweak these later
    if (c === 'red') return '#ef4444';
    if (c === 'yellow') return '#eab308';
    if (c === 'green') return '#22c55e';
    return '#9ca3af'; // gray
  }
}
