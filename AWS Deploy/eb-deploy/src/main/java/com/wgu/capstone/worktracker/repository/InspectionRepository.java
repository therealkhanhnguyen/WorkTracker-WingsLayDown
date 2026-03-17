package com.wgu.capstone.worktracker.repository;

import com.wgu.capstone.worktracker.entity.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    List<Inspection> findByJob_IdOrderByCreatedAtDesc(Long jobId);
}
