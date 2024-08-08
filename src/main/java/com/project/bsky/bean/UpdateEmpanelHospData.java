package com.project.bsky.bean;

import lombok.Data;

@Data
public class UpdateEmpanelHospData {
	
	private String hospitalId;
	private String hospitalCode;
	private String hospitalName;
	private String stateCode;
	private String districtCode;
	private String mobile;
	private String emailId;
	private String status;
	private String hospitalCategoryid;
	private String hosCValidDateFrom;
	private String hosCValidDateTo;
	private String mou;
	private String mouStartDt;
	private String mouEndDt;
	private String mouStatus;
	private String updatedby;
	private String updateon;
	private String stateName;
	private String districtName;
	private String empanelmentstatus;
	private String mouDocUpload;
	private String isBlockActive;
	private String cpdApprovalRequired;
	private String preauthapprovalrequired;

}
