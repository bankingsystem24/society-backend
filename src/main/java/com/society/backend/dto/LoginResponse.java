package com.society.backend.dto;

public class LoginResponse {

    private String token;
    private Long societyId;
    private String societyName;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(String token, Long societyId, String societyName,String role) {
        this.token = token;
        this.societyId = societyId;
        this.societyName = societyName;
        this.role = role;
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

    public String getRole(){
        return role;
    }

    public void setRole(String role){ 
        this.role = role;
    }
}