package com.project.bsky.bean;

public class CpdLeaveInfoBean {
	private String bskyUserId;
	private String leaveId;
	private String formdate;
	private String todate;
	private String remarks;
	private String createdby;
	public String getFormdate() {
		return formdate;
	}
	public void setFormdate(String formdate) {
		this.formdate = formdate;
	}
	public String getTodate() {
		return todate;
	}
	public void setTodate(String todate) {
		this.todate = todate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public String getLeaveId() {
		return leaveId;
	}
	public void setLeaveId(String leaveId) {
		this.leaveId = leaveId;
	}
	public String getBskyUserId() {
		return bskyUserId;
	}
	public void setBskyUserId(String bskyUserId) {
		this.bskyUserId = bskyUserId;
	}
	@Override
	public String toString() {
		return "CpdLeaveInfoBean [bskyUserId=" + bskyUserId + ", leaveId=" + leaveId + ", formdate=" + formdate
				+ ", todate=" + todate + ", remarks=" + remarks + ", createdby=" + createdby + "]";
	}
	
	
}
