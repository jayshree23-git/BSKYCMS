package com.project.bsky.bean;

import java.util.Date;

public class DCDashboardBean {
	
	private Long userId;
	private Integer month;
	private Integer year;
	private Date fromDate;
	private Date toDate;
	private String hospitalCode;
	private String viewFlag;
	private String sectionFlag;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
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
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getViewFlag() {
		return viewFlag;
	}
	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}
	public String getSectionFlag() {
		return sectionFlag;
	}
	public void setSectionFlag(String sectionFlag) {
		this.sectionFlag = sectionFlag;
	}
	@Override
	public String toString() {
		return "DCDashboardBean [userId=" + userId + ", month=" + month + ", year=" + year + ", fromDate=" + fromDate
				+ ", toDate=" + toDate + ", hospitalCode=" + hospitalCode + ", viewFlag=" + viewFlag + ", sectionFlag="
				+ sectionFlag + "]";
	}

	
}
