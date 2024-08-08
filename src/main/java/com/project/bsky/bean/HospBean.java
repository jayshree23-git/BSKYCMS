package com.project.bsky.bean;

public class HospBean {

	private String hospitalCode;
	private String hospitalName;

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

	@Override
	public String toString() {
		return "HospBean [hospitalCode=" + hospitalCode + ", hospitalName=" + hospitalName + "]";
	}

}
