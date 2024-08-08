package com.project.bsky.bean;

public class SnaFloatReportBean {
	private long floatid;
	private String floatno;
	public String getFloatno() {
		return floatno;
	}
	public void setFloatno(String floatno) {
		this.floatno = floatno;
	}
	private double amount;
	private String createdby;
	private String CreatedOn;
	private Integer statusflag;
	private Integer pendingat;
	@Override
	public String toString() {
		return "SnaFloatReportBean [floatid=" + floatid + ", floatno=" + floatno + ", amount=" + amount + ", createdby="
				+ createdby + ", CreatedOn=" + CreatedOn + ", statusflag=" + statusflag + ", pendingat=" + pendingat
				+ ", paymentstatus=" + paymentstatus + "]";
	}
	public long getFloatid() {
		return floatid;
	}
	public void setFloatid(long floatid) {
		this.floatid = floatid;
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public String getCreatedOn() {
		return CreatedOn;
	}
	public void setCreatedOn(String createdOn) {
		CreatedOn = createdOn;
	}
	public Integer getStatusflag() {
		return statusflag;
	}
	public void setStatusflag(Integer statusflag) {
		this.statusflag = statusflag;
	}
	public Integer getPendingat() {
		return pendingat;
	}
	public void setPendingat(Integer pendingat) {
		this.pendingat = pendingat;
	}
	public Integer getPaymentstatus() {
		return paymentstatus;
	}
	public void setPaymentstatus(Integer paymentstatus) {
		this.paymentstatus = paymentstatus;
	}
	private Integer paymentstatus;







}
