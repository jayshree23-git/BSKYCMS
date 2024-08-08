package com.project.bsky.bean;

import lombok.Data;

@Data

public class claimtrackingdetails {
	private String urn;
	private String createdon;
	private String hospitalcode;
	private Long claimstatus;
	private Long pendingat;
	private String CLAIM_CASE_NO;
	private String packagecode;
	private String patientname;
	private String actualdateofdischarge;
	private String actualdateofadmission;
	private String packagename;
	private Long CLAIM_AMOUNT;
	private Long claimid;
	private String authorizedcode;
	private Long transactiondetailsid;
	private String hospitalname;
	private String claim_no;
	private String totalamountclaimed;
	private String invoiceno;
	private String caseno;
	private String claimcaseno;
	private String hospitalclaimno;
	private String hospitalbillno;
	private String claimbilldate;

}
