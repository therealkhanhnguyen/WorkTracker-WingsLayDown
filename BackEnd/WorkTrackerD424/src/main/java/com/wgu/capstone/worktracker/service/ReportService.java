package com.wgu.capstone.worktracker.service;

import com.wgu.capstone.worktracker.entity.Job;
import com.wgu.capstone.worktracker.enumtype.JobStatus;
import com.wgu.capstone.worktracker.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    private final JobRepository jobRepository;

    public ReportService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public String buildJobCycleTimeCsv() {
        // Only FINAL_APPROVED jobs as you requested
        List<Job> jobs = jobRepository.findByStatus(JobStatus.FINAL_APPROVED);

        String title = "WorkTracker Job Cycle Time Report";
        String generatedAt = Instant.now().toString(); // ISO-8601 UTC

        StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n");
        sb.append("Generated At: ").append(generatedAt).append("\n\n");

        // Multiple columns
        sb.append("Job Code,Job Title,Wing Section,Started At,Final Approved At,Total Minutes,Total Hours\n");

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.systemDefault());

        for (Job j : jobs) {
            String jobCode = formatJobCode(j.getId()); // WT-000123 style
            String titleCol = j.getTitle();
            String wingSectionName = (j.getWingSection() != null) ? j.getWingSection().getName() : "";

            Instant startedAt = j.getStartedAt();
            Instant finalApprovedAt = j.getCompletedAt(); // we set this when FINAL_APPROVED happens

            String startedAtStr = (startedAt != null) ? formatter.format(startedAt) : "";
            String finalApprovedAtStr = (finalApprovedAt != null) ? formatter.format(finalApprovedAt) : "";


            Long totalMinutes = null;
            Double totalHours = null;

            if (startedAt != null && finalApprovedAt != null && !finalApprovedAt.isBefore(startedAt)) {
                long minutes = Duration.between(startedAt, finalApprovedAt).toMinutes();
                totalMinutes = minutes;
                totalHours = minutes / 60.0;
            }

            sb.append(csv(jobCode)).append(",");
            sb.append(csv(titleCol)).append(",");
            sb.append(csv(wingSectionName)).append(",");
            sb.append(csv(startedAtStr)).append(",");
            sb.append(csv(finalApprovedAtStr)).append(",");
            sb.append(totalMinutes != null ? totalMinutes : "").append(",");
            sb.append(totalHours != null ? String.format("%.2f", totalHours) : "");
            sb.append("\n");
        }

        return sb.toString();
    }

    private String formatJobCode(Long id) {
        if (id == null) return "";
        return "WT-" + String.format("%06d", id);
    }

    // Minimal CSV escaping: wrap in quotes if it contains comma, quote, or newline
    private String csv(String value) {
        if (value == null) return "";
        boolean mustQuote = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        if (!mustQuote) return value;
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
