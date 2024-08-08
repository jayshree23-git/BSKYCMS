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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USERDETAILS")
public class UserDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "USERID")
	private Long userId;

	@Column(name = "USERNAME")
	private String UserName;

	@Column(name = "PASSWORD")
	@JsonIgnore
	private String password;

	@ManyToOne
	@JoinColumn(name = "GROUPID", referencedColumnName = "TYPE_ID")
	private GroupTypeDetails GroupId;

	@Column(name = "PHONE")
	private Long Phone;

	@Column(name = "CREATEDATETIME")
	private Date CreateDateTime;

	@Column(name = "EMAIL")
	private String Email;

	@Column(name = "COMPANYCODE")
	private String CompanyCode;

	@Column(name = "TPACODE")
	private String TPACode;

	@Column(name = "CREATEDUSERNAME")
	private String CreatedUserName;

	@Column(name = "ISACTIVE")
	private Integer IsActive;

	@Column(name = "STATUS_FLAG")
	private Integer status;

	@Column(name = "FULL_NAME")
	private String fullname;

	@Column(name = "NON_UPLOAD_BTN_FLG")
	private Integer nonUploadBtnFlg;

	@Column(name = "NON_COMP_BTN_FLG")
	private Integer nonCompBtnFlg;

	@Column(name = "BTN_VISIBLE_BY")
	private Date btnVisibleBy;

	@Column(name = "ATTEMPTED_STATUS")
	private int attemptedStatus;

	@Column(name = "TMS_LOGIN_STATUS")
	private Integer tmsLoginStatus;

	@Column(name = "PROFILE_PHOTO")
	private String profilePhoto;

	@Column(name = "PASSWORD_UPDATED_ON")
	private Date passwordUpdatedOn;

	@Column(name = "IS_OTP_ALLOWED")
	private Integer isOtpAllowed;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public GroupTypeDetails getGroupId() {
		return GroupId;
	}

	public void setGroupId(GroupTypeDetails groupId) {
		GroupId = groupId;
	}

	public Long getPhone() {
		return Phone;
	}

	public void setPhone(Long phone) {
		Phone = phone;
	}

	public Date getCreateDateTime() {
		return CreateDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		CreateDateTime = createDateTime;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getCompanyCode() {
		return CompanyCode;
	}

	public void setCompanyCode(String companyCode) {
		CompanyCode = companyCode;
	}

	public String getTPACode() {
		return TPACode;
	}

	public void setTPACode(String TPACode) {
		this.TPACode = TPACode;
	}

	public String getCreatedUserName() {
		return CreatedUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		CreatedUserName = createdUserName;
	}

	public Integer getIsActive() {
		return IsActive;
	}

	public void setIsActive(Integer isActive) {
		IsActive = isActive;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getNonUploadBtnFlg() {
		return nonUploadBtnFlg;
	}

	public void setNonUploadBtnFlg(Integer nonUploadBtnFlg) {
		this.nonUploadBtnFlg = nonUploadBtnFlg;
	}

	public Integer getNonCompBtnFlg() {
		return nonCompBtnFlg;
	}

	public void setNonCompBtnFlg(Integer nonCompBtnFlg) {
		this.nonCompBtnFlg = nonCompBtnFlg;
	}

	public Date getBtnVisibleBy() {
		return btnVisibleBy;
	}

	public void setBtnVisibleBy(Date btnVisibleBy) {
		this.btnVisibleBy = btnVisibleBy;
	}

	public int getAttemptedStatus() {
		return attemptedStatus;
	}

	public void setAttemptedStatus(int attemptedStatus) {
		this.attemptedStatus = attemptedStatus;
	}

	public Integer getTmsLoginStatus() {
		return tmsLoginStatus;
	}

	public void setTmsLoginStatus(Integer tmsLoginStatus) {
		this.tmsLoginStatus = tmsLoginStatus;
	}

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public Date getPasswordUpdatedOn() {
		return passwordUpdatedOn;
	}

	public void setPasswordUpdatedOn(Date passwordUpdatedOn) {
		this.passwordUpdatedOn = passwordUpdatedOn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getIsOtpAllowed() {
		return isOtpAllowed;
	}

	public void setIsOtpAllowed(Integer isOtpAllowed) {
		this.isOtpAllowed = isOtpAllowed;
	}

	@Override
	public String toString() {
		return "UserDetails [userId=" + userId + ", UserName=" + UserName + ", password=" + password + ", GroupId="
				+ GroupId + ", Phone=" + Phone + ", CreateDateTime=" + CreateDateTime + ", Email=" + Email
				+ ", CompanyCode=" + CompanyCode + ", TPACode=" + TPACode + ", CreatedUserName=" + CreatedUserName
				+ ", IsActive=" + IsActive + ", status=" + status + ", fullname=" + fullname + ", nonUploadBtnFlg="
				+ nonUploadBtnFlg + ", nonCompBtnFlg=" + nonCompBtnFlg + ", btnVisibleBy=" + btnVisibleBy
				+ ", attemptedStatus=" + attemptedStatus + ", tmsLoginStatus=" + tmsLoginStatus + ", profilePhoto="
				+ profilePhoto + ", passwordUpdatedOn=" + passwordUpdatedOn + ", isOtpAllowed=" + isOtpAllowed + "]";
	}

}
