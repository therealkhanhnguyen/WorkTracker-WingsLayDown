package com.wgu.capstone.worktracker.service;

import com.wgu.capstone.worktracker.entity.WingSection;
import com.wgu.capstone.worktracker.repository.WingSectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WingSectionService {
    private final WingSectionRepository wingSectionRepository;

    public WingSectionService(WingSectionRepository wingSectionRepository) {
        this.wingSectionRepository = wingSectionRepository;
    }

    public List<WingSection> listAll() {
        return wingSectionRepository.findAll();
    }
}
