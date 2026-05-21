package com.society.backend.dto;

public class LoginResponse {

    private String token;
    private Long societyId;
    private String societyName;

    public LoginResponse() {
    }

    public LoginResponse(String token, Long societyId, String societyName) {
        this.token = token;
        this.societyId = societyId;
        this.societyName = societyName;
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

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }
}