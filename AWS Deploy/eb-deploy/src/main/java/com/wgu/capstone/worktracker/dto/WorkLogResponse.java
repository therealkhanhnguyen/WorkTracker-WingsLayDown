package com.wgu.capstone.worktracker.dto;

import java.time.Instant;

public class WorkLogResponse {

    private Long id;
    private Long jobId;
    private Long userId;
    private String userDisplayName;
    private Instant startTime;
    private Instant endTime;
    private Long durationSeconds;
    private String notes;

    public WorkLogResponse(
            Long id,
            Long jobId,
            Long userId,
            String userDisplayName,
            Instant startTime,
            Instant endTime,
            Long durationSeconds,
            String notes
    ) {
        this.id = id;
        this.jobId = jobId;
        this.userId = userId;
        this.userDisplayName = userDisplayName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationSeconds = durationSeconds;
        this.notes = notes;
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
