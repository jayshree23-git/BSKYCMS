package com.project.bsky.bean;

import lombok.Data;

@Data
public class NonComplianceBean {

	private String fromDate;
	private String toDate;
	private String stateCode;
	private String districtCode;
	private String hospitalCode;
	private String actionId;
	private Long snoid;
	
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
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
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	@Override
	public String toString() {
		return "NonComplianceBean [fromDate=" + fromDate + ", toDate=" + toDate + ", stateCode=" + stateCode
				+ ", districtCode=" + districtCode + ", hospitalCode=" + hospitalCode + ", actionId=" + actionId
				+ ", snoid=" + snoid + "]";
	}
	
	
	
	
}
