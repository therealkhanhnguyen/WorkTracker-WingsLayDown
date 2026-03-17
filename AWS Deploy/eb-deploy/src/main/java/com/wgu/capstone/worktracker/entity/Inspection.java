package com.wgu.capstone.worktracker.entity;

import com.wgu.capstone.worktracker.enumtype.InspectionResult;
import com.wgu.capstone.worktracker.enumtype.InspectionType;
import jakarta.persistence.*;

@Entity
@Table(name = "inspections")
public class Inspection extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(optional = false)
    @JoinColumn(name = "inspected_by_user_id")
    private User inspectedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InspectionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InspectionResult result;

    @Column(length = 50)
    private String notes;

    public Inspection() {
    }

    public Inspection(Long id, Job job, User inspectedBy, String notes, InspectionResult result, InspectionType type) {
        this.id = id;
        this.job = job;
        this.inspectedBy = inspectedBy;
        this.notes = notes;
        this.result = result;
        this.type = type;
    }

    public InspectionType getType() {
        return type;
    }

    public void setType(InspectionType type) {
        this.type = type;
    }

    public InspectionResult getResult() {
        return result;
    }

    public void setResult(InspectionResult result) {
        this.result = result;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public User getInspectedBy() {
        return inspectedBy;
    }

    public void setInspectedBy(User inspectedBy) {
        this.inspectedBy = inspectedBy;
    }

    public Long getId() {
        return id;
    }
}
