package com.wgu.capstone.worktracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class JobCreateRequest {
    @NotBlank
    @Size(max = 64)
    private String wingSectionId;

    @NotNull
    private Long assignedEmployeeId;

    @NotBlank
    @Size(max = 120)
    private String title;

    @NotBlank
    @Size(max = 100)
    private String description;

    public JobCreateRequest() {
    }

    public @NotNull Long getAssignedEmployeeId() {
        return assignedEmployeeId;
    }

    public void setAssignedEmployeeId(@NotNull Long assignedEmployeeId) {
        this.assignedEmployeeId = assignedEmployeeId;
    }

    public @NotBlank @Size(max = 100) String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank @Size(max = 100) String description) {
        this.description = description;
    }

    public @NotBlank @Size(max = 120) String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank @Size(max = 120) String title) {
        this.title = title;
    }

    public @NotBlank @Size(max = 64) String getWingSectionId() {
        return wingSectionId;
    }

    public void setWingSectionId(@NotBlank @Size(max = 64) String wingSectionId) {
        this.wingSectionId = wingSectionId;
    }
}
