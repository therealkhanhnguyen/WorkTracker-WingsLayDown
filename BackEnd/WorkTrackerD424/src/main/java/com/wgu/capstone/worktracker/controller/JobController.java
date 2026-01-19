package com.wgu.capstone.worktracker.controller;

import com.wgu.capstone.worktracker.dto.JobCreateRequest;
import com.wgu.capstone.worktracker.dto.JobRespone;
import com.wgu.capstone.worktracker.dto.JobStatusUpdateRequest;
import com.wgu.capstone.worktracker.entity.Job;
import com.wgu.capstone.worktracker.enumtype.JobStatus;
import com.wgu.capstone.worktracker.mapper.JobMapper;
import com.wgu.capstone.worktracker.service.JobService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    //CREATE
    @PostMapping
    public JobRespone create(@Valid @RequestBody JobCreateRequest request){
        Job job = jobService.createJob(
                request.getWingSectionId(),
                request.getAssignedEmployeeId(),
                request.getTitle(),
                request.getDescription()
                );
        return JobMapper.toJobRespone(job);
    }

    //Read by id
    @GetMapping("/{id}")
    public JobRespone getById(@PathVariable Long id){
        return JobMapper.toJobRespone(jobService.getJob(id));
    }

    //List all
    @GetMapping
    public List<JobRespone> list(
           @RequestParam(required = false) JobStatus status,
            @RequestParam(required = false) String wingSectionId
    ) {
        List<Job> jobs;
        if (status != null) {
            jobs = jobService.listJobByStatus(status);
        } else if (wingSectionId != null && !wingSectionId.isBlank()) {
            jobs = jobService.listJobsByWingSection(wingSectionId);
        } else {
            jobs = jobService.listJob();
        }
        return jobs.stream().map(JobMapper::toJobRespone).toList();
    }

    //Update status
    @PatchMapping("/{id}/status")
    public JobRespone updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody JobStatusUpdateRequest request){
        Job updated = jobService.updateJob(id, request.getStatus());
        return JobMapper.toJobRespone(updated);
    }
}
