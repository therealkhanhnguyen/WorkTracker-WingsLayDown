package com.wgu.capstone.worktracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WorkLogStartRequest {
    @NotNull
    private Long jobId;

    @NotNull
    private long userId;

   @Size(max=500)
    private String note;

    public WorkLogStartRequest() {
    }

    public @NotNull Long getJobId() {
        return jobId;
    }

    public void setJobId(@NotNull Long jobId) {
        this.jobId = jobId;
    }

    public @Size(max = 500) String getNote() {
        return note;
    }

    public void setNote(@Size(max = 500) String note) {
        this.note = note;
    }

    @NotNull
    public long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull long userId) {
        this.userId = userId;
    }
}
