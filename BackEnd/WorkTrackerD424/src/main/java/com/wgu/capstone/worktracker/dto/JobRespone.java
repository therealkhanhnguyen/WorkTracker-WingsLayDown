package com.wgu.capstone.worktracker.dto;

import com.wgu.capstone.worktracker.enumtype.JobStatus;

import java.time.Instant;

public class JobRespone {
    private Long id;
    private String wingSectionId;
    private String wingSectionName;

    private Long assignedEmployeeId;
    private String assignedEmployeeName;

    private String title;
    private String description;

    private JobStatus status;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant completedAt;

    public JobRespone() {
    }

    public JobRespone(Long id,
                      String wingSectionId,
                      String wingSectionName,
                      Long assignedEmployeeId,
                      String assignedEmployeeName,
                      String title,
                      String description,
                      JobStatus status,
                      Instant createdAt,
                      Instant updatedAt,
                      Instant completedAt) {
        this.id = id;
        this.wingSectionId = wingSectionId;
        this.wingSectionName = wingSectionName;
        this.assignedEmployeeId = assignedEmployeeId;
        this.assignedEmployeeName = assignedEmployeeName;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
    }

    public Long getAssignedEmployeeId() {
        return assignedEmployeeId;
    }

    public String getAssignedEmployeeName() {
        return assignedEmployeeName;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getWingSectionId() {
        return wingSectionId;
    }

    public String getWingSectionName() {
        return wingSectionName;
    }
}
