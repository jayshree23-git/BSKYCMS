package com.project.bsky.bean;

import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.SNOConfiguration;

public class HospitalInfoBean {

	private HospitalInformation hospital;
	private SNOConfiguration sno;
	private String snoName;
	private String dcName;
	private String categoryName;

	public HospitalInformation getHospital() {
		return hospital;
	}

	public void setHospital(HospitalInformation hospital) {
		this.hospital = hospital;
	}

	public SNOConfiguration getSno() {
		return sno;
	}

	public void setSno(SNOConfiguration sno) {
		this.sno = sno;
	}

	public String getSnoName() {
		return snoName;
	}

	public void setSnoName(String snoName) {
		this.snoName = snoName;
	}

	public String getDcName() {
		return dcName;
	}

	public void setDcName(String dcName) {
		this.dcName = dcName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "HospitalInfoBean [hospital=" + hospital + ", sno=" + sno + ", snoName=" + snoName + ", dcName=" + dcName
				+ ", categoryName=" + categoryName + "]";
	}

}
