package com.wgu.capstone.worktracker.service;

import com.wgu.capstone.worktracker.entity.Job;
import com.wgu.capstone.worktracker.entity.User;
import com.wgu.capstone.worktracker.entity.WorkLog;
import com.wgu.capstone.worktracker.exception.BadRequestException;
import com.wgu.capstone.worktracker.exception.NotFoundException;
import com.wgu.capstone.worktracker.repository.JobRepository;
import com.wgu.capstone.worktracker.repository.UserRepository;
import com.wgu.capstone.worktracker.repository.WorkLogRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class WorkLogService {
    private final WorkLogRepository workLogRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public WorkLogService(JobRepository jobRepository, WorkLogRepository workLogRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.workLogRepository = workLogRepository;
        this.userRepository = userRepository;
    }

    public WorkLog startWorkLog(Long jobId, Long userId, String note){
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found:" + jobId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found:" + userId));

        // don't start new job if there's already an open log for this user and job
        List<WorkLog> existing = workLogRepository.findByJob_IdOrderByStartTimeDesc(jobId);
        for (WorkLog wl : existing) {
            if (wl.getUser().getId().equals(userId) && wl.getEndTime() == null) {
                throw new BadRequestException("This user already has an active work log for this job ");
            }
        }

        WorkLog workLog = new WorkLog();
        workLog.setJob(job);
        workLog.setUser(user);
        workLog.setStartTime(Instant.now());
        workLog.setEndTime(null);
        workLog.setNote(note);

        return workLogRepository.save(workLog);
    }

    public WorkLog stopWorkLog(Long workLogId){
        WorkLog workLog = workLogRepository.findById(workLogId)
                .orElseThrow(() -> new NotFoundException("WorkLog not found:" + workLogId));
        if (workLog.getEndTime() != null) {
            throw new BadRequestException("WorkLog is already stopped.");
        }
        workLog.setEndTime(Instant.now());
        //end must be after start
        if (workLog.getStartTime() != null && workLog.getEndTime().isBefore(workLog.getStartTime())) {
            throw new BadRequestException("End time cannot be before start time.");
        }

        return workLogRepository.save(workLog);
    }
}
