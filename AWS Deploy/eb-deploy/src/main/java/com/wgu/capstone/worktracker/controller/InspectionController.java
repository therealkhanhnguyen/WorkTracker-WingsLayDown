package com.wgu.capstone.worktracker.controller;

import com.wgu.capstone.worktracker.dto.InspectionCreateRequest;
import com.wgu.capstone.worktracker.dto.InspectionResponse;
import com.wgu.capstone.worktracker.entity.Inspection;
import com.wgu.capstone.worktracker.service.InspectionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs/{jobId}/inspections")
public class InspectionController {
    private final InspectionService inspectionService;

    public InspectionController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }


    @PostMapping
    public InspectionResponse create(@PathVariable Long jobId, @Valid @RequestBody InspectionCreateRequest request){
        Inspection saved = inspectionService.createInspection(
                jobId,
                request.getInspectorUserId(),
                request.getResult(),
                request.getNotes()
                );
        return toResponse(saved);
    }

    @GetMapping
    public List<InspectionResponse> list(@PathVariable Long jobId){
        return inspectionService.listByJob(jobId).stream()
                .map(this::toResponse)
                .toList();
    }
    private InspectionResponse toResponse(Inspection i){
        return new InspectionResponse(
                i.getId(),
                i.getJob().getId(),
                i.getInspectedBy().getId(),
                i.getInspectedBy().getDisplayName(),
                i.getType(),
                i.getNotes(),
                i.getResult(),
                i.getCreatedAt()
        );
    }
}
