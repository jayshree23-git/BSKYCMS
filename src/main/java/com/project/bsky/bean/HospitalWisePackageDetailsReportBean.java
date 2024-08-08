package com.project.bsky.bean;

import lombok.Data;

@Data
public class HospitalWisePackageDetailsReportBean {
	private String STATECODE;
	private String STATENAME;
	private String DISTRICTNAME;
	private String HOSPITAL_CODE;
	private String HOSPITAL_NAME;
	private String PROCEDURENAME;
	private String PACKAGENAME;
	private String TotalClaimedunderpackages;
	private String TotalClaimedAmountunderpackages;


}
