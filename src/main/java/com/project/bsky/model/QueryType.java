package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name ="M_QUERY_TYPE")
public class QueryType implements Serializable {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "TYPE_ID")
	private Long typeId;
	
	@Column(name = "TYPE_NAME")
	private String typeName;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "STATUS_FLAG")
	private Long statusflag;
	
	//@Column(name = "CREATED_BY")
	//private Long createdBy;
	
	@ManyToOne
	@JoinColumn(name = "CREATED_BY")
	private UserDetails createdBy;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	@ManyToOne
	@JoinColumn(name = "UPDATED_BY")
	private UserDetails updatedBy;
	
	
	@Column(name = "UPDATED_ON")
	private Date updatedOn;


	public Long getTypeId() {
		return typeId;
	}


	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}


	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Long getStatusflag() {
		return statusflag;
	}


	public void setStatusflag(Long statusflag) {
		this.statusflag = statusflag;
	}


	public UserDetails getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(UserDetails createdBy) {
		this.createdBy = createdBy;
	}


	public Date getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}


	public UserDetails getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(UserDetails updatedBy) {
		this.updatedBy = updatedBy;
	}


	public Date getUpdatedOn() {
		return updatedOn;
	}


	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}


	@Override
	public String toString() {
		return "QueryType [typeId=" + typeId + ", typeName=" + typeName + ", remarks=" + remarks + ", statusflag="
				+ statusflag + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", updatedBy=" + updatedBy
				+ ", updatedOn=" + updatedOn + "]";
	}


	

	
	
}
