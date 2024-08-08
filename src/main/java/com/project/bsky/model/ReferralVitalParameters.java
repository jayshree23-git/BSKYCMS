package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "REFERRALVITALPARAMETERS")
public class ReferralVitalParameters {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REFERRALVITALPARAMETERS_SEQ")
    @SequenceGenerator(sequenceName = "REFERRALVITALPARAMETERS_SEQ", name = "REFERRALVITALPARAMETERS_SEQ",allocationSize = 1)
    private Long referalVitalId;

//    @Column(name = "REFERRALID")
//    private Long referralId;

    public Long getVital() {
        return vital;
    }

    public void setVital(Long vital) {
        this.vital = vital;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "VITALSIGNID")
    private Long vital;

    @Column(name = "VITALVALUE")
    private String value;

    @Column(name = "UPLOADDATE")
    private Date uploadDate;

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

    public Referral getReferral() {
        return referral;
    }

    public void setReferral(Referral referral) {
        this.referral = referral;
    }

    @ManyToOne
    @JoinColumn(name="REFERRALID")
    private Referral referral;

    public Long getReferalVitalId() {
        return referalVitalId;
    }

    public void setReferalVitalId(Long referalVitalId) {
        this.referalVitalId = referalVitalId;
    }

//    public Long getReferralId() {
//        return referralId;
//    }
//
//    public void setReferralId(Long referralId) {
//        this.referralId = referralId;
//    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

}

