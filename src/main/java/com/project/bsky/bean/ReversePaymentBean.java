package com.project.bsky.bean;

import java.util.Date;
import java.util.List;

public class ReversePaymentBean {

	private Long userId;
	private Date fromDate;
	private Date toDate;
	private String typeNumber;
	private String userName;
	private String otp;
	private List<String> pymntList;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getTypeNumber() {
		return typeNumber;
	}

	public void setTypeNumber(String typeNumber) {
		this.typeNumber = typeNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public List<String> getPymntList() {
		return pymntList;
	}

	public void setPymntList(List<String> pymntList) {
		this.pymntList = pymntList;
	}

	@Override
	public String toString() {
		return "ReversePaymentBean [userId=" + userId + ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", typeNumber=" + typeNumber + ", userName=" + userName + ", otp=" + otp + ", pymntList=" + pymntList
				+ "]";
	}

}
