package com.wgu.capstone.worktracker.controller;

import com.wgu.capstone.worktracker.dto.WorkLogResponse;
import com.wgu.capstone.worktracker.dto.WorkLogStartRequest;
import com.wgu.capstone.worktracker.entity.WorkLog;
import com.wgu.capstone.worktracker.service.WorkLogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/work-logs")
public class WorkLogController {
    private final WorkLogService workLogService;

    public WorkLogController(WorkLogService workLogService) {
        this.workLogService = workLogService;
    }

    @PostMapping("/start")
    public WorkLogResponse start(@Valid @RequestBody WorkLogStartRequest request) {
        WorkLog created = workLogService.startWorkLog(request.getJobId(),request.getUserId(), request.getNote());
        return toResponse(created);
    }

    @PostMapping("/{id}/stop")
    public WorkLogResponse stop(@PathVariable Long id) {
        WorkLog stopped = workLogService.stopWorkLog(id);
        return toResponse(stopped);
    }

    @GetMapping("/by-job/{jobId}")
    public List<WorkLogResponse> listByJob(@PathVariable Long jobId) {
        return workLogService.listByJob(jobId).stream()
                .map(this::toResponse)
                .toList();
    }

    private WorkLogResponse toResponse(WorkLog wl) {
        Duration d = wl.getDuration();
        Long second = ( d == null ) ? null : d.getSeconds();

        return new WorkLogResponse(
                wl.getId(),
                wl.getJob().getId(),
                wl.getUser().getId(),
                wl.getUser().getDisplayName(),
                wl.getStartTime(),
                wl.getEndTime(),
                second,
                wl.getNote()

        );
    }
}
