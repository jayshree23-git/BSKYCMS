package com.project.bsky.bean;

import lombok.Data;

@Data
public class MonthWiseClaimDataReportBean {
	private String Statename;

	private String Statecode;
	private String Month;
    private String Totalclaimed;
    private String Totalclaimamount;

}
