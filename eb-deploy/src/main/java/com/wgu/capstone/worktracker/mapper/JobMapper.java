package com.wgu.capstone.worktracker.mapper;

import com.wgu.capstone.worktracker.dto.JobRespone;
import com.wgu.capstone.worktracker.entity.Job;

public final class JobMapper {
    public JobMapper() {
    }

    public static JobRespone toJobRespone(Job job) {
        return new JobRespone(
                job.getId(),
                job.getWingSection().getId(),
                job.getWingSection().getName(),
                job.getAssignedEmployee().getId(),
                job.getAssignedEmployee().getDisplayName(),
                job.getTitle(),
                job.getDescription(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getUpdatedAt(),
                job.getCompletedAt()
        );
    }
}
