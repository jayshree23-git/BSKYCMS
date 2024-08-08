package com.project.bsky.bean;

import lombok.Data;

//Rajendra

@Data
public class CceSummaryReport {

	private String hospname;
	private String hospcode;
	private String statename;
	private String distname;
	private String callattempeted;
	private String trtmtunderbsky;
	private String trtmtnotstart;
	private String no;
	private String notconnected;
	private String yesnottrtmtunderbsky;
	private String pendingcalls;
	private String datafromPreviousday;
	private String dataavailableCurrentDate;
	private Integer totaldataavailableforCalling;
	
}
