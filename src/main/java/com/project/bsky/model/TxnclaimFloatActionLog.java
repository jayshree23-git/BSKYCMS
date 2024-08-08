package com.project.bsky.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author ronauk
 *
 */
@Entity
@Table(name = "TXNCLAIM_FLOAT_ACTION_LOG")
public class TxnclaimFloatActionLog {

	@Id
	@Column(name = "ACTION_LOG_ID")
	private Long actionLogId;

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

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ACTIONBY")
	private UserDetails actionby;

	@Column(name = "ACTIONON")
	private Timestamp actionon;

	@Column(name = "FLOAT_DOC")
	private String floatDoc;

	public Long getActionLogId() {
		return actionLogId;
	}

	public void setActionLogId(Long actionLogId) {
		this.actionLogId = actionLogId;
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

	public Integer getPendingAt() {
		return pendingAt;
	}

	public void setPendingAt(Integer pendingAt) {
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

	public Long getSnaId() {
		return snaId;
	}

	public void setSnaId(Long snaId) {
		this.snaId = snaId;
	}

	public UserDetails getActionby() {
		return actionby;
	}

	public void setActionby(UserDetails actionby) {
		this.actionby = actionby;
	}

	public Timestamp getActionon() {
		return actionon;
	}

	public void setActionon(Timestamp actionon) {
		this.actionon = actionon;
	}

	public String getFloatDoc() {
		return floatDoc;
	}

	public void setFloatDoc(String floatDoc) {
		this.floatDoc = floatDoc;
	}

	@Override
	public String toString() {
		return "TxnclaimFloatActionLog [actionLogId=" + actionLogId + ", floateId=" + floateId + ", floateno="
				+ floateno + ", amount=" + amount + ", createby=" + createby + ", createon=" + createon
				+ ", statusflag=" + statusflag + ", pendingAt=" + pendingAt + ", paymentstatus=" + paymentstatus
				+ ", updateby=" + updateby + ", updateon=" + updateon + ", remarks=" + remarks + ", assignedauthority="
				+ assignedauthority + ", isVerified=" + isVerified + ", snaId=" + snaId + ", actionby=" + actionby
				+ ", actionon=" + actionon + ", floatDoc=" + floatDoc + "]";
	}

}
