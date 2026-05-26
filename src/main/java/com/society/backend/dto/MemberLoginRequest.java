package com.society.backend.dto;

public class MemberLoginRequest {

    private String username;
    private String password;

    // ✅ REQUIRED
    public MemberLoginRequest() {
    }

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
}