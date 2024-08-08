package com.project.bsky.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name ="MSTHIGHENDDRUG")
public class HighEndDrugs {
    @Id
    @GenericGenerator(name = "catInc", strategy = "increment")
    @GeneratedValue(generator = "catInc")
    @Column(name = "HEDID")
    private Long id;
  //  @Column(name = "IMPLANTDETAILS_ID")
//    @ManyToOne
//    @JoinColumn(name="IMPLANTDETAILS_ID")
 //   private Integer implantDetailsId;
   // private Implant implantDetailsId;
    @Column(name="HED_CODE")
    private String hedCode;

    @Column(name="HED_NAME")
    private String hedName;

    @Column(name="UNIT")
    private Integer unit;

    @Column(name="PRICE")
    private Integer price;

    @Column(name="MAXIMUMUNIT")
    private String maximumUnit;

    public String getIsPreAuthRequired() {
        return isPreAuthRequired;
    }

    public void setIsPreAuthRequired(String isPreAuthRequired) {
        this.isPreAuthRequired = isPreAuthRequired;
    }

    @Column(name="ISPREAUTHREQUIRED")
    private String isPreAuthRequired;

    @Column(name="RECOMENDEDDOSE")
    private String recomendedDose;

    @Column(name="CREATEDON")
    private Date createdOn;

    @Column(name="CREATEDBY")
    private Integer createdBy;

    @Column(name="UPDATEDON")
    private Date updatedOn;

    @Column(name="UPDATEDBY")
    private Integer updatedBy;

    @Column(name="DELETEDFLAG")
    private Integer deletedFlag;

    @Column(name="UNITEDITABLE")
    private String unitEditable;

    @Column(name="PRICEEDITABLE")
    private String priceEditable;

//    @Column(name="IMPLANTCODE")
//    private String implantCode;
//    @ManyToOne
//    @JoinColumn(name="WARDCATEGORYID")
   // @Column(name="WARDCATEGORYID")
//    private WardCategoryMaster wardCategoryId;


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

//    public Implant getImplantDetailsId() {
//        return implantDetailsId;
//    }
//
//    public void setImplantDetailsId(Implant implantDetailsId) {
//        this.implantDetailsId = implantDetailsId;
//    }

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

    public Integer getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public String getUnitEditable() { return unitEditable; }

    public void setUnitEditable(String unitEditable) { this.unitEditable = unitEditable;}

    public String getPriceEditable() {
        return priceEditable;
    }

    public void setPriceEditable(String priceEditable) {
        this.priceEditable = priceEditable;
    }

//    public Integer getImplantDetailsId() { return implantDetailsId; }
//
//    public void setImplantDetailsId(Integer implantDetailsId) { this.implantDetailsId = implantDetailsId; }

//    public String getImplantCode() { return implantCode; }
//
//    public void setImplantCode(String implantCode) { this.implantCode = implantCode; }
//
//    public WardCategoryMaster getWardCategoryId() { return wardCategoryId; }
//
//    public void setWardCategoryId(WardCategoryMaster wardCategoryId) { this.wardCategoryId = wardCategoryId; }
}
