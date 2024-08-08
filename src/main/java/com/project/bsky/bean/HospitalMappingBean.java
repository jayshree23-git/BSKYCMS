package com.project.bsky.bean;

import java.util.Date;

public class HospitalMappingBean {
    private String hospitalState;
    private String hospitalDistrict;
    private String hospitalCode;
    private Long packageHeaderId;
    private String packageDetailsId;
    private Integer createdBy;
    private Integer updatedBy;
    private Date createdOn;
    private Date updatedOn;
    private Long packageSubcategoryId;

    public String getHospitalState() {
        return hospitalState;
    }

    public void setHospitalState(String hospitalState) {
        this.hospitalState = hospitalState;
    }

    public String getHospitalDistrict() {
        return hospitalDistrict;
    }

    public void setHospitalDistrict(String hospitalDistrict) {
        this.hospitalDistrict = hospitalDistrict;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public Long getPackageHeaderId() {
        return packageHeaderId;
    }

    public void setPackageHeaderId(Long packageHeaderId) {
        this.packageHeaderId = packageHeaderId;
    }

    public String getPackageDetailsId() {
        return packageDetailsId;
    }

    public void setPackageDetailsId(String packageDetailsId) {
        this.packageDetailsId = packageDetailsId;
    }

    public Integer getCreatedBy() { return createdBy; }

    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy;}

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getPackageSubcategoryId() { return packageSubcategoryId; }

    public void setPackageSubcategoryId(Long packageSubcategoryId) { this.packageSubcategoryId = packageSubcategoryId; }
}
