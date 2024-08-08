package com.project.bsky.bean;

import java.util.Date;

public class HighEndDrugsBean {
    private Long implantDetailsId;
    private String hedCode;
    private String hedName;
    private Integer unit;
    private Integer price;
    private String maximumUnit;
    private String recomendedDose;

    private String isPreAuthRequired;
    private Date createdOn;
    private Integer createdBy;
    private Date updatedOn;
    private Integer updatedBy;
    private String unitEditable;
    private String priceEditable;
    private String implantCode;
    private Integer wardCategoryId;

    public Long getImplantDetailsId() {
        return implantDetailsId;
    }

    public void setImplantDetailsId(Long implantDetailsId) {
        this.implantDetailsId = implantDetailsId;
    }

    public String getHedCode() {
        return hedCode;
    }

    public void setHedCode(String hedCode) {
        this.hedCode = hedCode;
    }

    public String getHedName() {
        return hedName;
    }

    public void setHedName(String hedName) {
        this.hedName = hedName;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getMaximumUnit() {
        return maximumUnit;
    }

    public void setMaximumUnit(String maximumUnit) {
        this.maximumUnit = maximumUnit;
    }

    public String getRecomendedDose() {
        return recomendedDose;
    }

    public void setRecomendedDose(String recomendedDose) {
        this.recomendedDose = recomendedDose;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getIsPreAuthRequired() {
        return isPreAuthRequired;
    }

    public void setIsPreAuthRequired(String isPreAuthRequired) {
        this.isPreAuthRequired = isPreAuthRequired;
    }

    public String getUnitEditable() { return unitEditable; }

    public void setUnitEditable(String unitEditable) { this.unitEditable = unitEditable; }

    public String getPriceEditable() { return priceEditable; }

    public void setPriceEditable(String priceEditable) { this.priceEditable = priceEditable; }

    public String getImplantCode() { return implantCode; }

    public void setImplantCode(String implantCode) { this.implantCode = implantCode; }

    public Integer getWardCategoryId() { return wardCategoryId; }

    public void setWardCategoryId(Integer wardCategoryId) { this.wardCategoryId = wardCategoryId; }
}
