
package com.project.bsky.model;




import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author priyanka.singh
 *
 */

@Entity
@Table(name = "M_HOSPITALAUTHTAGGING")
public class HospitalAuthTagging  implements Serializable{

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "AUTHTAGGING_ID")
	private Integer authTaggingId;

	@Column(name = "GROUPID")
	private Integer groupId;
	
	@Column(name = "USERID")
	private Integer userId;
	
	@Column(name = "HOSPITALCODE")
	private String tagHospitalCode;
	
	@Column(name = "CREATEDBY")
	private Integer createdBy;
	
	@Column(name = "CREATEDON")
	private Timestamp createdOn;

	@Column(name = "STATUSFLAG")
	private Integer status;
	
	@Column(name = "UPDATEDBY")
	private Integer updatedBy;
	
	@Column(name = "UPDATEDON")
	private Date updatedOn;

	public Integer getAuthTaggingId() {
		return authTaggingId;
	}

	public void setAuthTaggingId(Integer authTaggingId) {
		this.authTaggingId = authTaggingId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTagHospitalCode() {
		return tagHospitalCode;
	}

	public void setTagHospitalCode(String tagHospitalCode) {
		this.tagHospitalCode = tagHospitalCode;
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

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Override
	public String toString() {
		return "HospitalAuthTagging [authTaggingId=" + authTaggingId + ", groupId=" + groupId + ", userId=" + userId
				+ ", tagHospitalCode=" + tagHospitalCode + ", createdBy=" + createdBy + ", createdOn=" + createdOn
				+ ", status=" + status + ", updatedBy=" + updatedBy + ", updatedOn=" + updatedOn + "]";
	}



	
}
