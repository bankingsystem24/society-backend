package com.society.backend.dto;

import com.society.backend.entity.Flat;
import com.society.backend.entity.Society;

public class MemberRequest {

    private String name;
    private String email;
    private String mobile;
    private String address;
    private String gender;
    private String occupation;
    private String memberType;

    private Society society;
    private Flat flat ;

    // getters & setters

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getMemberType() { return memberType; }
    public void setMemberType(String memberType) { this.memberType = memberType; }

    public Society getSociety() { return society; }
    public void setSociety(Society society) { this.society = society; }

    public Flat getFlat() { return flat; }
    public void setFlat(Flat flat) { this.flat = flat; }

}
