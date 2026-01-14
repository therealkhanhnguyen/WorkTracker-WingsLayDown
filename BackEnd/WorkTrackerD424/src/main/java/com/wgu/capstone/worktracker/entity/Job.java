package com.wgu.capstone.worktracker.entity;

import com.wgu.capstone.worktracker.enumtype.JobStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "jobs",
        //set up for search
        indexes = {
                @Index(name = "idx_jobs_status", columnList = "status"),
                @Index(name = "idx_jobs_completed_at", columnList = "completed_at")
        }
)
public class Job extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //each job belong to one section
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "wing_section_id",nullable = false)
    private WingSection wingSection;

    //one employee per job
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_empoylee_id", nullable = false)
    private User assignedEmployee;

    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private JobStatus status;

    @Column(name = "completed_at")
    private Instant completedAt;

    protected Job(){}

    public Job(WingSection wingSection, User assignedEmployee, String title, String description) {
        this.wingSection = wingSection;
        this.assignedEmployee = assignedEmployee;
        this.title = title;
        this.description = description;
        this.status = JobStatus.CREATED;
        this.completedAt = null;
    }

    public User getAssignedEmployee() {
        return assignedEmployee;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public JobStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public WingSection getWingSection() {
        return wingSection;
    }

    public void setAssignedEmployee(User assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWingSection(WingSection wingSection) {
        this.wingSection = wingSection;
    }
}
