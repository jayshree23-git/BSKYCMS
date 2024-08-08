/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author rajendra.sahoo
 *
 */
@Entity
@Table(name = "TXNCLAIM_FLOAT_DETAILS")
public class TxnclamFloateDetails {

	@Id
	@Column(name = "FLOAT_ID")
	private Long floateId;

	@Column(name = "FLOAT_NO")
	private String floateno;

	@Column(name = "AMOUNT")
	private Double amount;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CREATED_BY")
	private UserDetails createby;

	@Column(name = "CREATED_ON")
	private Date createon;

	@Column(name = "STATUS_FLAG")
	private Integer statusflag;

	@Column(name = "PENDING_AT")
	private Integer pendingAt;

	@Column(name = "PAYMENT_STATUS")
	private Integer paymentstatus;

	@Column(name = "UPDATED_BY")
	private Long updateby;

	@Column(name = "UPDATED_ON")
	private Date updateon;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "ASSIGNED_AUTHORITY")
	private Integer assignedauthority;

	@Column(name = "IS_VERIFIED")
	private Integer isVerified;

	@Column(name = "SNA_ID")
	private Long snaId;

	@Column(name = "FLOAT_DOC")
	private String floatDoc;

	@Transient
	private String screateon;
	
	@Transient
	private String claimcount;

	public String getScreateon() {
		return screateon;
	}

	public void setScreateon(String screateon) {
		this.screateon = screateon;
	}

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

	public UserDetails getCreateby() {
		return createby;
	}

	public void setCreateby(UserDetails createby) {
		this.createby = createby;
	}

	public Date getCreateon() {
		return createon;
	}

	public void setCreateon(Date createon) {
		this.createon = createon;
	}

	public Integer getStatusflag() {
		return statusflag;
	}

	public void setStatusflag(Integer statusflag) {
		this.statusflag = statusflag;
	}

	public Integer getPendingat() {
		return pendingAt;
	}

	public void setPendingat(Integer pendingAt) {
		this.pendingAt = pendingAt;
	}

	public Integer getPaymentstatus() {
		return paymentstatus;
	}

	public void setPaymentstatus(Integer paymentstatus) {
		this.paymentstatus = paymentstatus;
	}

	public Long getUpdateby() {
		return updateby;
	}

	public void setUpdateby(Long updateby) {
		this.updateby = updateby;
	}

	public Date getUpdateon() {
		return updateon;
	}

	public void setUpdateon(Date updateon) {
		this.updateon = updateon;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getAssignedauthority() {
		return assignedauthority;
	}

	public void setAssignedauthority(Integer assignedauthority) {
		this.assignedauthority = assignedauthority;
	}

	public Integer getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Integer isVerified) {
		this.isVerified = isVerified;
	}

	public Integer getPendingAt() {
		return pendingAt;
	}

	public void setPendingAt(Integer pendingAt) {
		this.pendingAt = pendingAt;
	}

	public Long getSnaId() {
		return snaId;
	}

	public void setSnaId(Long snaId) {
		this.snaId = snaId;
	}

	public String getFloatDoc() {
		return floatDoc;
	}

	public void setFloatDoc(String floatDoc) {
		this.floatDoc = floatDoc;
	}

	public String getClaimcount() {
		return claimcount;
	}

	public void setClaimcount(String claimcount) {
		this.claimcount = claimcount;
	}

	@Override
	public String toString() {
		return "TxnclamFloateDetails [floateId=" + floateId + ", floateno=" + floateno + ", amount=" + amount
				+ ", createby=" + createby + ", createon=" + createon + ", statusflag=" + statusflag + ", pendingAt="
				+ pendingAt + ", paymentstatus=" + paymentstatus + ", updateby=" + updateby + ", updateon=" + updateon
				+ ", remarks=" + remarks + ", assignedauthority=" + assignedauthority + ", isVerified=" + isVerified
				+ ", snaId=" + snaId + ", floatDoc=" + floatDoc + ", screateon=" + screateon + ", claimcount="
				+ claimcount + "]";
	}

	

}
