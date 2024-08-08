package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author ronauk
 *
 */
@Entity
@Table(name = "TBL_BSKYSNA_POSTPAYMENT_SUMMARY")
public class TblBskySnaPostPymntSummary {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TBL_BSKYSNA_PP_SUMMARY_LOGID")
	@SequenceGenerator(name = "SEQ_TBL_BSKYSNA_PP_SUMMARY_LOGID", sequenceName = "SEQ_TBL_BSKYSNA_PP_SUMMARY_LOGID", allocationSize = 1)
	@Column(name = "POSTPAYMENT_LOGID")
	private Integer logId;

	@Column(name = "FINAL_APPROVE_AMOUNT")
	private Double finalApproveAmount;

	@Column(name = "ACTUAL_PAID_AMOUNT")
	private Double actualPaidAmount;

	@Column(name = "PAYEMENT_MODE")
	private Long paymentMode;

	@Column(name = "PAYMENT_INFO")
	private String paymentInfo;

	@Column(name = "BANK_ID")
	private Long bankId;

	@Column(name = "PAYMENT_DATE")
	private Date paymentDate;

	@Column(name = "CREATED_BY")
	private Long createdBy;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "UPDATED_BY")
	private Long updatedBy;

	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "STATUSFLAG")
	private Integer status;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Double getFinalApproveAmount() {
		return finalApproveAmount;
	}

	public void setFinalApproveAmount(Double finalApproveAmount) {
		this.finalApproveAmount = finalApproveAmount;
	}

	public Double getActualPaidAmount() {
		return actualPaidAmount;
	}

	public void setActualPaidAmount(Double actualPaidAmount) {
		this.actualPaidAmount = actualPaidAmount;
	}

	public Long getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(Long paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TblBskySnaClaimPostPymntSummary [logId=" + logId + ", finalApproveAmount=" + finalApproveAmount
				+ ", actualPaidAmount=" + actualPaidAmount + ", paymentMode=" + paymentMode + ", paymentInfo="
				+ paymentInfo + ", bankId=" + bankId + ", paymentDate=" + paymentDate + ", createdBy=" + createdBy
				+ ", createdOn=" + createdOn + ", updatedBy=" + updatedBy + ", updatedOn=" + updatedOn + ", status="
				+ status + "]";
	}

}
