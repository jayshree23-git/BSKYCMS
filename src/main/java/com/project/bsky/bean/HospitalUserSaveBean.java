package com.project.bsky.bean;

public class HospitalUserSaveBean {

	private Long id;
	private String hospitalId;
	private String hospitalName;
	private String hospitalCode;
	private String stateId;
	private String districtId;
	private String cpdApprovalRequired;
	private String stateCode;
	private String districtcode;
	private int status;
	private String mobile;
	private String emailId;
	private Integer userId;
	private Integer createdBy;
	private Integer updatedBy;
	private Long assignedDc;
	private Long snoUserId;
	private Integer snoMappingId;
	private String latitude;
	private String longitude;
	private Integer hospitalType;
	private int tmsActiveStat;

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
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

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getCpdApprovalRequired() {
		return cpdApprovalRequired;
	}

	public void setCpdApprovalRequired(String cpdApprovalRequired) {
		this.cpdApprovalRequired = cpdApprovalRequired;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getAssignedDc() {
		return assignedDc;
	}

	public void setAssignedDc(Long assignedDc) {
		this.assignedDc = assignedDc;
	}

	public Long getSnoUserId() {
		return snoUserId;
	}

	public void setSnoUserId(Long snoUserId) {
		this.snoUserId = snoUserId;
	}

	public Integer getSnoMappingId() {
		return snoMappingId;
	}

	public void setSnoMappingId(Integer snoMappingId) {
		this.snoMappingId = snoMappingId;
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

	public Integer getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(Integer hospitalType) {
		this.hospitalType = hospitalType;
	}

	public int getTmsActiveStat() {
		return tmsActiveStat;
	}

	public void setTmsActiveStat(int tmsActiveStat) {
		this.tmsActiveStat = tmsActiveStat;
	}

	@Override
	public String toString() {
		return "HospitalUserSaveBean [id=" + id + ", hospitalId=" + hospitalId + ", hospitalName=" + hospitalName
				+ ", hospitalCode=" + hospitalCode + ", stateId=" + stateId + ", districtId=" + districtId
				+ ", cpdApprovalRequired=" + cpdApprovalRequired + ", stateCode=" + stateCode + ", districtcode="
				+ districtcode + ", status=" + status + ", mobile=" + mobile + ", emailId=" + emailId + ", userId="
				+ userId + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", assignedDc=" + assignedDc
				+ ", snoUserId=" + snoUserId + ", snoMappingId=" + snoMappingId + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", hospitalType=" + hospitalType + ", tmsActiveStat=" + tmsActiveStat
				+ "]";
	}

}
