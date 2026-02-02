package com.wgu.capstone.worktracker.service;

import com.wgu.capstone.worktracker.entity.Job;
import com.wgu.capstone.worktracker.entity.User;
import com.wgu.capstone.worktracker.entity.WingSection;
import com.wgu.capstone.worktracker.enumtype.JobStatus;
import com.wgu.capstone.worktracker.enumtype.Role;
import com.wgu.capstone.worktracker.exception.BadRequestException;
import com.wgu.capstone.worktracker.exception.NotFoundException;
import com.wgu.capstone.worktracker.repository.JobRepository;
import com.wgu.capstone.worktracker.repository.UserRepository;
import com.wgu.capstone.worktracker.repository.WingSectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;

@Service
@Transactional
public class JobService {
    private final JobRepository jobRepository;
    private final WingSectionRepository wingSectionRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, WingSectionRepository wingSectionRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.wingSectionRepository = wingSectionRepository;
        this.userRepository = userRepository;
    }

//    public Job createJob(String wingSectionId,Long employeeId,String title, String description) {
//
//        WingSection section = wingSectionRepository.findById(wingSectionId)
//                .orElseThrow( ()-> new NotFoundException("WingSection Not Found"+ wingSectionId));
//
//        User employee = userRepository.findById(employeeId)
//                .orElseThrow( ()-> new NotFoundException("User Not Found"+ employeeId));
//
//        if (employee.getRole() != Role.EMPLOYEE){
//            throw new BadRequestException("Assigned user must have role EMPLOYEE");
//        }
//
//        //only one active job per wing section
//        boolean existActive = jobRepository.existsByWingSection_IdAndStatusNot(wingSectionId, JobStatus.COMPLETED);
//
//        if (existActive){
//            throw new BadRequestException("An active job already exists for wing section " + wingSectionId);
//        }
//
//        Job job = new Job(section, employee, title, description);
//
//        return jobRepository.save(job);
//    }
//simplified work flow logic
    public Job createJob(String wingSectionId, Long employeeId, String title, String description) {

        WingSection section = wingSectionRepository.findById(wingSectionId)
                .orElseThrow(() -> new NotFoundException("WingSection Not Found " + wingSectionId));

        User employee = null;
        if (employeeId != null) {
            employee = userRepository.findById(employeeId)
                    .orElseThrow(() -> new NotFoundException("User Not Found " + employeeId));

            if (employee.getRole() != Role.EMPLOYEE) {
                throw new BadRequestException("Assigned user must have role EMPLOYEE");
            }
        }

        // only one active job per wing section
        boolean existActive = jobRepository.existsByWingSection_IdAndStatusNot(wingSectionId, JobStatus.COMPLETED);
        if (existActive) {
            throw new BadRequestException("An active job already exists for wing section " + wingSectionId);
        }

        Job job = new Job(section, employee, title, description);
        return jobRepository.save(job);
    }


    public Job getJob(Long jobId){
        return jobRepository.findById(jobId)
                .orElseThrow( ()-> new NotFoundException("Job Not Found"+ jobId));
    }

    public List<Job> listJob(){
        return jobRepository.findAll();
    }

    public List<Job> listJobByStatus(JobStatus status){
        return jobRepository.findByStatus(status);
    }

    public List<Job> listJobsByWingSection(String wingSectionId){
        //validate if wingSection exist
        if (!wingSectionRepository.existsById(wingSectionId)){
            throw new NotFoundException("WingSection Not Found"+ wingSectionId);
        }

        return jobRepository.findByWingSection_Id(wingSectionId);
    }

    public Job updateJob(Long jobId, JobStatus newStatus){
        Job job = getJob(jobId);
        validateTransition(job.getStatus(), newStatus);

        job.setStatus(newStatus);

        //set timestamp if complete
        if(newStatus == JobStatus.COMPLETED){
            job.setCompletedAt(Instant.now());
        }

        return jobRepository.save(job);
    }

//    private void validateTransition(JobStatus current, JobStatus next){
//        if (current == next){
//            return;
//        }
//
//        switch (current){
//            case CREATED -> requiredNext(next, JobStatus.IN_WORK);
//            case IN_WORK -> requiredNext(next, JobStatus.READY_FOR_INSPECTION);
//            case READY_FOR_INSPECTION -> requiredNext(next, JobStatus.INSPECTION_IN_PROGRESS);
//            case INSPECTION_IN_PROGRESS -> requiredNext(next, JobStatus.REWORK_REQUESTED,JobStatus.FINAL_APPROVED );
//            case REWORK_REQUESTED -> requiredNext(next, JobStatus.IN_WORK); //employee rework
//            case FINAL_APPROVED -> requiredNext(next, JobStatus.COMPLETED);
//            case COMPLETED -> throw new BadRequestException("Job Status is already completed, cannot change status");
//
//            default -> throw new BadRequestException("unsupported job status transition");
//
//        }
//    }
// simplified workflow
    private void validateTransition(JobStatus current, JobStatus next) {
        if (current == next) return;

        switch (current) {
            case CREATED -> requiredNext(next, JobStatus.IN_WORK);

            case IN_WORK -> requiredNext(next, JobStatus.READY_FOR_INSPECTION);

            case READY_FOR_INSPECTION -> requiredNext(next, JobStatus.READY_FOR_FINAL);

            case READY_FOR_FINAL -> requiredNext(next, JobStatus.FINAL_APPROVED);

            case FINAL_APPROVED -> throw new BadRequestException("Job is FINAL_APPROVED. No further changes in simplified flow.");

            case COMPLETED -> throw new BadRequestException("Job is already COMPLETED, cannot change status");

            default -> throw new BadRequestException("Unsupported job status transition");
        }
    }



    private void requiredNext(JobStatus next, JobStatus... allowed){
        EnumSet<JobStatus> allowedSet = EnumSet.noneOf(JobStatus.class);
        for (JobStatus s : allowed){
            allowedSet.add(s);
        }
        if (!allowedSet.contains(next)){
            throw new BadRequestException("Invalid status transition to " + next);
        }
    }

    @Transactional
    public Job requestInspection(Long jobId){
        Job job = getJob(jobId);
        if (job.getStatus() != JobStatus.IN_WORK){
            throw new BadRequestException("Job must be IN_WORK to request inspection ");
        }
        job.setStatus(JobStatus.READY_FOR_INSPECTION);
        return jobRepository.save(job);
    }

    @Transactional
    public Job requestFinal(Long jobId) {
        Job job = getJob(jobId);

        if (job.getStatus() != JobStatus.READY_FOR_INSPECTION) {
            throw new BadRequestException("Job must be READY_FOR_INSPECTION to request final");
        }

        job.setStatus(JobStatus.READY_FOR_FINAL);
        return jobRepository.save(job);
    }

}
