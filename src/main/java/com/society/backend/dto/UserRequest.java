package com.society.backend.dto;

import com.society.backend.enums.Role;

public class UserRequest {

    private String username;
    private String password;
    private String email;
    private String mobile;
    private Role role;
    private Boolean active;

    // Nested objects from frontend (keep same JSON structure)
    private IdRequest member;
    private IdRequest society;

    // ===== Inner reusable class for { id: X } =====
    public static class IdRequest {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    // ===== Getters / Setters =====

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public IdRequest getMember() {
        return member;
    }

    public void setMember(IdRequest member) {
        this.member = member;
    }

    public IdRequest getSociety() {
        return society;
    }

    public void setSociety(IdRequest society) {
        this.society = society;
    }
}