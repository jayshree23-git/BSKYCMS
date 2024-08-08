package com.project.bsky.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "m_snaexecutivemapping")
public class SNAExecutive {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "executivemapping_id")
	private Integer ExecutivemappingId;
	
	@Column(name = "groupid")
	private Integer groupId;
	
	@Column(name = "snauserid")
	private Integer snauserid;
	
	@Column(name = "snaexecutive_id")
	private Integer snaexecutiveId;
	
	@Column(name = "createdby")
	private Integer createdBy;
	
	@Column(name = "createdon")
	private Timestamp createdOn;

	@Column(name = "statusflag")
	private Integer status;
	
	@Column(name = "updatedby")
	private Integer updatedBy;
	
	@Column(name = "updatedon")
	private Timestamp updatedOn;

	public Integer getExecutivemappingId() {
		return ExecutivemappingId;
	}

	public void setExecutivemappingId(Integer executivemappingId) {
		ExecutivemappingId = executivemappingId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getSnauserid() {
		return snauserid;
	}

	public void setSnauserid(Integer snauserid) {
		this.snauserid = snauserid;
	}

	public Integer getSnaexecutiveId() {
		return snaexecutiveId;
	}

	public void setSnaexecutiveId(Integer snaexecutiveId) {
		this.snaexecutiveId = snaexecutiveId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Override
	public String toString() {
		return "SNAExecutive [ExecutivemappingId=" + ExecutivemappingId + ", groupId=" + groupId + ", snauserid="
				+ snauserid + ", snaexecutiveId=" + snaexecutiveId + ", createdBy=" + createdBy + ", createdOn="
				+ createdOn + ", status=" + status + ", updatedBy=" + updatedBy + ", updatedOn=" + updatedOn + "]";
	}
	
	
}
