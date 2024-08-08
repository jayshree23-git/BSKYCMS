package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author ronauk
 *
 */

@Entity
@Table(name = "USER_DETAILS_PROFILE_LOG")
public class UserDetailsProfileLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "LOG_ID")
	private Integer logId;

	@Column(name = "BSKYUSERID")
	private Integer bskyUserId;

	@Column(name = "UESRNAME")
	private String userName;

	@Column(name = "MOBILENO")
	private Long mobileNo;

	@Column(name = "EMAILID")
	private String emailId;

	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "STATE_CODE")
	private String stateCode;

	@Column(name = "DISTRICTCODE")
	private String districtCode;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "STATUS_FLAG")
	private Integer status;

	@Column(name = "GROUP_TYPE_ID")
	private Integer groupId;

	@Column(name = "ADDRESS_INFO")
	private String address;

	@Column(name = "CREATED_BY")
	private Integer createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getBskyUserId() {
		return bskyUserId;
	}

	public void setBskyUserId(Integer bskyUserId) {
		this.bskyUserId = bskyUserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(Long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	@Override
	public String toString() {
		return "UserDetailsProfileLog [logId=" + logId + ", bskyUserId=" + bskyUserId + ", userName=" + userName
				+ ", mobileNo=" + mobileNo + ", emailId=" + emailId + ", userId=" + userId + ", stateCode=" + stateCode
				+ ", districtCode=" + districtCode + ", fullName=" + fullName + ", status=" + status + ", groupId="
				+ groupId + ", address=" + address + ", createdBy=" + createdBy + ", createdOn=" + createdOn + "]";
	}

}
