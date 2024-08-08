package com.project.bsky.bean;

import java.util.Arrays;
import java.util.Date;

public class OldFloatBean {

	private Long userId;
	private Date fromDate;
	private Date toDate;
	private String stateId;
	private String districtId;
	private String hospitalId;
	private Long snaUserId;
	private String[] hospitalCodeArr;

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

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getSnaUserId() {
		return snaUserId;
	}

	public void setSnaUserId(Long snaUserId) {
		this.snaUserId = snaUserId;
	}

	public String[] getHospitalCodeArr() {
		return hospitalCodeArr;
	}

	public void setHospitalCodeArr(String[] hospitalCodeArr) {
		this.hospitalCodeArr = hospitalCodeArr;
	}

	@Override
	public String toString() {
		return "OldFloatBean [userId=" + userId + ", fromDate=" + fromDate + ", toDate=" + toDate + ", stateId="
				+ stateId + ", districtId=" + districtId + ", hospitalId=" + hospitalId + ", snaUserId=" + snaUserId
				+ ", hospitalCodeArr=" + Arrays.toString(hospitalCodeArr) + "]";
	}

}
