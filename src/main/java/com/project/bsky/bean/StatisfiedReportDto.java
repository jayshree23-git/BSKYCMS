package com.project.bsky.bean;

import lombok.Data;

@Data
public class StatisfiedReportDto {
	private String fromDate;
	private String toDate;
	private String state;
	private String district;
	private String hospitalName;
	private Integer totalStatisfied;
	private Integer totalNotStatisfied;
	private Integer totalNoOfClaim;
	private Integer percentage;
	private Integer yes;
	private Integer no;
	private Integer notConnected;
	private String allottedDate;
	private Integer totalClaim;
	private Integer callCenterAction;
	private Integer question1;
	private Integer question1n;
	private Integer question1notConnected;
	private Integer question2;
	private Integer question2n;
	private Integer question2notConnected;
	private Integer question3;
	private Integer question3n;
	private Integer question3notConnected;
	private Integer question4;
	private Integer question4n;
	private Integer question4notConnected;
	private Integer noTreatementBsky;
}
