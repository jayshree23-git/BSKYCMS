/**
 * 
 */
package com.project.bsky.bean;

/**
 * @author hrusikesh.mohanty
 *
 */
public class TxnclamFloateDetailsbean {
	private Long floateId;
	private String floateno;
	private Double amount;
	private String createby;
	private String createon;
	private String statusflag;
	private String pendingat;
	private String paymentstatus;
	private Long updateby;
	private String updateon;
	private String remarks;
	private String remarkby;
	private Long roundAmount;
	public String getRemarkby() {
		return remarkby;
	}

	public void setRemarkby(String remarkby) {
		this.remarkby = remarkby;
	}

	private String assignedauthority;
	private String isVerified;
	private String fullname;
	private String snaFullName;
	private String assignedFoName;
	private Integer count;

	public Long getFloateId() {
		return floateId;
	}

	public void setFloateId(Long floateId) {
		this.floateId = floateId;
	}

	public String getFloateno() {
		return floateno;
	}

	public void setFloateno(String floateno) {
		this.floateno = floateno;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

	public String getCreateon() {
		return createon;
	}

	public void setCreateon(String createon) {
		this.createon = createon;
	}

	public String getStatusflag() {
		return statusflag;
	}

	public void setStatusflag(String statusflag) {
		this.statusflag = statusflag;
	}

	public String getPendingat() {
		return pendingat;
	}

	public void setPendingat(String pendingat) {
		this.pendingat = pendingat;
	}

	public String getPaymentstatus() {
		return paymentstatus;
	}

	public void setPaymentstatus(String paymentstatus) {
		this.paymentstatus = paymentstatus;
	}

	public Long getUpdateby() {
		return updateby;
	}

	public void setUpdateby(Long updateby) {
		this.updateby = updateby;
	}

	public String getUpdateon() {
		return updateon;
	}

	public void setUpdateon(String updateon) {
		this.updateon = updateon;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAssignedauthority() {
		return assignedauthority;
	}

	public void setAssignedauthority(String assignedauthority) {
		this.assignedauthority = assignedauthority;
	}

	public String getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(String isVerified) {
		this.isVerified = isVerified;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getSnaFullName() {
		return snaFullName;
	}

	public void setSnaFullName(String snaFullName) {
		this.snaFullName = snaFullName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getAssignedFoName() {
		return assignedFoName;
	}

	public void setAssignedFoName(String assignedFoName) {
		this.assignedFoName = assignedFoName;
	}

	public Long getRoundAmount() {
		return roundAmount;
	}

	public void setRoundAmount(Long roundAmount) {
		this.roundAmount = roundAmount;
	}

	@Override
	public String toString() {
		return "TxnclamFloateDetailsbean [floateId=" + floateId + ", floateno=" + floateno + ", amount=" + amount
				+ ", createby=" + createby + ", createon=" + createon + ", statusflag=" + statusflag + ", pendingat="
				+ pendingat + ", paymentstatus=" + paymentstatus + ", updateby=" + updateby + ", updateon=" + updateon
				+ ", remarks=" + remarks + ", remarkby=" + remarkby + ", roundAmount=" + roundAmount
				+ ", assignedauthority=" + assignedauthority + ", isVerified=" + isVerified + ", fullname=" + fullname
				+ ", snaFullName=" + snaFullName + ", assignedFoName=" + assignedFoName + ", count=" + count + "]";
	}

 

}
