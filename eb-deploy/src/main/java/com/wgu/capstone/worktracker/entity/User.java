package com.wgu.capstone.worktracker.entity;

import com.wgu.capstone.worktracker.enumtype.Role;
import jakarta.persistence.*;

@Entity
@Table(
        name = "users",

        //set this up for search function later
        indexes = {
                @Index( name = "idx_users_role", columnList = "role"),
                @Index(name = "idx_users_display_name", columnList = "display_name")
        }

)
public class User extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    protected User() {}

    public User(String displayName, Role role) {
        this.displayName = displayName;
        this.role = role;
    }

    //Getter

    public String getDisplayName() {
        return displayName;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }
    //Setters

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
