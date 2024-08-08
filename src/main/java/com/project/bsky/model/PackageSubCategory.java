package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain=true)
@ToString
@Table(name = "MSTPACKAGESUBCATEGORY")
public class PackageSubCategory implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PACKAGESUBCATAGORYID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long subcategoryId;
	
	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "PACKAGEHEADERCODE")
	private String packageheadercode;
	
	@Column(name = "PACKAGESUBHEADER")
	private String subcategoryName; 
	
	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "SUBPACKAGENAME")
	private String packagesubcategoryname;
	
	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "PACKAGESUBCODE")
	private String packagesubcategorycode; 
	
	
	@Column(name = "CREATEDBY")
	private Integer createdBy;

	@CreationTimestamp
	@Column(name = "CREATEDON")
	private Date createdOn;

	@Column(name = "UPDATEDBY")
	private Long updatedBy;

	@Column(name = "UPDATEDON")
	private Date updatedOn;
	
	@Column(name = "DELETEDFLAG")
	private Integer deletedFlag;

	public Long getSubcategoryId() {
		return subcategoryId;
	}

	public void setSubcategoryId(Long subcategoryId) {
		this.subcategoryId = subcategoryId;
	}

	public String getPackageheadercode() {
		return packageheadercode;
	}

	public void setPackageheadercode(String packageheadercode) {
		this.packageheadercode = packageheadercode;
	}

	public String getPackagesubcategorycode() {
		return packagesubcategorycode;
	}

	public void setPackagesubcategorycode(String packagesubcategorycode) {
		this.packagesubcategorycode = packagesubcategorycode;
	}

	public String getPackagesubcategoryname() {
		return packagesubcategoryname;
	}

	public void setPackagesubcategoryname(String packagesubcategoryname) {
		this.packagesubcategoryname = packagesubcategoryname;
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

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
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
