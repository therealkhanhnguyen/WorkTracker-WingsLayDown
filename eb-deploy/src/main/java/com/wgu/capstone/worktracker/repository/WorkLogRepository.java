package com.wgu.capstone.worktracker.repository;

import com.wgu.capstone.worktracker.entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
    List<WorkLog> findByJob_IdOrderByStartTimeDesc(Long jobId);
    List<WorkLog> findByUser_IdOrderByStartTimeAsc(Long userId);
}
