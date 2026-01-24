package com.wgu.capstone.worktracker.service;

import com.wgu.capstone.worktracker.entity.Inspection;
import com.wgu.capstone.worktracker.entity.Job;
import com.wgu.capstone.worktracker.entity.User;
import com.wgu.capstone.worktracker.enumtype.InspectionResult;
import com.wgu.capstone.worktracker.enumtype.InspectionType;
import com.wgu.capstone.worktracker.enumtype.JobStatus;
import com.wgu.capstone.worktracker.exception.BadRequestException;
import com.wgu.capstone.worktracker.exception.NotFoundException;
import com.wgu.capstone.worktracker.repository.InspectionRepository;
import com.wgu.capstone.worktracker.repository.JobRepository;
import com.wgu.capstone.worktracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InspectionService {
    private final InspectionRepository inspectionRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public InspectionService(InspectionRepository inspectionRepository, JobRepository jobRepository, UserRepository userRepository) {
        this.inspectionRepository = inspectionRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public List<Inspection> listByJob(Long jobId) {
        if(!jobRepository.existsById(jobId)) {
            throw new NotFoundException("Job not found:" + jobId);
        }
        return inspectionRepository.findByJob_IdOrderByCreatedAtDesc(jobId);
    }

    @Transactional
    public Inspection createInspection(Long jobId, Long inspectorUserId, InspectionResult result,String notes) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job not found:" + jobId));
        User inspector = userRepository.findById(inspectorUserId)
                .orElseThrow(() -> new NotFoundException("User not found:" + inspectorUserId));
        //Decide inspection type based on job status
        InspectionType type;
        if (job.getStatus() == JobStatus.READY_FOR_INSPECTION){
            type = InspectionType.INITIAL;
        } else if(job.getStatus() == JobStatus.READY_FOR_FINAL){
            type = InspectionType.FINAL;
        } else {
            throw new BadRequestException("Job is not ready for inspection. Current status: " + job.getStatus());
        }

        Inspection inspection = new Inspection();
        inspection.setJob(job);
        inspection.setInspectedBy(inspector);
        inspection.setType(type);
        inspection.setResult(result);
        inspection.setNotes(notes);

        Inspection saved = inspectionRepository.save(inspection);

        //status transition

        if (result == InspectionResult.FAIL){
            job.setStatus(JobStatus.REWORK_REQUESTED);
        }else {
            //pass
            if (type == InspectionType.INITIAL){
                job.setStatus(JobStatus.READY_FOR_FINAL);
            } else {
                //final
                job.setStatus(JobStatus.COMPLETED);
            }
        }
        jobRepository.save(job);
        return saved;
    }
}
