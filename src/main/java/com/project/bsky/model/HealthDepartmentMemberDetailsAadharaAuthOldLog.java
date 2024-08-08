package com.project.bsky.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "HEALTHDEPARTMENTMEMBERDETAILS_AADHARAAUTH_OLD_LOG")
public class HealthDepartmentMemberDetailsAadharaAuthOldLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HEALTHDEPARTMENTMEMBERDETAILS_AADHARAAUTH_OLD_LOG_id_gen")
    @SequenceGenerator(name = "HEALTHDEPARTMENTMEMBERDETAILS_AADHARAAUTH_OLD_LOG_id_gen", sequenceName = "HEALTHDEPARTMENTMEMBERDETAILS_AADHARAAUTH_OLD_LOG_SEQ", allocationSize = 1)
    @Column(name = "OLD_DATA_ID", nullable = false)
    private Long id;

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