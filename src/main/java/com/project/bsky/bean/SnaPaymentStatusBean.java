package com.project.bsky.bean;

import java.util.Date;

public class SnaPaymentStatusBean {

	private String hospitalCode;
	private Date fromdate;
	private Date todate;
	
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public Date getFromdate() {
		return fromdate;
	}
	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}
	public Date getTodate() {
		return todate;
	}
	public void setTodate(Date todate) {
		this.todate = todate;
	}
	@Override
	public String toString() {
		return "SnaPaymentStatusBean [hospitalCode=" + hospitalCode + ", fromdate=" + fromdate + ", todate=" + todate
				+ "]";
	}
	
	
}
