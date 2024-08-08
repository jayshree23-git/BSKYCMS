package com.project.bsky.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@Table(name = "HEALTHDEPARTMENTBASICDETAILS_AADHARAAUTH")
public class HealthDepartmentBasicDetailsAadharaauth {
    @Id
    @Column(name = "HEALTHSLNO", nullable = false)
    private Long healthslno;

    @Column(name = "RATIONCARDNUMBER", length = 20)
    private String rationCardNumber;

    @Column(name = "RATIONCARDTYPE", length = 20)
    private String rationCardType;

    @Column(name = "FULLNAMEENGLISH", length = 1000)
    private String fullNameEnglish;

    @Column(name = "FULLNAMEODIYA", length = 1000)
    private String fullNameOdiya;

    @Column(name = "AADHAARNUMBER", length = 20)
    private String aadhaarNumber;

    @Column(name = "GENDER", length = 20)
    private String gender;

    @Column(name = "SPOUSEFULLNAME", length = 1000)
    private String spouseFullName;

    @Column(name = "FATHERFULLNAME", length = 1000)
    private String fatherFullName;

    @Column(name = "MOBILENUMBER", length = 20)
    private String mobileNumber;

    @Column(name = "DISTRICT", length = 100)
    private String district;

    @Column(name = "DISTRICTID")
    private Integer districtId;

    @Column(name = "BLOCK_ULB", length = 500)
    private String blockUlb;

    @Column(name = "BLOCKID_ULBID")
    private Integer blockIdUlbId;

    @Column(name = "GP_WARD", length = 500)
    private String gpWard;

    @Column(name = "GPID_WARDID")
    private Integer gpIdWardId;

    @Column(name = "LOCALITY_VILLAGE", length = 500)
    private String localityVillage;

    @Column(name = "LOCALITYID_VILLAGEID")
    private Integer localityIdVillageId;

    @Column(name = "FPSNAME", length = 500)
    private String fpsName;

    @Column(name = "SCHEMETYPE", length = 20)
    private String schemeType;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "ADDITIONDELETIONSTATUS", length = 20)
    private String additionDeletionStatus;

    @Column(name = "EXPORTDATE")
    private Date exportDate;

    @Column(name = "UPDATEDATE")
    private Date updateDate;
}