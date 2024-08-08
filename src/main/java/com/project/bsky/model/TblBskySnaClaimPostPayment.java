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
@Table(name = "TBL_BSKYSNACLAIMS_POSTPAYMENT")
public class TblBskySnaClaimPostPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TBL_BSKYSNACLAIMS_POSTPAYMENT_ID")
	@SequenceGenerator(name = "SEQ_TBL_BSKYSNACLAIMS_POSTPAYMENT_ID", sequenceName = "SEQ_TBL_BSKYSNACLAIMS_POSTPAYMENT_ID", allocationSize = 1)
	@Column(name = "POSTPAYMENT_ID")
	private Integer postPaymentId;

	@Column(name = "PAYMENT_MODE_ID")
	private Long paymentModeId;

	@Column(name = "TRANSID")
	private Long transId;

	@Column(name = "BANK_ID")
	private Long bankId;

	@Column(name = "DD_CHEQUE_NUMBER")
	private String ddChequeNo;

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

	@Column(name = "PAYMENT_DATE")
	private Date paymentDate;

	public Integer getPostPaymentId() {
		return postPaymentId;
	}

	public void setPostPaymentId(Integer postPaymentId) {
		this.postPaymentId = postPaymentId;
	}

	public Long getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Long paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public Long getTransId() {
		return transId;
	}

	public void setTransId(Long transId) {
		this.transId = transId;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getDdChequeNo() {
		return ddChequeNo;
	}

	public void setDdChequeNo(String ddChequeNo) {
		this.ddChequeNo = ddChequeNo;
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

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Override
	public String toString() {
		return "TblBskySnaClaimPostPayment [postPaymentId=" + postPaymentId + ", paymentModeId=" + paymentModeId
				+ ", transId=" + transId + ", bankId=" + bankId + ", ddChequeNo=" + ddChequeNo + ", createdBy="
				+ createdBy + ", createdOn=" + createdOn + ", updatedBy=" + updatedBy + ", updatedOn=" + updatedOn
				+ ", status=" + status + ", paymentDate=" + paymentDate + "]";
	}

}
