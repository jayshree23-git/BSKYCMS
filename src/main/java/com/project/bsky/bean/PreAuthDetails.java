/**
 * 
 */
package com.project.bsky.bean;

import java.util.List;

/**
 * @author santanu.barad
 *
 */
public class PreAuthDetails {
	private Long id;
	private Long actionType;
	private Long txnPackageDetailId;
	private Long userId;
	private Long amount;
	private String remarks;
	private String description;
	private Long actionRemarksId;
	private Long icdFlag;
	private List<ICDDetailsBean> icdFinalData;
	private Long requestId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActionType() {
		return actionType;
	}

	public void setActionType(Long actionType) {
		this.actionType = actionType;
	}

	public Long getTxnPackageDetailId() {
		return txnPackageDetailId;
	}

	public void setTxnPackageDetailId(Long txnPackageDetailId) {
		this.txnPackageDetailId = txnPackageDetailId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getActionRemarksId() {
		return actionRemarksId;
	}

	public void setActionRemarksId(Long actionRemarksId) {
		this.actionRemarksId = actionRemarksId;
	}

	public Long getIcdFlag() {
		return icdFlag;
	}

	public void setIcdFlag(Long icdFlag) {
		this.icdFlag = icdFlag;
	}

	public List<ICDDetailsBean> getIcdFinalData() {
		return icdFinalData;
	}

	public void setIcdFinalData(List<ICDDetailsBean> icdFinalData) {
		this.icdFinalData = icdFinalData;
	}
	

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		return "PreAuthDetails [id=" + id + ", actionType=" + actionType + ", txnPackageDetailId=" + txnPackageDetailId
				+ ", userId=" + userId + ", amount=" + amount + ", remarks=" + remarks + ", description=" + description
				+ ", actionRemarksId=" + actionRemarksId + ", icdFlag=" + icdFlag + ", icdFinalData=" + icdFinalData
				+ ", requestId=" + requestId + ", getId()=" + getId() + ", getActionType()=" + getActionType()
				+ ", getTxnPackageDetailId()=" + getTxnPackageDetailId() + ", getUserId()=" + getUserId()
				+ ", getAmount()=" + getAmount() + ", getRemarks()=" + getRemarks() + ", getDescription()="
				+ getDescription() + ", getActionRemarksId()=" + getActionRemarksId() + ", getIcdFlag()=" + getIcdFlag()
				+ ", getIcdFinalData()=" + getIcdFinalData() + ", getRequestId()=" + getRequestId() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	 

}
