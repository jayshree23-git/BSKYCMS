package com.project.bsky.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class TreatmentHistoryPerUrnBean implements Serializable {
	private String urnno;
	private String packagename;
	private String packagecode;
	private String hospitalname;
	private String totalamount;
	private String dateofadmission;
	private String dateofdischarge;
	private String patientname;
	private String status;
	private Long transctionId;
	private Long claimId;
	private String hospitalclaimedamount;
	private String snaapproveamount;
	private String cpdapproveamount;
	private String cpdname;
	private String actualDateofadmission;
	private String actualDateofdischarge;
	private String invoiceNo;
	private String caseNo;
}
