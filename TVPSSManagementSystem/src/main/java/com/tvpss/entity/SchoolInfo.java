package com.tvpss.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "schoolinfo")
public class SchoolInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    //private String schoolOfficer;
    private String schoolCode;
    private String schoolName;
    private String schoolEmail;
    private String schoolAddress1;
    private String schoolAddress2;
    private String district;
    private String postcode;
    private String state;
    private String noPhone;
    private String noFax;
    //private String schoolLogo;
    private String linkYoutube;

    /*@Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;*/

    @OneToOne(mappedBy = "schoolInfo", cascade = CascadeType.ALL)
    private TVPSSVersion tvpssVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /*public String getSchoolOfficer() {
        return schoolOfficer;
    }

    public void setSchoolOfficer(String schoolOfficer) {
        this.schoolOfficer = schoolOfficer;
    }*/

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolEmail() {
        return schoolEmail;
    }

    public void setSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }

    public String getSchoolAddress1() {
        return schoolAddress1;
    }

    public void setSchoolAddress1(String schoolAddress1) {
        this.schoolAddress1 = schoolAddress1;
    }

    public String getSchoolAddress2() {
        return schoolAddress2;
    }

    public void setSchoolAddress2(String schoolAddress2) {
        this.schoolAddress2 = schoolAddress2;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNoPhone() {
        return noPhone;
    }

    public void setNoPhone(String noPhone) {
        this.noPhone = noPhone;
    }

    public String getNoFax() {
        return noFax;
    }

    public void setNoFax(String noFax) {
        this.noFax = noFax;
    }

    /*public String getSchoolLogo() {
        return schoolLogo;
    }

    public void setSchoolLogo(String schoolLogo) {
        this.schoolLogo = schoolLogo;
    }*/

    public String getLinkYoutube() {
        return linkYoutube;
    }

    public void setLinkYoutube(String linkYoutube) {
        this.linkYoutube = linkYoutube;
    }

    /*public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }*/

    public TVPSSVersion getTvpssVersion() {
        return tvpssVersion;
    }

    public void setTvpssVersion(TVPSSVersion tvpssVersion) {
        this.tvpssVersion = tvpssVersion;
    }
    
    @Override
    public String toString() {
        return "SchoolInfo{" +
                "id=" + id +
                ", schoolCode='" + schoolCode + '\'' +
                ", schoolName='" + schoolName + '\'' +
                '}';
    }
}
