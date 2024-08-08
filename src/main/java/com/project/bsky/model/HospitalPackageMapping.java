package com.project.bsky.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name ="Hospitalpackage_Mapping_TMS")
public class HospitalPackageMapping {
    @Id
    @GenericGenerator(name = "catInc", strategy = "increment")
    @GeneratedValue(generator = "catInc")
    @Column(name = "HOSPITALPACKAGEMAPPING_ID")
    private Long id;

    @Column(name = "HOSPITALSTATE")
    private String hospitalState;

    @Column(name = "HOSPITALDISTRICT")
    private String hospitalDistrict;

    @ManyToOne
    @JoinColumn(name = "HOSPITALCODE", referencedColumnName = "HOSPITAL_CODE")
    private HospitalInformation hospitalCode;
    @ManyToOne
    @JoinColumn(name="PACKAGEHEADERID")
    //@Column(name = "PACKAGEHEADERID")
    private PackageHeader packageHeaderId;
    @ManyToOne
    @JoinColumn(name="PACKAGESUBCATEGORYID")
    //@Column(name = "PACKAGESUBCATEGORYID")
    private PackageSubCategory packageSubcategoryId;
    @ManyToOne
    @JoinColumn(name="PACKAGEDETAILSID")
    //@Column(name = "PACKAGEDETAILSID")
    private PackageDetailsHospital packageDetailsId;

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


    public String getHospitalState() {
        return hospitalState;
    }

    public void setHospitalState(String hospitalState) {
        this.hospitalState = hospitalState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHospitalDistrict() {
        return hospitalDistrict;
    }

    public void setHospitalDistrict(String hospitalDistrict) {
        this.hospitalDistrict = hospitalDistrict;
    }

    public HospitalInformation getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(HospitalInformation hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public PackageHeader getPackageHeaderId() {
        return packageHeaderId;
    }

    public void setPackageHeaderId(PackageHeader packageHeaderId) {
        this.packageHeaderId = packageHeaderId;
    }

    public PackageSubCategory getPackageSubcategoryId() {
        return packageSubcategoryId;
    }

    public void setPackageSubcategoryId(PackageSubCategory packageSubcategoryId) {
        this.packageSubcategoryId = packageSubcategoryId;
    }

    public PackageDetailsHospital getPackageDetailsId() {
        return packageDetailsId;
    }

    public void setPackageDetailsId(PackageDetailsHospital packageDetailsId) {
        this.packageDetailsId = packageDetailsId;
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
