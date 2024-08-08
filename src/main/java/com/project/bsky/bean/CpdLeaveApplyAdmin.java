/**
 * 
 */
package com.project.bsky.bean;

/**
 * @author priyanka.singh
 *
 */
public class CpdLeaveApplyAdmin {
	
	private String leaveId;
	private String bskyUserId;
	private String formdate;
	private String todate;
	private String remarks;
	private String createdby;
	private String fullname;
	private String userName;
	private String hospitalCode;
	private String hospitalName;
	public String getLeaveId() {
		return leaveId;
	}
	public void setLeaveId(String leaveId) {
		this.leaveId = leaveId;
	}
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
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	
	public String getBskyUserId() {
		return bskyUserId;
	}
	public void setBskyUserId(String bskyUserId) {
		this.bskyUserId = bskyUserId;
	}
	@Override
	public String toString() {
		return "CpdLeaveApplyAdmin [leaveId=" + leaveId + ", bskyUserId=" + bskyUserId + ", formdate=" + formdate
				+ ", todate=" + todate + ", remarks=" + remarks + ", createdby=" + createdby + ", fullname=" + fullname
				+ ", userName=" + userName + ", hospitalCode=" + hospitalCode + ", hospitalName=" + hospitalName + "]";
	}
	
	
}
