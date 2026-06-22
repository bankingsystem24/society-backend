package com.society.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "societies")
public class Society extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String societyName;
    private String registrationNumber;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pinCode;
    private String email;
    private String mobile;
    private String secretaryName;
    private Boolean active = true;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditor_id")
    private User auditor;
    private String upi1;
    private String upi2;
    private Boolean upi1Active;
    private Boolean upi2Active;

    // =========================
    // Getters and Setters
    // =========================

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSocietyName() {
        return societyName;
    }
    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getPinCode() {
        return pinCode;
    }
    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
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
    public String getSecretaryName() {
        return secretaryName;
    }
    public void setSecretaryName(String secretaryName) {
        this.secretaryName = secretaryName;
    }
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public User getAuditor() {
        return auditor;
    }
    public void setAuditor(User auditor) {
        this.auditor = auditor;
    }
    public String getUpi1(){ return upi1;}
    public String getUpi2(){ return upi2;}
    public void setUpi1(String upi1){ this.upi1 = upi1;}
    public void setUpi2(String upi2){ this.upi2 = upi2;}

    public Boolean getUpi1Active(){return upi1Active;}
    public Boolean getUpi2Active(){return upi2Active;}
    public void setUpi1Active(Boolean upi1Active){this.upi1Active = upi1Active;}
    public void setUpi2Active(Boolean upi2Active){this.upi2Active = upi2Active;}

}