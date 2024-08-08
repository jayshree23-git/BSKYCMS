package com.project.bsky.bean;

public class UserProfileBean {

	private int bskyUserId;
	private String username;
	private long mobileNo;
	private int status;
	private String emailId;
	private String stateName;
	private String districtName;
	private String districtCode;
	private String fullName;
	private String groupName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getBskyUserId() {
		return bskyUserId;
	}

	public void setBskyUserId(int bskyUserId) {
		this.bskyUserId = bskyUserId;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return "UserProfileBean [bskyUserId=" + bskyUserId + ", username=" + username + ", mobileNo=" + mobileNo
				+ ", status=" + status + ", emailId=" + emailId + ", stateName=" + stateName + ", districtName="
				+ districtName + ", districtCode=" + districtCode + ", fullName=" + fullName + ", groupName="
				+ groupName + "]";
	}
	
}
