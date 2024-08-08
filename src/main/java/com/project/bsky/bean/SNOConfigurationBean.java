package com.project.bsky.bean;

import java.util.List;

public class SNOConfigurationBean {

	private Integer snoId;
	private String stateCode;
	private String districtCode;
	private List<Hospital> hospitalCode;
	private String snoName;
	private String stateName;
	private String districtName;
	private String hospitalName;
	private Integer snoMappingId;
	private Integer status;
	private Integer SnoUserId;
	private String hospCode;
	private List<String> hospitalC;
	private List<HospBean> hospObj;
	private List<String> hospListForView;
	private Integer createdBy;
	private Integer updatedBy;
	private List<HospObj> hospList;

	public Integer getSnoId() {
		return snoId;
	}

	public void setSnoId(Integer snoId) {
		this.snoId = snoId;
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

	public List<Hospital> getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(List<Hospital> hospital) {
		this.hospitalCode = hospital;
	}

	public String getSnoName() {
		return snoName;
	}

	public void setSnoName(String snoName) {
		this.snoName = snoName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Integer getSnoMappingId() {
		return snoMappingId;
	}

	public void setSnoMappingId(Integer snoMappingId) {
		this.snoMappingId = snoMappingId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getHospCode() {
		return hospCode;
	}

	public void setHospCode(String hospCode) {
		this.hospCode = hospCode;
	}

	public Integer getSnoUserId() {
		return SnoUserId;
	}

	public void setSnoUserId(Integer snoUserId) {
		SnoUserId = snoUserId;
	}

	public List<String> getHospitalC() {
		return hospitalC;
	}

	public void setHospitalC(List<String> hospitalC) {
		this.hospitalC = hospitalC;
	}

	public List<HospBean> getHospObj() {
		return hospObj;
	}

	public void setHospObj(List<HospBean> hospObj) {
		this.hospObj = hospObj;
	}

	public List<String> getHospListForView() {
		return hospListForView;
	}

	public void setHospListForView(List<String> hospListForView) {
		this.hospListForView = hospListForView;
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

	public List<HospObj> getHospList() {
		return hospList;
	}

	public void setHospList(List<HospObj> hospList) {
		this.hospList = hospList;
	}

	@Override
	public String toString() {
		return "SNOConfigurationBean [snoId=" + snoId + ", stateCode=" + stateCode + ", districtCode=" + districtCode
				+ ", hospitalCode=" + hospitalCode + ", snoName=" + snoName + ", stateName=" + stateName
				+ ", districtName=" + districtName + ", hospitalName=" + hospitalName + ", snoMappingId=" + snoMappingId
				+ ", status=" + status + ", SnoUserId=" + SnoUserId + ", hospCode=" + hospCode + ", hospitalC="
				+ hospitalC + ", hospObj=" + hospObj + ", hospListForView=" + hospListForView + ", createdBy="
				+ createdBy + ", updatedBy=" + updatedBy + ", hospList=" + hospList + "]";
	}

}
