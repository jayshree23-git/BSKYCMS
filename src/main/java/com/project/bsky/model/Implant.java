package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain=true)
@ToString
@Table(name = "MSTIMPLANT")
public class Implant  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "IMPLANTID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long implantId;
	
	
//	@Column(name = "PROCEDURECODE")
//	private String  procedureCode;
	
	
	@Column(name = "IMPLANTCODE")
	private String implantCode;
	

	@Column(name = "IMPLANTNAME")
	private String  implantName;
	
	@Column(name = "UNIT")
	private Integer  unit;
	
	@Column(name = "MAXIMUMUNIT")
	private Integer  maximumUnit;
	
	@Column(name = "UNITCYCLEPRICE")
	private String  perunitCyclePrice;
	
	@Column(name = "PRICEEDITABLE")
	private String  priceEditable;
	
	@Column(name = "UNITEDITABLE")
	private String  unitEditable;
	
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

	
	public Long getImplantId() {
		return implantId;
	}

	public void setImplantId(Long implantId) {
		this.implantId = implantId;
	}

	public String getUnitEditable() {
		return  unitEditable;
	}

	public void setUnitEditable(String  unitEditable) {
		 this.unitEditable =  unitEditable;
	}
	public String getImplantCode() {
		return  implantCode;
	}

	public void setImplantCode(String  implantCode) {
		 this.implantCode =  implantCode;
	}
	public String getImplantName() {
		return  implantName;
	}

	public void setImplantName(String  implantName) {
		 this.implantName =  implantName;
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
		return perunitCyclePrice;
	}

	public void setUnitCyclePrice(String perunitCyclePrice) {
		this.perunitCyclePrice = perunitCyclePrice;
	}
	public String getPriceEditable() {
		return priceEditable;
	}

	public void setPriceEditable(String priceEditable) {
		this.priceEditable = priceEditable;
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

	
	
	
	
	
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	} 
	

}
