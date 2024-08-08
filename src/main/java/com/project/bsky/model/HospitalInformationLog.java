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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author ronauk
 *
 */
@Data
@Entity
@Table(name = "HOSPITL_INFO_LOG")
public class HospitalInformationLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
//	@GenericGenerator(name = "catInc", strategy = "increment")
//	@GeneratedValue(generator = "catInc")
	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HOSPITL_INFO_LOG_SEQ")
	@SequenceGenerator(name = "HOSPITL_INFO_LOG_SEQ", sequenceName = "HOSPITL_INFO_LOG_SEQ", allocationSize = 1)
	@Column(name = "LOG_ID")
	private Integer logId;

	@Column(name = "HOSPITAL_ID")
	private Integer hospitalId;

	@Column(name = "HOSPITAL_NAME")
	private String hospitalName;

	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;

	@Column(name = "STATE_ID")
	private Integer stateId;

	@Column(name = "DISTRICT_ID")
	private Integer districtId;

	@Column(name = "STATUS_FLAG")
	private Integer status;

	@Column(name = "STATE_CODE")
	private String stateCode;

	@Column(name = "DISTRICT_CODE")
	private String districtcode;

	@Column(name = "MOBILE")
	private String mobile;

	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "CPD_APPROVAL_REQUIRED")
	private Integer cpdApprovalRequired;

	@Column(name = "USER_ID")
	private Integer userId;

	@Column(name = "CREATED_BY")
	private Integer createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "ASSIGNED_DC")
	private Long assigned_dc;

	@Column(name = "LATITUDE")
	private String latitude;

	@Column(name = "LONGITUDE")
	private String longitude;

	@Column(name = "HOSPITAL_CATEGORYID")
	private Integer hospitalCategoryid;
	
	
//	new column
	
	@Column(name = "HC_VALID_FROM_DATE")
	private Date hcValidFromDate;
	
	@Column(name = "HC_VALID_TO_DATE")
	private Date hcValidToDate;
	
	@Column(name = "MOU")
	private String mou;
	
	@Column(name = "MOU_START_DATE")
	private Date mouStartDate;
	
	@Column(name = "MOU_END_DATE")
	private Date mouEndDate;
	
	
	@Column(name = "MOU_STATUS")
	private Integer mouStatus;
	
	@Column(name = "EMPANELMENTSTATUS_FLAG")
	private Integer empanelmentstatus;
	
	@Column(name = "MOU_DOC_UPLOAD")
	private String mouDocUpload;
	
	@Column(name = "IS_BLOCK_ACTIVE")
	private Integer isBlockActive;
	
	@Column(name = "PATIENT_OTP_REQUIRED")
	private Integer patientOtpRequired;
	
	@Column(name = "LOGIN_OTP_REQUIRED")
	private Integer loginOtpRequired;
	
	@Transient
	private String dcname;
	
	@Transient
	private String typename;
	
	@Transient
	private String createname;
	
	@Column(name = "EXCEPTIONHOSPITAL")
	private Integer preauthapprovalrequired;

	public Date getHcValidFromDate() {
		return hcValidFromDate;
	}

	public void setHcValidFromDate(Date hcValidFromDate) {
		this.hcValidFromDate = hcValidFromDate;
	}

	public Date getHcValidToDate() {
		return hcValidToDate;
	}

	public void setHcValidToDate(Date hcValidToDate) {
		this.hcValidToDate = hcValidToDate;
	}

	public String getMou() {
		return mou;
	}

	public void setMou(String mou) {
		this.mou = mou;
	}

	public Date getMouStartDate() {
		return mouStartDate;
	}

	public void setMouStartDate(Date mouStartDate) {
		this.mouStartDate = mouStartDate;
	}

	public Date getMouEndDate() {
		return mouEndDate;
	}

	public void setMouEndDate(Date mouEndDate) {
		this.mouEndDate = mouEndDate;
	}

	public Integer getMouStatus() {
		return mouStatus;
	}

	public void setMouStatus(Integer mouStatus) {
		this.mouStatus = mouStatus;
	}

	public Integer getEmpanelmentstatus() {
		return empanelmentstatus;
	}

	public void setEmpanelmentstatus(Integer empanelmentstatus) {
		this.empanelmentstatus = empanelmentstatus;
	}

	public String getMouDocUpload() {
		return mouDocUpload;
	}

	public void setMouDocUpload(String mouDocUpload) {
		this.mouDocUpload = mouDocUpload;
	}

	public Integer getIsBlockActive() {
		return isBlockActive;
	}

	public void setIsBlockActive(Integer isBlockActive) {
		this.isBlockActive = isBlockActive;
	}

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getDistrictcode() {
		return districtcode;
	}

	public void setDistrictcode(String districtcode) {
		this.districtcode = districtcode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Integer getCpdApprovalRequired() {
		return cpdApprovalRequired;
	}

	public void setCpdApprovalRequired(Integer cpdApprovalRequired) {
		this.cpdApprovalRequired = cpdApprovalRequired;
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

	public Long getAssigned_dc() {
		return assigned_dc;
	}

	public void setAssigned_dc(Long assigned_dc) {
		this.assigned_dc = assigned_dc;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Integer getHospitalCategoryid() {
		return hospitalCategoryid;
	}

	public void setHospitalCategoryid(Integer hospitalCategoryid) {
		this.hospitalCategoryid = hospitalCategoryid;
	}

	@Override
	public String toString() {
		return "HospitalInformationLog [logId=" + logId + ", hospitalId=" + hospitalId + ", hospitalName="
				+ hospitalName + ", hospitalCode=" + hospitalCode + ", stateId=" + stateId + ", districtId="
				+ districtId + ", status=" + status + ", stateCode=" + stateCode + ", districtcode=" + districtcode
				+ ", mobile=" + mobile + ", emailId=" + emailId + ", cpdApprovalRequired=" + cpdApprovalRequired
				+ ", userId=" + userId + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", assigned_dc="
				+ assigned_dc + ", latitude=" + latitude + ", longitude=" + longitude + ", hospitalCategoryid="
				+ hospitalCategoryid + ", hcValidFromDate=" + hcValidFromDate + ", hcValidToDate=" + hcValidToDate
				+ ", mou=" + mou + ", mouStartDate=" + mouStartDate + ", mouEndDate=" + mouEndDate + ", mouStatus="
				+ mouStatus + ", empanelmentstatus=" + empanelmentstatus + ", mouDocUpload=" + mouDocUpload
				+ ", isBlockActive=" + isBlockActive + "]";
	}

	

}
