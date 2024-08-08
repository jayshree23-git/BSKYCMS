package com.project.bsky.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name ="BSKYGROUP")
public class Group implements Serializable {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name="GROUPID")
	private Integer groupId;
	
	@Column(name="GROUPNAME")
	private String groupName;
	
	@Column(name="CREATEDBY")
	private Integer createdBy;
	
	@Column(name="CREATEDDATE")
	private Date createdDate;
	
	@Column(name="LASTUPDATEDATE")
	private Date lastupdateDate;
	
	@Column(name="LASTUPDATEDBY")
	private Integer lastupdateBy;
	
	@Column(name="ISACTIVE")
	private Integer isActive;

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastupdateDate() {
		return lastupdateDate;
	}

	public void setLastupdateDate(Date lastupdateDate) {
		this.lastupdateDate = lastupdateDate;
	}

	public Integer getLastupdateBy() {
		return lastupdateBy;
	}

	public void setLastupdateBy(Integer lastupdateBy) {
		this.lastupdateBy = lastupdateBy;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", groupName=" + groupName + ", createdBy=" + createdBy + ", createdDate="
				+ createdDate + ", lastupdateDate=" + lastupdateDate + ", lastupdateBy=" + lastupdateBy + ", isActive="
				+ isActive + "]";
	}

	
	
	
	

}
