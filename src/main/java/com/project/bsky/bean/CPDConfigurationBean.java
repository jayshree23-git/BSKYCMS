/**
 * 
 */
package com.project.bsky.bean;

import java.util.List;

/**
 * @author satyabrata.s
 *
 */
public class CPDConfigurationBean {

	private Integer cpdMappingId;
	private Integer cpdId;
	private String stateCode;
	private String districtCode;
	private List<Hospital> hospitalCode;
	private String cpdName;
	private String stateName;
	private String districtName;
	private String hospitalName;
	private Integer status;
	private String hospCode;
	private Integer cpdUserId;

	private List<String> hospitalC;

	private List<HospBean> hospObj;

	private List<String> hospListForView;

	private Integer createdBy;

	private Integer updatedBy;

	private List<HospObj> hospList;

	public Integer getCpdMappingId() {
		return cpdMappingId;
	}

	public void setCpdMappingId(Integer cpdMappingId) {
		this.cpdMappingId = cpdMappingId;
	}

	public Integer getCpdId() {
		return cpdId;
	}

	public void setCpdId(Integer cpdId) {
		this.cpdId = cpdId;
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

	public String getCpdName() {
		return cpdName;
	}

	public void setCpdName(String cpdName) {
		this.cpdName = cpdName;
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

	public Integer getCpdUserId() {
		return cpdUserId;
	}

	public void setCpdUserId(Integer cpdUserId) {
		this.cpdUserId = cpdUserId;
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
		return "CPDConfigurationBean [cpdMappingId=" + cpdMappingId + ", cpdId=" + cpdId + ", stateCode=" + stateCode
				+ ", districtCode=" + districtCode + ", hospitalCode=" + hospitalCode + ", cpdName=" + cpdName
				+ ", stateName=" + stateName + ", districtName=" + districtName + ", hospitalName=" + hospitalName
				+ ", status=" + status + ", hospCode=" + hospCode + ", cpdUserId=" + cpdUserId + ", hospitalC="
				+ hospitalC + ", hospObj=" + hospObj + ", hospListForView=" + hospListForView + ", createdBy="
				+ createdBy + ", updatedBy=" + updatedBy + ", hospList=" + hospList + "]";
	}

}
