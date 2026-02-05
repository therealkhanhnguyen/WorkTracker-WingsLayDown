package com.wgu.capstone.worktracker.repository;

import com.wgu.capstone.worktracker.entity.Job;
import com.wgu.capstone.worktracker.enumtype.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatus(JobStatus status);
    List<Job> findByWingSection_Id(String wingSectionId);
    boolean existsByWingSection_IdAndStatusNot(String wingSectionId, JobStatus status);

}
