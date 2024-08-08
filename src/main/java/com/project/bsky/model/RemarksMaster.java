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


@Entity
@Data
@Table(name = "ACTIONREMARK")

public class RemarksMaster implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "REMARKID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	private Long remarkId;

	
	@Column(name = "REMARK")
	private String  remark;
	
	@Column(name = "DESCRIPTION")
	private String  description;
	
	@Column(name = "CREATEDON")
	private Date  createdOn;
	
	@Column(name = "CREATEDBY")
	private Integer  createdBy;
	
	@Column(name = "UPDATEDON")
	private Date  updatedOn;
	
	@Column(name = "UPDATEDBY")
	private Integer  updatedBy;
	
	@Column(name = "DELETEDFLAG")
	private Integer deletedFlag;

	public Long getRemarkId() {
		return remarkId;
	}

	public void setRemarkId(Long remarkId) {
		this.remarkId = remarkId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
