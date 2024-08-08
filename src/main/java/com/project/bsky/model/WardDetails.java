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
@Table(name = "WARDDETAILS")
public class WardDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long Id;

	@Column(name = "WARDMASTERID")
	private Long  wardMasterId;
	
//	@Column(name = "WARDMASTERID")
//	private Long  wardMasterId;
	
	@Column(name = "IMPLANTCODE")
	private String  wardCode;
	
	@Column(name = "HOSPITALCATEGORYID")
	private String hospitalCategoryId;
	
	@Column(name = "PACK_WARD_AMOUNT")
	private String packWardAmount;
	
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

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getWardMasterId() {
		return wardMasterId;
	}

	public void setWardMasterId(Long wardMasterId) {
		this.wardMasterId = wardMasterId;
	}

	public String getWardCode() {
		return wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public String getHospitalCategoryId() {
		return hospitalCategoryId;
	}

	public void setHospitalCategoryId(String hospitalCategoryId) {
		this.hospitalCategoryId = hospitalCategoryId;
	}

	public String getPackWardAmount() {
		return packWardAmount;
	}

	public void setPackWardAmount(String packWardAmount) {
		this.packWardAmount = packWardAmount;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
