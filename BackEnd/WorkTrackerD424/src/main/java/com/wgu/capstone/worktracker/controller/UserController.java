package com.wgu.capstone.worktracker.controller;

import com.wgu.capstone.worktracker.dto.UserResponse;
import com.wgu.capstone.worktracker.entity.User;
import com.wgu.capstone.worktracker.enumtype.Role;
import com.wgu.capstone.worktracker.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
      this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> list(@RequestParam(required = false) Role role){
        List<User> users = (role == null) ? userService.listAll() : userService.listByRole(role);

        return users.stream().map(u -> new UserResponse(u.getId(), u.getDisplayName(), u.getRole())).
                toList();
    }
}
