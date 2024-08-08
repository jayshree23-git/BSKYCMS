package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "USER_MENU_MAPPING")
public class UserMenuMapping implements Serializable {

	private static final long serialVersionUID = 1L;
	
//	@Id
//	@Column(name = "MAPPING_ID")
//	private Integer userMappingId;
	
	/*
	 * Modified by Suprava Parida
	 * Sequence Add*/
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_menu_mapping_MAPPING_ID_SEQ")
	@SequenceGenerator(name = "user_menu_mapping_MAPPING_ID_SEQ", sequenceName = "user_menu_mapping_MAPPING_ID_SEQ", allocationSize = 1)
	@Column(name = "MAPPING_ID")
	private Integer userMappingId;
	
	
	@Column(name = "USER_ID")
	private Integer userId;

	@Column(name = "CREATED_BY")
	private Integer createdBy;

	@CreationTimestamp
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "UPDATED_BY")
	private Integer updatedBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "STATUS_FLAG")
	private Integer bitStatus;

	@ManyToOne
	@JoinColumn(name = "PRIMARY_LINK_ID")
	private PrimaryLink primaryLink;

	@Column(name = "GLOBAL_LINK_ID")
	private Integer global_Link_Id;

	public UserMenuMapping(Integer userMappingId, Integer userId, Integer createdBy, Date createdOn, Integer updatedBy,
			Date updatedOn, Integer bitStatus, PrimaryLink primaryLink, Integer global_Link_Id) {
		super();
		this.userMappingId = userMappingId;
		this.userId = userId;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
		this.bitStatus = bitStatus;
		this.primaryLink = primaryLink;
		this.global_Link_Id = global_Link_Id;
	}

	public UserMenuMapping() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getUserMappingId() {
		return userMappingId;
	}

	public void setUserMappingId(Integer userMappingId) {
		this.userMappingId = userMappingId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public Integer getBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(Integer bitStatus) {
		this.bitStatus = bitStatus;
	}

	public PrimaryLink getPrimaryLink() {
		return primaryLink;
	}

	public void setPrimaryLink(PrimaryLink primaryLink) {
		this.primaryLink = primaryLink;
	}

	public Integer getGlobal_Link_Id() {
		return global_Link_Id;
	}

	public void setGlobal_Link_Id(Integer global_Link_Id) {
		this.global_Link_Id = global_Link_Id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "UserMenuMapping [userMappingId=" + userMappingId + ", userId=" + userId + ", createdBy=" + createdBy
				+ ", createdOn=" + createdOn + ", updatedBy=" + updatedBy + ", updatedOn=" + updatedOn + ", bitStatus="
				+ bitStatus + ", primaryLink=" + primaryLink + ", global_Link_Id=" + global_Link_Id + "]";
	}

}
