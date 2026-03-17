package com.wgu.capstone.worktracker.dto;

import com.wgu.capstone.worktracker.enumtype.JobStatus;
import jakarta.validation.constraints.NotNull;

public class JobStatusUpdateRequest {
    @NotNull
    private JobStatus status;

    public JobStatusUpdateRequest() {
    }

    public @NotNull JobStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull JobStatus status) {
        this.status = status;
    }
}
