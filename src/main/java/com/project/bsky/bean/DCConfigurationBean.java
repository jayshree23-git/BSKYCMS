package com.project.bsky.bean;

import java.util.List;

public class DCConfigurationBean {

	private Long dcId;
	private String stateCode;
	private String districtCode;
	private List<Hospital> hospitalCode;
	private String dcName;
	private String stateName;
	private String districtName;
	private String hospitalName;
	private Integer dcUserId;
	private String hospCode;
	private List<String> hospitalC;
	private List<HospBean> hospObj;
	private List<String> hospListForView;
	private Integer createdBy;
	private Integer updatedBy;
	private Integer status;
	private String initHosp;
	private List<HospObj> hospList;

	public Long getDcId() {
		return dcId;
	}

	public void setDcId(Long dcId) {
		this.dcId = dcId;
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

	public void setHospitalCode(List<Hospital> hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getDcName() {
		return dcName;
	}

	public void setDcName(String dcName) {
		this.dcName = dcName;
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

	public String getHospCode() {
		return hospCode;
	}

	public void setHospCode(String hospCode) {
		this.hospCode = hospCode;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getInitHosp() {
		return initHosp;
	}

	public void setInitHosp(String initHosp) {
		this.initHosp = initHosp;
	}

	public Integer getDcUserId() {
		return dcUserId;
	}

	public void setDcUserId(Integer dcUserId) {
		this.dcUserId = dcUserId;
	}

	public List<HospObj> getHospList() {
		return hospList;
	}

	public void setHospList(List<HospObj> hospList) {
		this.hospList = hospList;
	}

	@Override
	public String toString() {
		return "DCConfigurationBean [dcId=" + dcId + ", stateCode=" + stateCode + ", districtCode=" + districtCode
				+ ", hospitalCode=" + hospitalCode + ", dcName=" + dcName + ", stateName=" + stateName
				+ ", districtName=" + districtName + ", hospitalName=" + hospitalName + ", dcUserId=" + dcUserId
				+ ", hospCode=" + hospCode + ", hospitalC=" + hospitalC + ", hospObj=" + hospObj + ", hospListForView="
				+ hospListForView + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", status=" + status
				+ ", initHosp=" + initHosp + ", hospList=" + hospList + "]";
	}

}
