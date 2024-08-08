package com.project.bsky.bean;

import java.util.Date;

public class GODashboardBean {

	private Long userId;
	private String stateCode;
	private String districtCode;
	private String hospitalCode;
	private String sectionFlag;
	private String inSection;
	private Date fromDate;
	private Date toDate;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public String getSectionFlag() {
		return sectionFlag;
	}
	public void setSectionFlag(String sectionFlag) {
		this.sectionFlag = sectionFlag;
	}
	
	public String getInSection() {
		return inSection;
	}
	public void setInSection(String inSection) {
		this.inSection = inSection;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	@Override
	public String toString() {
		return "GODashboardBean [userId=" + userId + ", stateCode=" + stateCode + ", districtCode=" + districtCode
				+ ", hospitalCode=" + hospitalCode + ", sectionFlag=" + sectionFlag + ", inSection=" + inSection
				+ ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
	}
	
	
	
}
