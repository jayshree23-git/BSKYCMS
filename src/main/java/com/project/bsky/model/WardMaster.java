package com.project.bsky.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name ="WARDMASTER")
public class WardMaster {
    @Id
    @GenericGenerator(name = "catInc", strategy = "increment")
    @GeneratedValue(generator = "catInc")
    @Column(name = "WARDMASTERID")
    private Long id;

    @Column(name="IMPLANTCODE")
    private String implantCode;

    @Column(name="WARDNAME")
    private String wardName;

    @Column(name="PROCEDURECODE")
    private String procedureCode;

    @Column(name="UNIT")
    private Integer unit;

    @Column(name="MAXIMUMUNIT")
    private Integer maximumUnit;

    @Column(name="UNITCYCLEPRICE")
    private String unitCyclePrice;

    @Column(name="PRICEFIXEDEDITABLE")
    private String priceFixedEditable;

    @Column(name="CREATEDBY")
    private Integer cretedBy;

    @Column(name="CREATEDON")
    private Date createdOn;

    @Column(name="UPDATEDBY")
    private String updatedBy;

    @Column(name="UPDATEDON")
    private Date updatedOn;

    @Column(name="DELETEDFLAG")
    private Integer deletedFlag;

    @Column(name="UNITPRICE")
    private Integer unitPrice;

    @Column(name="PRICEFIXEDED")
    private Integer priceFixeded;

    @Column(name="WARDTYPE")
    private Integer WARDTYPE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImplantCode() {
        return implantCode;
    }

    public void setImplantCode(String implantCode) {
        this.implantCode = implantCode;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Integer getMaximumUnit() {
        return maximumUnit;
    }

    public void setMaximumUnit(Integer maximumUnit) {
        this.maximumUnit = maximumUnit;
    }

    public String getUnitCyclePrice() {
        return unitCyclePrice;
    }

    public void setUnitCyclePrice(String unitCyclePrice) {
        this.unitCyclePrice = unitCyclePrice;
    }

    public String getPriceFixedEditable() {
        return priceFixedEditable;
    }

    public void setPriceFixedEditable(String priceFixedEditable) {
        this.priceFixedEditable = priceFixedEditable;
    }

    public Integer getCretedBy() {
        return cretedBy;
    }

    public void setCretedBy(Integer cretedBy) {
        this.cretedBy = cretedBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
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

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getPriceFixeded() {
        return priceFixeded;
    }

    public void setPriceFixeded(Integer priceFixeded) {
        this.priceFixeded = priceFixeded;
    }

    public Integer getWARDTYPE() {
        return WARDTYPE;
    }

    public void setWARDTYPE(Integer WARDTYPE) {
        this.WARDTYPE = WARDTYPE;
    }
}
