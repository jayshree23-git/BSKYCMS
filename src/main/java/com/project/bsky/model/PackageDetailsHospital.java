package com.project.bsky.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name ="PACKAGEDETAILS")
public class PackageDetailsHospital implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GenericGenerator(name = "catInc", strategy = "increment")
    @GeneratedValue(generator = "catInc")
    @Column(name = "ID")
    private Long id;

    @Column(name = "PACKAGEHEADERCODE")
    private String packageHeaderCode;

    @Column(name = "PACKAGESUBCATAGORYID")
    private Long packageSubcategoryId;

    @Column(name="PACKAGESUBCODE")
    private String packageSubCode;

    @Column(name="PROCEDURECODE")
    private String procedureCode;

    @Column(name="PROCEDUREDESCRIPTION")
    private String procedureDescription;

    @Column(name="HOSPITALCATEGORYID")
    private Integer hospitalCategoryId;

    @Column(name="AMOUNT")
    private Long amount;

    @Column(name="CREATEDBY")
    private Integer createdBy;

    @Column(name="CREATEDON")
    private Date createdOn;

    @Column(name="UPDATEDBY")
    private Integer updatedBy;

    @Column(name="UPDATEDON")
    private Date updatedOn;

    @Column(name="DELETEDFLAG")
    private Integer deletedFlag;

    @Column(name="PACKAGECATAGORYTYPE")
    private String packageCategoryType;

    @Column(name="MAXIMUMDAYS")
    private Integer maximumDays;

    @Column(name="MULTIPROCEDURE")
    private String multiProcedure;

    @Column(name="MANDATORYPREAUTH")
    private String mandatoryPreAuth;

    @Column(name="STAYTYPE")
    private String stayType;

    @Column(name="DAYCARE")
    private String dayCare;

    @Column(name="PREAUTHDOCS")
    private String preAuthDocs;

    @Column(name="CLAIMPROCESSDOCS")
    private String claimProcessDocs;

    @Column(name="SLNOTEMP")
    private String slNoTemo;

    @Column(name="AMOUNT_DESC")
    private String amountDesc;

    @Column(name="FEMALEAMOUNT")
    private Integer femaleAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageHeaderCode() {
        return packageHeaderCode;
    }

    public void setPackageHeaderCode(String packageHeaderCode) {
        this.packageHeaderCode = packageHeaderCode;
    }

    public Long getPackageSubcategoryId() {
        return packageSubcategoryId;
    }

    public void setPackageSubcategoryId(Long packageSubcategoryId) {
        this.packageSubcategoryId = packageSubcategoryId;
    }

    public String getPackageSubCode() {
        return packageSubCode;
    }

    public void setPackageSubCode(String packageSubCode) {
        this.packageSubCode = packageSubCode;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getProcedureDescription() {
        return procedureDescription;
    }

    public void setProcedureDescription(String procedureDescription) {
        this.procedureDescription = procedureDescription;
    }

    public Integer getHospitalCategoryId() {
        return hospitalCategoryId;
    }

    public void setHospitalCategoryId(Integer hospitalCategoryId) {
        this.hospitalCategoryId = hospitalCategoryId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
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

    public String getPackageCategoryType() {
        return packageCategoryType;
    }

    public void setPackageCategoryType(String packageCategoryType) {
        this.packageCategoryType = packageCategoryType;
    }

    public Integer getMaximumDays() {
        return maximumDays;
    }

    public void setMaximumDays(Integer maximumDays) {
        this.maximumDays = maximumDays;
    }

    public String getMultiProcedure() {
        return multiProcedure;
    }

    public void setMultiProcedure(String multiProcedure) {
        this.multiProcedure = multiProcedure;
    }

    public String getMandatoryPreAuth() {
        return mandatoryPreAuth;
    }

    public void setMandatoryPreAuth(String mandatoryPreAuth) {
        this.mandatoryPreAuth = mandatoryPreAuth;
    }

    public String getStayType() {
        return stayType;
    }

    public void setStayType(String stayType) {
        this.stayType = stayType;
    }

    public String getDayCare() {
        return dayCare;
    }

    public void setDayCare(String dayCare) {
        this.dayCare = dayCare;
    }

    public String getPreAuthDocs() {
        return preAuthDocs;
    }

    public void setPreAuthDocs(String preAuthDocs) {
        this.preAuthDocs = preAuthDocs;
    }

    public String getClaimProcessDocs() {
        return claimProcessDocs;
    }

    public void setClaimProcessDocs(String claimProcessDocs) {
        this.claimProcessDocs = claimProcessDocs;
    }

    public String getSlNoTemo() {
        return slNoTemo;
    }

    public void setSlNoTemo(String slNoTemo) {
        this.slNoTemo = slNoTemo;
    }

    public String getAmountDesc() {
        return amountDesc;
    }

    public void setAmountDesc(String amountDesc) {
        this.amountDesc = amountDesc;
    }

    public Integer getFemaleAmount() {
        return femaleAmount;
    }

    public void setFemaleAmount(Integer femaleAmount) {
        this.femaleAmount = femaleAmount;
    }
}
