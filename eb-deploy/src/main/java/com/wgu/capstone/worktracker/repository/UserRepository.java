package com.wgu.capstone.worktracker.repository;

import com.wgu.capstone.worktracker.entity.User;
import com.wgu.capstone.worktracker.enumtype.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(Role role);
}
