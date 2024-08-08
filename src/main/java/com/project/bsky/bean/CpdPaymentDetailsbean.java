package com.project.bsky.bean;

import lombok.Data;

@Data
public class CpdPaymentDetailsbean {
	private String claimnumber;
	private String invoicenumber;
	private String urn;
	private String patientname;
	private String packagecode;
	private String actualdateofdischarge;
	private String actualdateofadmission;
	private String actionon;
	private String createdon;
	private String totalamountclaimed;
	private String approvedamount;
	private String cliamid;

}
