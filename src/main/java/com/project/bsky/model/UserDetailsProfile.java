package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author ronauk
 *
 */
@Entity
@Table(name = "USER_DETAILS_PROFILE")
public class UserDetailsProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "BSKYUSERID")
	private Integer bskyUserId;

	@Column(name = "UESRNAME")
	private String userName;

	@Column(name = "MOBILENO")
	private Long mobileNo;

	@Column(name = "EMAILID")
	private String emailId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	private UserDetails userId;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "DISTRICTCODE", referencedColumnName = "DISTRICTCODE"),
			@JoinColumn(name = "STATE_CODE", referencedColumnName = "STATECODE") })
	private DistrictMaster districtCode;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "STATUS_FLAG")
	private Integer status;

	@ManyToOne
	@JoinColumn(name = "GROUP_TYPE_ID", referencedColumnName = "TYPE_ID")
	private GroupTypeDetails groupId;

	@Column(name = "ADDRESS_INFO")
	private String address;

	@Column(name = "CREATED_BY")
	private Long createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "UPDATED_BY")
	private Long updatedBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;
	
	@Column(name= "DATE_OF_JOINING")
	private Date dateofjoin;

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

	public UserDetails getUserId() {
		return userId;
	}

	public void setUserId(UserDetails userId) {
		this.userId = userId;
	}

	public DistrictMaster getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(DistrictMaster districtCode) {
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

	public GroupTypeDetails getGroupId() {
		return groupId;
	}

	public void setGroupId(GroupTypeDetails groupId) {
		this.groupId = groupId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
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

	public Date getDateofjoin() {
		return dateofjoin;
	}

	public void setDateofjoin(Date dateofjoin) {
		this.dateofjoin = dateofjoin;
	}

	@Override
	public String toString() {
		return "UserDetailsProfile [bskyUserId=" + bskyUserId + ", userName=" + userName + ", mobileNo=" + mobileNo
				+ ", emailId=" + emailId + ", userId=" + userId + ", districtCode=" + districtCode + ", fullName="
				+ fullName + ", status=" + status + ", groupId=" + groupId + ", address=" + address + ", createdBy="
				+ createdBy + ", createdOn=" + createdOn + ", updatedBy=" + updatedBy + ", updatedOn=" + updatedOn
				+ ", dateofjoin=" + dateofjoin + "]";
	}

	

}
