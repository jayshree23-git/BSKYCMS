package com.project.bsky.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="TBL_OSTFBALANCE_TEST")
public class CardPolicy {
    @Id
    @GenericGenerator(name = "catInc", strategy = "increment")
    @GeneratedValue(generator = "catInc")
    @Column(name = "ID")
    private Long id;

    @Column(name = "URN")
    private String urn;

    @Column(name="CHIPNO")
    private String chipNo;

    @Column(name="BALANCEAMOUNT")
    private Integer balanceAmount;

    @Column(name="BLOCKEDAMOUNT")
    private Integer blockedAmount;

    @Column(name="CLAIMEDAMOUNT")
    private Integer claimedAmount;
    @DateTimeFormat(fallbackPatterns = "dd-MMM-yy")
    @Column(name="STARTDATE")
    private Date startDate;

    @Column(name="ENDDATE")
    private Date endDate;

    @Column(name="UPDATEDDATE")
    private Date updatedDate;

    @Column(name="STATECODE")
    private String stateCode;

    @Column(name="DISTRICTCODE")
    private String districtCode;

    @Column(name="OSTFFLAG")
    private String ostfFlag;

    @Column(name="INSUFFICIENTAMOUNT")
    private Integer insufficientAmount;

    @Column(name="FEMALEFUND")
    private Integer femaleFund;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getChipNo() {
        return chipNo;
    }

    public void setChipNo(String chipNo) {
        this.chipNo = chipNo;
    }

    public Integer getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Integer balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public Integer getBlockedAmount() {
        return blockedAmount;
    }

    public void setBlockedAmount(Integer blockedAmount) {
        this.blockedAmount = blockedAmount;
    }

    public Integer getClaimedAmount() {
        return claimedAmount;
    }

    public void setClaimedAmount(Integer claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getOstfFlag() {
        return ostfFlag;
    }

    public void setOstfFlag(String ostfFlag) {
        this.ostfFlag = ostfFlag;
    }

    public Integer getInsufficientAmount() {
        return insufficientAmount;
    }

    public void setInsufficientAmount(Integer insufficientAmount) {
        this.insufficientAmount = insufficientAmount;
    }

    public Integer getFemaleFund() {
        return femaleFund;
    }

    public void setFemaleFund(Integer femaleFund) {
        this.femaleFund = femaleFund;
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
