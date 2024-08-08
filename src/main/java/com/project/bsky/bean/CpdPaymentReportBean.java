package com.project.bsky.bean;

import javax.management.loading.PrivateClassLoader;

import lombok.Data;

@Data

public class CpdPaymentReportBean {
	private String cpdname;
	private String approved;
	private String reject;
	private String settelement;
	private String dishonour;
	private Double settelement_amount;
	private Double dishonour_amount;
	private Double final_amount;
	private String cpdstatus;

	
	private Long CLAIMID;
	private String APPROVEDAMOUNT;
	private String claimno;
	private String urn;
	private String PATIENTNAME;
	private String currenttotalamount;
	private String packagename;
	private String hospitalname;
	private String actualdateofadmission;
	private String actualdateofdischarge;
	private String authorizedcode;
	private String hospitalcode;
	private String packagecode;
	private String INVOICENO;
	private Long Assignedcpd;
	private String dishonourdate;
	private String alloteddate;
	private String previousassignedcpd;
	private String actionon;
	private String createdOn;
	private String cpdalloteddate;
	private String createdOnForDishounur;
	private String previuousAllotedDate;
	
	
	//cpd claim payment new Report
	private String assigendcpd;
	private Long cpduserid;
	private String totalcpdapprovedcases;
	private String totalCPDRejectedCases;
	private String totalProcessedClaims;
	private String cPDExaminedcasesrejectedbySystemduetononcomplianceofquerybyhospitalswithin7dayscases;
	private String cPDExaminedcasesrejectedbySystemduetononcomplianceofquerybyhospitalswithin7dayAmount;
	private String cPDApprovedcasesApprovedbyNodalDoctorsduringAudit;
	private String cPDApprovedcasesrejectedbyNodalDoctorsduringAudit;
	private String casesdetectedbyNodalDoctoswhereCPDremarksmismatchingwithamountsetteledbyCPD;
	private String casesdetectedbyNodalDoctorswherewrongmarkingofMortalitymadebyCPDs;
	private String casesforwhichsettelementprocessnotinitiatedbyCPDswithin3daysofAssignment;
	private String assignclaims;
	private String finalcase;
	private String dishonoramount;
	private String finalpayableamount;
	private String querycases;

}
