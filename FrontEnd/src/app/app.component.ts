import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { JobsApiService } from './core/api/jobs-api.service';
import { JsonPipe } from '@angular/common';
import { WingSectionDashboardComponent } from './wing-section-dashboard/wing-section-dashboard.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, JsonPipe,WingSectionDashboardComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  jobs: any[] = [];

  constructor(private jobsApi: JobsApiService){}

  ngOnInit(): void {
    this.jobsApi.list().subscribe({
      next: (data) => (this.jobs = data),
      error: (err) => console.error("API error", err) 
    });
  }
}
