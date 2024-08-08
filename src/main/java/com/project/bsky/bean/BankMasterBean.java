/**
 * 
 */
package com.project.bsky.bean;

import com.project.bsky.model.UserDetails;

/**
 * @author priyanka.singh
 *
 */
public class BankMasterBean {
	
	private Integer bankId;
	private String bankName;
	private String createdBy;
	private String updatedBy;
	private Integer statusFlag;
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Integer getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}
	@Override
	public String toString() {
		return "BankMasterBean [bankId=" + bankId + ", bankName=" + bankName + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + ", statusFlag=" + statusFlag + "]";
	}
	

}
