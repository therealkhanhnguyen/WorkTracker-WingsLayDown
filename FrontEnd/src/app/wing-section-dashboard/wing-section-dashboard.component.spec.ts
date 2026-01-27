import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WingSectionDashboardComponent } from './wing-section-dashboard.component';

describe('WingSectionDashboardComponent', () => {
  let component: WingSectionDashboardComponent;
  let fixture: ComponentFixture<WingSectionDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WingSectionDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WingSectionDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
