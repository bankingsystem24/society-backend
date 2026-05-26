package com.society.backend.dto;

public class MemberLoginResponse {

    private String token;
    private Long societyId;
    private String societyName;
    private Long memberId;
    private String memberName;
    private String role;

    public MemberLoginResponse() {
    }

    public MemberLoginResponse(String token, Long societyId, String societyName,Long memberId,String memberName, String role) {
        this.token = token;
        this.societyId = societyId;
        this.societyName = societyName;
        this.memberId = memberId;
        this.memberName = memberName;
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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}