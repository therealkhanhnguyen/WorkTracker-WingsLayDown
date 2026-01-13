package com.wgu.capstone.worktracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "wing_sections")
public class WingSection {

    //SVG already has string id. Remember to match to UI.
    @Id
    @Column(name = "id", nullable = false, updatable = false,length = 64)
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name ="svg_region_id", nullable = false, unique = true, length = 64)
    private String svgRegionId;

    @Column(name = "description", length = 255)
    private String description;

    protected WingSection() {
    }

    public WingSection(String id, String name, String svgRegionId, String description) {
        this.id = id;
        this.name = name;
        this.svgRegionId = svgRegionId;
        this.description = description;
    }

    //getter (encapsulation)

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSvgRegionId() {
        return svgRegionId;
    }

    //setters

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSvgRegionId(String svgRegionId) {
        this.svgRegionId = svgRegionId;
    }
}
