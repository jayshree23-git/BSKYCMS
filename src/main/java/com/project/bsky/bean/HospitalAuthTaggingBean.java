package com.project.bsky.bean;

import com.project.bsky.model.HospitalAuthTagging;

public class HospitalAuthTaggingBean {

	private String hospitalName;
	private HospitalAuthTagging hospitalAuthTagging;

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public HospitalAuthTagging getHospitalAuthTagging() {
		return hospitalAuthTagging;
	}

	public void setHospitalAuthTagging(HospitalAuthTagging hospitalAuthTagging) {
		this.hospitalAuthTagging = hospitalAuthTagging;
	}

	@Override
	public String toString() {
		return "HospitalAuthTaggingBean [hospitalName=" + hospitalName + ", hospitalAuthTagging=" + hospitalAuthTagging
				+ "]";
	}

}
