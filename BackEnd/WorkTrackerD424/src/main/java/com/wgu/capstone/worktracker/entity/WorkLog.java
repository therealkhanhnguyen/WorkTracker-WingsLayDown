package com.wgu.capstone.worktracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "work_logs", indexes = {
        @Index(name = "idx_work_logs_job_id", columnList = "job_id"),
        @Index(name = "idx_work_logs_user_id", columnList = "user_id"),
        @Index(name = "idx_work_logs_start_time", columnList = "start_time")
})
public class WorkLog extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Many logs per job
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    //Many logs per users
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;


    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "note", length = 500)
    private String note;

    public WorkLog() {
    }

    public WorkLog(Job job, User user, Instant startTime, String note) {
        this.job = job;
        this.user = user;
        this.startTime = startTime;
        this.note = note;
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

    public @NotNull Job getJob() {
        return job;
    }

    public void setJob(@NotNull Job job) {
        this.job = job;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public @NotNull Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(@NotNull Instant startTime) {
        this.startTime = startTime;
    }

    public @NotNull User getUser() {
        return user;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
    }

    /**
     * Computed duration. Returns null if endTime is not set yet.
     */
    @Transient
    public Duration getDuration() {
        if (startTime == null || endTime == null) return null;
        return Duration.between(startTime, endTime);
    }
}
