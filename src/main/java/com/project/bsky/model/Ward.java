package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name = "WARDCATEGORYMASTER")
public class Ward implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "WARDCATEGORYID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long wardMasterId;

//	@Column(name = "PROCEDURECODE")
//	private String  procedureCode;
	
	
	@Column(name = "IMPLANTCODE")
	private String implantCode;
	

	@Column(name = "WARDNAME")
	private String  wardName;
	
	@Column(name = "UNIT")
	private Integer  unit;
	
	@Column(name = "MAXIMUMUNIT")
	private Integer  maximumUnit;
	
//	@Column(name = "UNITCYCLEPRICE")
//	private String  unitCyclePrice;
//	
//	@Column(name = "PRICEFIXEDEDITABLE")
//	private String  priceFixedEditable;
	
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


	public Long getWardMasterId() {
		return wardMasterId;
	}


	public void setWardMasterId(Long wardMasterId) {
		this.wardMasterId = wardMasterId;
	}


//	public String getProcedureCode() {
//		return procedureCode;
//	}
//
//
//	public void setProcedureCode(String procedureCode) {
//		this.procedureCode = procedureCode;
//	}


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


//	public String getUnitCyclePrice() {
//		return unitCyclePrice;
//	}
//
//
//	public void setUnitCyclePrice(String unitCyclePrice) {
//		this.unitCyclePrice = unitCyclePrice;
//	}
//
//
//	public String getPriceFixedEditable() {
//		return priceFixedEditable;
//	}
//
//
//	public void setPriceFixedEditable(String priceFixedEditable) {
//		this.priceFixedEditable = priceFixedEditable;
//	}


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


	public static long getSerialversionuid() {
		return serialVersionUID;
	}



}
