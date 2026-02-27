package com.wgu.capstone.worktracker.dto;

import com.wgu.capstone.worktracker.enumtype.InspectionResult;
import jakarta.validation.constraints.NotNull;

public class InspectionCreateRequest {
    @NotNull
    private Long inspectorUserId;
    @NotNull
    private InspectionResult result;

    private String notes;

    public InspectionCreateRequest() {
    }

    public @NotNull Long getInspectorUserId() {
        return inspectorUserId;
    }

    public String getNotes() {
        return notes;
    }

    public @NotNull InspectionResult getResult() {
        return result;
    }
}
