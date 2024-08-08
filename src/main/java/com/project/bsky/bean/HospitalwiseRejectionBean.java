package com.project.bsky.bean;

import lombok.Data;

@Data
public class HospitalwiseRejectionBean {
	
	private String statecode;
	private String statename;
	private String districtname;
	private String hospitalcode;
	private String hospitalname;
	private String totalSNAApproved;
	private String snaapprovedamount;
	private String noOfSNARejection;
	private String SNARejectionamount;

}
