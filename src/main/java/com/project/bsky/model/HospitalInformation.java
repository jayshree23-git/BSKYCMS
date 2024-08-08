/**
 * 
 */
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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author ronauk
 *
 */
@Entity
@Data
@Table(name = "HOSPITAL_INFO")
public class HospitalInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "HOSPITAL_ID")
	private Integer hospitalId;

	@Column(name = "HOSPITAL_NAME")
	private String hospitalName;

	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "DISTRICT_CODE", referencedColumnName = "DISTRICTCODE"),
			@JoinColumn(name = "STATE_CODE", referencedColumnName = "STATECODE") })
	private DistrictMaster districtcode;

	@Column(name = "STATUS_FLAG")
	private Integer status;

	@Column(name = "MOBILE")
	private String mobile;

	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "CPD_APPROVAL_REQUIRED")
	private Integer cpdApprovalRequired;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID")
	private UserDetails userId;

	@Column(name = "CREATED_BY")
	private Long createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "UPDATED_BY")
	private Long updatedBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "ASSIGNED_DC")
	private Long assigned_dc;

	@Column(name = "LATITUDE")
	private String latitude;

	@Column(name = "LONGITUDE")
	private String longitude;

	@Column(name = "HOSPITAL_CATEGORYID")
	private Integer hospitalCategoryid;
	
	@Column(name = "BACKDATE_ADMISSION_DAYS")
	private Integer backdateadmissiondate;
	
	@Column(name = "BACKDATE_DISCHARGE_DAYS")
	private Integer backdatedischargedate;
	
	@Column(name = "PATIENT_OTP_REQUIRED")
	private Integer patientOtpRequired;
	
	@Column(name = "LOGIN_OTP_REQUIRED")
	private Integer loginOtpRequired;

	@Transient
	private String state;

	@Transient
	private String dist;
	
	
//	new column
	
	@Column(name = "HC_VALID_FROM_DATE")
	private Date hcValidFromDate;
	
	@Transient
	private String hosCValidDateFrom;
	
	@Column(name = "HC_VALID_TO_DATE")
	private Date hcValidToDate;
	
	@Transient
	private String hosCValidDateTo;
	
	@Column(name = "MOU")
	private String mou;
	
	@Column(name = "MOU_START_DATE")
	private Date mouStartDate;
	
	@Transient
	private String mouStartDt;
	
	@Column(name = "MOU_END_DATE")
	private Date mouEndDate;
	
	@Transient
	private String mouEndDt;
	
	@Column(name = "MOU_STATUS")
	private Integer mouStatus;
	
	@Column(name = "EMPANELMENTSTATUS_FLAG")
	private Integer empanelmentstatus;
	
	@Column(name = "MOU_DOC_UPLOAD")
	private String mouDocUpload;
	
	@Column(name = "IS_BLOCK_ACTIVE")
	private Integer isBlockActive;
	
	@Column(name = "EXCEPTIONHOSPITAL")
	private Integer preauthapprovalrequired;
	
	
	
	
	public String getMouDocUpload() {
		return mouDocUpload;
	}

	public void setMouDocUpload(String mouDocUpload) {
		this.mouDocUpload = mouDocUpload;
	}

	public Integer getEmpanelmentstatus() {
		return empanelmentstatus;
	}

	public void setEmpanelmentstatus(Integer empanelmentstatus) {
		this.empanelmentstatus = empanelmentstatus;
	}

	public Integer getMouStatus() {
		return mouStatus;
	}

	public void setMouStatus(Integer mouStatus) {
		this.mouStatus = mouStatus;
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

	public DistrictMaster getDistrictcode() {
		return districtcode;
	}

	public void setDistrictcode(DistrictMaster districtcode) {
		this.districtcode = districtcode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public UserDetails getUserId() {
		return userId;
	}

	public void setUserId(UserDetails userId) {
		this.userId = userId;
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

	public Long getAssigned_dc() {
		return assigned_dc;
	}

	public void setAssigned_dc(Long assigned_dc) {
		this.assigned_dc = assigned_dc;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		this.dist = dist;
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
	
	public String getHosCValidDateFrom() {
		return hosCValidDateFrom;
	}

	public void setHosCValidDateFrom(String hosCValidDateFrom) {
		this.hosCValidDateFrom = hosCValidDateFrom;
	}

	public String getHosCValidDateTo() {
		return hosCValidDateTo;
	}

	public void setHosCValidDateTo(String hosCValidDateTo) {
		this.hosCValidDateTo = hosCValidDateTo;
	}

	public String getMouStartDt() {
		return mouStartDt;
	}

	public void setMouStartDt(String mouStartDt) {
		this.mouStartDt = mouStartDt;
	}

	public String getMouEndDt() {
		return mouEndDt;
	}

	public void setMouEndDt(String mouEndDt) {
		this.mouEndDt = mouEndDt;
	}
	
	

	@Override
	public String toString() {
		return "HospitalInformation [hospitalId=" + hospitalId + ", hospitalName=" + hospitalName + ", hospitalCode="
				+ hospitalCode + ", districtcode=" + districtcode + ", status=" + status + ", mobile=" + mobile
				+ ", emailId=" + emailId + ", cpdApprovalRequired=" + cpdApprovalRequired + ", userId=" + userId
				+ ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", updatedBy=" + updatedBy + ", updatedOn="
				+ updatedOn + ", assigned_dc=" + assigned_dc + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", hospitalCategoryid=" + hospitalCategoryid + ", backdateadmissiondate=" + backdateadmissiondate
				+ ", backdatedischargedate=" + backdatedischargedate + ", state=" + state + ", dist=" + dist
				+ ", hcValidFromDate=" + hcValidFromDate + ", hosCValidDateFrom=" + hosCValidDateFrom
				+ ", hcValidToDate=" + hcValidToDate + ", hosCValidDateTo=" + hosCValidDateTo + ", mou=" + mou
				+ ", mouStartDate=" + mouStartDate + ", mouStartDt=" + mouStartDt + ", mouEndDate=" + mouEndDate
				+ ", mouEndDt=" + mouEndDt + ", mouStatus=" + mouStatus + ", empanelmentstatus=" + empanelmentstatus
				+ ", mouDocUpload=" + mouDocUpload + ", isBlockActive=" + isBlockActive + "]";
	}
	
	
	


	


}
