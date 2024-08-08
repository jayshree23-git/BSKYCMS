package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Table(name = "MSTPACKAGEHEADER")
public class PackageHeader implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long headerId;

	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "PACKAGEHEADER")
	private String  packageheadername;
	
	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "PACKAGEHEADERCODE")
	private String packageheadercode;
	
	@Column(name = "CREATEDBY")
	private Integer createdBy;

	@CreationTimestamp
	@Column(name = "CREATEON")
	private Date createdOn;

	@Column(name = "UPDATEDBY")
	private Long updatedBy;

	@Column(name = "UPDATEDON")
	private Date updatedOn;
	
	@Column(name = "DELETEDFLAG")
	private Integer deletedFlag;
	
	@Transient
	private String  headername;

	public Long getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Long headerId) {
		this.headerId = headerId;
	}

	public String getPackageheadername() {
		return  packageheadername;
	}

	public void setPackageheadername(String  packageheadername) {
		 this.packageheadername =  packageheadername;
	}

	public String getPackageheadercode() {
		return packageheadercode;
	}

	public void setPackageheadercode(String packageheadercode) {
		this.packageheadercode = packageheadercode;
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

//	public Integer getBitStatus() {
//		return bitStatus;
//	}
//
//	public void setBitStatus(Integer bitStatus) {
//		this.bitStatus = bitStatus;
//	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
