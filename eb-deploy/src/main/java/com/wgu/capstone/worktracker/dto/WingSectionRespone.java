package com.wgu.capstone.worktracker.dto;

public class WingSectionRespone {
    private String id;
    private String name;
    private String description;
    private String svgRegionId;

    public WingSectionRespone() {
    }

    public WingSectionRespone(String id, String name, String description, String svgRegionId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.svgRegionId = svgRegionId;
    }

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
}
