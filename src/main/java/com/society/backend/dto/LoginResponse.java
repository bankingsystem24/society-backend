package com.society.backend.dto;

public class LoginResponse {

    private String token;
    private Long societyId;

    public LoginResponse() {
    }

    public LoginResponse(String token, Long societyId) {
        this.token = token;
        this.societyId = societyId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Long societyId) {
        this.societyId = societyId;
    }
}