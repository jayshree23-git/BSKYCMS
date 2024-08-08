package com.project.bsky.bean;

import lombok.Data;

@Data
public class HospitalAllReportDto {
//	private Long Id ;
	private String fromDate;
	private String toDate;
	private String hospitalName;
	private String hospitalCode;
	private String admission;
	private String blocking;
	private String unblocking;
	private String discharge;
	private String preauth;
	private String action;
	
	
	private String urn;
	private String membername;
	private String gender;
	private String pheadname;
	private String packagecatname;
	private String date;
	
	

}
