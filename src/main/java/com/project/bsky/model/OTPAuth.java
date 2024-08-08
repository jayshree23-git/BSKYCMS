package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "USER_OTP_AUTH")
public class OTPAuth implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
//	@GenericGenerator(name = "catInc", strategy = "increment")
//	@GeneratedValue(generator = "catInc") //for oracle sequence generate
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_OTP_AUTH_AUTH_ID_SEQ")
	@SequenceGenerator(name = "USER_OTP_AUTH_AUTH_ID_SEQ", sequenceName = "USER_OTP_AUTH_AUTH_ID_SEQ", allocationSize = 1)
	@Column(name = "AUTH_ID")
	private Long authId;

	@Column(name = "USERNAME")
	private String userName;

	@Column(name = "MOBILE")
	private String mobileNo;

	@Column(name = "OTP")
	private String otp;

	@CreationTimestamp
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "OTP_VERIFY")
	private Integer verifyStatus;
	
	@Column(name = "ATTEMPT_STATUS")
	private Integer attemptStatus;
	

	public Integer getAttemptStatus() {
		return attemptStatus;
	}

	public void setAttemptStatus(Integer attemptStatus) {
		this.attemptStatus = attemptStatus;
	}

	public Long getAuthId() {
		return authId;
	}

	public void setAuthId(Long authId) {
		this.authId = authId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Integer getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(Integer verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
