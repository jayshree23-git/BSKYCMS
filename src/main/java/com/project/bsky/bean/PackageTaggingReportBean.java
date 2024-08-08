package com.project.bsky.bean;

import lombok.Data;

@Data
public class PackageTaggingReportBean {
	
	private String stateName;
	private String districtName;
	private String hospitalCode;
	private String hospitalName;
	private String specialityType;
	private String headerCode;
	private String headerName;
	private String subPackageCode;
	private String subPackageName;
	private String procedureCode;
	private String procedureDescription;
	private String packageAmount;
	private String taggedType;
}
