package com.project.bsky.bean;

import java.io.Serializable;

import lombok.Data;
@Data
public class financiladetailsbean implements Serializable{
	private long FLOAT_ID;
	private String FLOAT_NO;
	private Double AMOUNT;
	private String CREATED_BY;
	private String CREATED_ON;
	private Integer STATUS_FLAG;
	private Integer PENDING_AT;
	private Integer PAYMENT_STATUS;
	private long transactiondetailsid;
	private long claimid;
	private String URNno;
	private String CLaimno;
	private String patentname;
	private String packageid;
	private String ClaimRaisedon;
	private String packagename;
	
	private String hospitalname;
	private String hospitalCode;
	private String ActualdateofAdmission;
	private String ActualdateofDischarge;

}
