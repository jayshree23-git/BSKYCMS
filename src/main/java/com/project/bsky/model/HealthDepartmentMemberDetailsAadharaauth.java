package com.project.bsky.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "HEALTHDEPARTMENTMEMBERDETAILS_AADHARAAUTH")
public class HealthDepartmentMemberDetailsAadharaauth {
    @Id
    @Column(name = "HEALTHMEMEBERSLNO", nullable = false)
    private Long healthmemeberslno;

    @Column(name = "RATIONCARDNUMBER", length = 20)
    private String rationCardNumber;

    @Column(name = "MEMBERID")
    private Long memberId;

    @Column(name = "FULLNAMEENGLISH", length = 1000)
    private String fullNameEnglish;

    @Column(name = "FULLNAMEODIYA", length = 1000)
    private String fullNameOdiya;

    @Column(name = "AADHARNUMBER", length = 20)
    private String aadharNumber;

    @Column(name = "GENDER", length = 20)
    private String gender;

    @Column(name = "DATEOFBIRTH")
    private Date dateOfBirth;

    @Column(name = "AGE", length = 10)
    private String age;

    @Column(name = "RELATIONWITHFAMILYHEAD", length = 100)
    private String relationWithFamilyHead;

    @Column(name = "SCHEMETYPE", length = 20)
    private String schemeType;

    @Column(name = "MOBILENUMBER", length = 20)
    private String mobileNumber;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "ADDITIONDELETIONSTATUS", length = 20)
    private String additionDeletionStatus;

    @Column(name = "EXPORTDATE")
    private Date exportDate;

    @Column(name = "UPDATEDATE")
    private Date updateDate;

}