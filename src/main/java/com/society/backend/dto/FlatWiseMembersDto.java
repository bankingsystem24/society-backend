package com.society.backend.dto;

public class FlatWiseMembersDto {
    private String flatNo;
    private String memberName;
    private String mobile;
    private String email;
    private String memberType;
    private String ownershipType;
    private Boolean active;

    public FlatWiseMembersDto() {
    }

    public FlatWiseMembersDto(String flatNo,
                              String memberName, String mobile, String email,
                              String memberType, String ownershipType, Boolean active
                              ) {

        this.flatNo = flatNo;
        this.memberName = memberName;
        this.mobile = mobile;
        this.email = email;
        this.memberType = memberType;
        this.ownershipType = ownershipType;
        this.active = active;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(String ownershipType) {
        this.ownershipType = ownershipType;
    }

    public Boolean getActive(){return active;}
    public void setActive(Boolean active){this.active = active;}

}