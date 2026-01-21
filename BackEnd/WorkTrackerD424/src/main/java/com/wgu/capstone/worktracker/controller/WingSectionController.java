package com.wgu.capstone.worktracker.controller;

import com.wgu.capstone.worktracker.dto.WingSectionRespone;
import com.wgu.capstone.worktracker.entity.WingSection;
import com.wgu.capstone.worktracker.service.WingSectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wing-sections")
public class WingSectionController {
    private final WingSectionService wingSectionService;

    public WingSectionController(WingSectionService wingSectionService) {
        this.wingSectionService = wingSectionService;
    }

    @GetMapping
    public List<WingSectionRespone> listAll(){
        List<WingSection> sections = wingSectionService.listAll();
        return sections.stream()
                .map(ws -> new WingSectionRespone(ws.getId(), ws.getName(), ws.getDescription(), ws.getSvgRegionId()))
                .toList();
    }
}
