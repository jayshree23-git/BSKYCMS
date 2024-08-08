package com.project.bsky.bean;

import lombok.Data;

@Data
public class CceReportDto {
	private String fromDate;
	private String toDate;
	private String stateName;
	private String districtName;
	private String hospitalName;
	private String totalCompleted;
	private String totalConnectedCall;
	private String totalNotConnectedCall;
	private String totalNoCall;
	private String totalCount;
	private String action;
	
	
	private String question1;
	private String question2;
	private String question3;
	private String question4;
	private String question1n;
	private String question2n;
	private String question3n;
	private String question4n;
	private String date;
	
}
