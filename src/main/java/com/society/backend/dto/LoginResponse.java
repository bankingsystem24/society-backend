package com.society.backend.dto;

public class LoginResponse {

    private String token;
    private Long societyId;
    private String societyName;
    private String role;
    private Long auditorId;
    private String name;
    private Long memberId;
    private String upi;

    public LoginResponse() {
    }

    public LoginResponse(String token, Long societyId, String societyName,String role, Long auditorId,String name,Long memberId,String upi) {
        this.token = token;
        this.societyId = societyId;
        this.societyName = societyName;
        this.role = role;
        this.auditorId = auditorId;
        this.name = name;
        this.memberId = memberId;
        this.upi = upi;
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

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
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

    public Long getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(Long auditorId) {
        this.auditorId = auditorId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getUpi(){return upi;}
    public void setUpi(String upi){this.upi=upi;}
}