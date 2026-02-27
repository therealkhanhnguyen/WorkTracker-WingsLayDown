package com.wgu.capstone.worktracker.config;

import com.wgu.capstone.worktracker.entity.User;
import com.wgu.capstone.worktracker.entity.WingSection;
import com.wgu.capstone.worktracker.enumtype.Role;
import com.wgu.capstone.worktracker.repository.UserRepository;
import com.wgu.capstone.worktracker.repository.WingSectionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final WingSectionRepository wingSectionRepository;
    private final UserRepository userRepository;

    public DataSeeder(WingSectionRepository wingSectionRepository, UserRepository userRepository) {
        this.wingSectionRepository = wingSectionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        seedWingSections();
        seedUsers();
    }

    private void seedWingSections() {
        //if section already exist do nothing
        if (wingSectionRepository.count() > 0) {
            return;
        }

        List<WingSection> sections = List.of(
                new WingSection("WS_0036YL_R", "0036YL/R", "WS_0036YL_R", "Wing section job area 0036YL/R"),
                new WingSection("WS_0035YL_R", "0035YL/R", "WS_0035YL_R", "Wing section job area 0035YL/R"),
                new WingSection("WS_0034YL_R", "0034YL/R", "WS_0034YL_R", "Wing section job area 0034YL/R"),

                new WingSection("WS_0022YL_R", "0022YL/R", "WS_0022YL_R", "Wing section job area 0022YL/R"),
                new WingSection("WS_0024YL_R", "0024YL/R", "WS_0024YL_R", "Wing section job area 0024YL/R"),

                new WingSection("WS_37910_14", "37910/14", "WS_37910_14", "Wing section job area 37910/14"),
                new WingSection("WS_37910_13", "37910/13", "WS_37910_13", "Wing section job area 37910/13"),
                new WingSection("WS_37909_102", "37909/102", "WS_37909_102", "Wing section job area 37909/102"),

                new WingSection("WS_0018YL_R", "0018YL/R", "WS_0018YL_R", "Wing section job area 0018YL/R"),
                new WingSection("WS_0615YL_R", "0615YL/R", "WS_0615YL_R", "Wing section job area 0615YL/R"),
                new WingSection("WS_0614YL_R", "0614YL/R", "WS_0614YL_R", "Wing section job area 0614YL/R")
        );
        wingSectionRepository.saveAll(sections);
        System.out.println("Seeded " + sections.size() + " Wing sections");

    }

    private void seedUsers(){
        if (userRepository.count() > 0) {
            return;
        }
        List<User> users = List.of(
                new User("Employee One", Role.EMPLOYEE),
                new User("Employee Two", Role.EMPLOYEE),
                new User("Employee Three", Role.EMPLOYEE),
                new User("Employee Four", Role.EMPLOYEE),
                new User("Employee Five", Role.EMPLOYEE)
        );
        userRepository.saveAll(users);
        System.out.println("Seeded " + users.size() + " users");
    }
}
