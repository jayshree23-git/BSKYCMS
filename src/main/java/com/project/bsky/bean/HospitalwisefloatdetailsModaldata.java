package com.project.bsky.bean;

import java.util.Date;

public class HospitalwisefloatdetailsModaldata {
	private Long userId;
	private Date fromDate;
	private Date toDate;
	private String hospitacodeString;
	private String floatnumber;
	private String hospitacode;
	public String getHospitacode() {
		return hospitacode;
	}
	public void setHospitacode(String hospitacode) {
		this.hospitacode = hospitacode;
	}
	public String getFloatnumber() {
		return floatnumber;
	}
	public void setFloatnumber(String floatnumber) {
		this.floatnumber = floatnumber;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public String getHospitacodeString() {
		return hospitacodeString;
	}
	public void setHospitacodeString(String hospitacodeString) {
		this.hospitacodeString = hospitacodeString;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	@Override
	public String toString() {
		return "HospitalwisefloatdetailsModaldata [userId=" + userId + ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", hospitacodeString=" + hospitacodeString + ", floatnumber=" + floatnumber + ", hospitacode="
				+ hospitacode + "]";
	}




}
