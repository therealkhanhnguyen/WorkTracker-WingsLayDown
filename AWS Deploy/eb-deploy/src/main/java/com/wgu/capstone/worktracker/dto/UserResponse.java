package com.wgu.capstone.worktracker.dto;

import com.wgu.capstone.worktracker.enumtype.Role;

public class UserResponse {
    private Long id;
    private String displayName;
    private Role role;

    public UserResponse() {
    }

    public UserResponse(Long id, String displayName, Role role) {
        this.id = id;
        this.displayName = displayName;
        this.role = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }
}
