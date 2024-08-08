package com.project.bsky.bean;

import java.util.Date;

public class Hospitalwisedischargecount {
	
	
	private String hospitalCode;
	private String hospitalName;
	private String totalDischarge;
	private String claimsubmitted;
	
	
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getTotalDischarge() {
		return totalDischarge;
	}
	public void setTotalDischarge(String totalDischarge) {
		this.totalDischarge = totalDischarge;
	}
	public String getClaimsubmitted() {
		return claimsubmitted;
	}
	public void setClaimsubmitted(String claimsubmitted) {
		this.claimsubmitted = claimsubmitted;
	}
	@Override
	public String toString() {
		return "Hospitalwisedischargecount [hospitalCode=" + hospitalCode + ", hospitalName=" + hospitalName
				+ ", totalDischarge=" + totalDischarge + ", claimsubmitted=" + claimsubmitted + "]";
	}
	
	
	
	

}
