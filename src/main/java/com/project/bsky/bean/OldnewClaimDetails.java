package com.project.bsky.bean;

import lombok.Data;

@Data
public class OldnewClaimDetails {
	private String claimnumber;
	private String urn;
	private String hospitalCode;
	private String hospitalName;
	private String patientname;
	private String actualdateofdischarge;
	private String claimraiseby;
	private String caseno;
	private String invoiceno;
	private String packagecode;
	private String packagename;
	private String CURRENT_STATUS;
	private String CLAIM_SUBMITTED_STATUS;
	private String claimid;
	private String transactiondetailsid;
	private String authorizedcode;
}