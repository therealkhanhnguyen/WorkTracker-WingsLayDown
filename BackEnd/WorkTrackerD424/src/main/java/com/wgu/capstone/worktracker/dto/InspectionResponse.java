package com.wgu.capstone.worktracker.dto;

import com.wgu.capstone.worktracker.enumtype.InspectionResult;
import com.wgu.capstone.worktracker.enumtype.InspectionType;

import java.time.Instant;

public class InspectionResponse {
    private Long id;
    private Long jobId;
    private Long inspectorUserId;
    private String inspectorName;
    private InspectionType type;
    private InspectionResult result;
    private String notes;
    private Instant createdAt;

    public InspectionResponse() {
    }

    public InspectionResponse(Long id, Long jobId, Long inspectorUserId, String inspectorName, InspectionType type, String notes, InspectionResult result, Instant createdAt) {
        this.id = id;
        this.jobId = jobId;
        this.inspectorUserId = inspectorUserId;
        this.inspectorName = inspectorName;
        this.type = type;
        this.notes = notes;
        this.result = result;
        this.createdAt = createdAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public Long getInspectorUserId() {
        return inspectorUserId;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getNotes() {
        return notes;
    }

    public InspectionResult getResult() {
        return result;
    }

    public InspectionType getType() {
        return type;
    }
}
