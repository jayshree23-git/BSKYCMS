/**
 * 
 */
package com.project.bsky.bean;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author santanu.barad
 *
 */
public class PostPaymentRequest {
	private Long userId;
	private Long bankModeId;
	private Long bankId;
	private String typeNumber;
	private List<Long> claimList;
	private Date currentDate;
	private Double totalPaidAmount;
	private Double paidAmount;
	private List<Map<String, Object>> cpdList;
	private Long month;
	private Long year;

	public Long getBankModeId() {
		return bankModeId;
	}

	public void setBankModeId(Long bankModeId) {
		this.bankModeId = bankModeId;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getTypeNumber() {
		return typeNumber;
	}

	public void setTypeNumber(String typeNumber) {
		this.typeNumber = typeNumber;
	}

	public List<Long> getClaimList() {
		return claimList;
	}

	public void setClaimList(List<Long> claimList) {
		this.claimList = claimList;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public Double getTotalPaidAmount() {
		return totalPaidAmount;
	}

	public void setTotalPaidAmount(Double totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public List<Map<String, Object>> getCpdList() {
		return cpdList;
	}

	public void setCpdList(List<Map<String, Object>> cpdList) {
		this.cpdList = cpdList;
	}

	public Long getMonth() {
		return month;
	}

	public void setMonth(Long month) {
		this.month = month;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "PostPaymentRequest [userId=" + userId + ", bankModeId=" + bankModeId + ", bankId=" + bankId
				+ ", typeNumber=" + typeNumber + ", claimList=" + claimList + ", currentDate=" + currentDate
				+ ", totalPaidAmount=" + totalPaidAmount + ", paidAmount=" + paidAmount + ", cpdList=" + cpdList
				+ ", month=" + month + ", year=" + year + "]";
	}

}
