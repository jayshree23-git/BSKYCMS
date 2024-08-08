package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "REFERRALPROCEDURES")
public class Referral {

    @Id
    @Column(name = "ID") //for oracle sequence generate
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REFERRALPROCEDURES_SEQ")
    @SequenceGenerator(sequenceName = "REFERRALPROCEDURES_SEQ", name = "REFERRALPROCEDURES_SEQ", allocationSize = 1)
    private Long refId;

    @Column(name = "MEMBERID")
    private Long memberId;

    @Column(name = "URN")
    private String urn;

    @Column(name = "PROCEDURECODE")
    private String procedureCode;

    @Column(name = "PACKAGECODE")
    private String packageCode;

    @Column(name = "HOSPITALCODE")
    private String hospitalCode;

    @Column(name = "PDFNAME")
    private String pdfName;

    @Column(name = "NOOFDAYS")
    private Long noOfDays;

    @Column(name = "REFERRALDATE")
    private Date referralDate;

    @Column(name = "REFERRALCODE")
    private String referralCode;

    @Column(name = "REFERRALSTATUS")
    private String referralStatus;

    @Column(name = "APPROVEDBY")
    private String approvedBy;

    @Column(name = "APPROVEDDATE")
    private Date approvedDate;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "PATIENTNAME")
    private String patientName;

    @Column(name = "AGE")
    private Long age;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "REGDNO")
    private String regdno;

    @Column(name = "FROMHOSPITALNAME")
    private String fromHospitalName;

    @Column(name = "FROMDRNAME")
    private String fromDrName;

    @Column(name = "FROMDEPTNAME")
    private String fromDeptName;

    @Column(name = "FROMREFERRALDATE")
    private Date fromReferralDate;

    @Column(name = "TOSTATE")
    private String stateId;

    @Column(name = "TODISTRICT")
    private String districtId;

    @Column(name = "TOHOSPITAL")
    private String toHospital;

    @Column(name = "REASONFORREFER")
    private String reasonForRefer;

    @Column(name = "TOREFERRALDATE")
    private Date toReferralDate;

    @Column(name = "DIAGNOSIS")
    private String diagnosis;

    @Column(name = "BRIEFHISTORY")
    private String briefHistory;

    @Column(name = "TREATMENTGIVEN")
    private String treatmentGiven;

    @Column(name = "INVESTIGATIONREMARK")
    private String investigationRemark;

    @Column(name = "TREATMENTADVISED")
    private String treatmentAdvised;

    @Column(name = "REFERRALDOC")
    private String document;

    @Column(name = "CREATEDBY")
    private Integer createdBy;

    @Column(name = "CREATEDON")
    private Date createdOn;

    @Column(name = "UPDATEDBY")
    private Integer updatedBy;

    @Column(name = "UPDATEDON")
    private Date updatedOn;

    @Column(name = "DELETEDFLAG")
    private Integer deletedFlag;

    @Column(name = "REFERREDTHROUGH")
    private String referredThrough;
//    public List<ReferralVitalParameters> getVitalParam() {
//        return vitalParam;
//    }
//
//    public void setVitalParam(List<ReferralVitalParameters> vitalParam) {
//        this.vitalParam = vitalParam;
//    }

//    @OneToMany
    @Column(name = "REFERRALCODEUSESTATUS")
    private Integer referralCodeStatus;

    @Column(name = "ISACTIVE")
    private Integer isActive;

    @Column(name = "AUTHSTATUS")
    private String authStatus;

    @Column(name = "FROMHOSPITALCODE")
    private String fromHospitalCode;

    @Column(name = "TOHOSPITALCODE")
    private String toHospitalCode;

    @Column(name = "ISEMPANELED")
    private String isEmpaneled;
    
    @Column(name = "SCHEMEID")
    private Integer schemeId;
    
    @Column(name = "SCHEMECATEGORYID")
    private Integer schemeCategoryId;
}
