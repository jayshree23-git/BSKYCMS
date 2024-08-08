package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

@Data
public class Cpdapprovalbean {
	private Long claimId;
	private Long userId;
	private String amount;
	private String remarks;
	private Long actionRemarksId;
	private String actionRemark;
	private String urnNo;
	private Long pendingAt;
	private Long claimStatus;
	private String claimAmount;
	private String preSurgerySlip;
	private String postSurgerySlip;
	private String additionaldocs;
	private String dischargeSlip;
	private String investigationdocs;
	private String additionaldoc1;
	private String additionaldoc2;
	private Integer statusflag;
	private String intraSurgery;
	private String patientPhoto;
	private String specimenPhoto;
	private String mortality;
	private String investigationdocs1;
	private String investigationdocs2;
	private int timingLogId;
	private Long remarkSelect;
	private String additinalSlip;
	private String admissionSlip;
	private String actionType;
	private Long icdFlag;
	private List<ICDDetailsBean> icdFinalData;

}
