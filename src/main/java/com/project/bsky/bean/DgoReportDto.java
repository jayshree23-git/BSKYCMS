package com.project.bsky.bean;

import lombok.Data;

@Data
public class DgoReportDto {
	private String urn;
	private String patientName;
	private String mobileNo;
	private String districtName;
	private String blockName;
	private String panchayatName;
	private String villageName;
	private String callResponse;
	private  String invoiceNo;
	private String dateOfAdm;
	private Integer totalAmountBlocked;
	private String hospitalDist;
	private  String hospitalName;
	private String procedureName;
	private String packageName;
	private String alottedDate;
	private String alternativeNo;
	private String transId;
	private String question1Response;
	private String question2Response;
	private String question3Response;
	private String question4Response;
	private String executiveRemarks;
	private Long cceId;
	private String mobileActiveStatus;
	private boolean status;
	private String hospitalCode;
	private String createdOn;
	private Integer totalData;
	private String feedbackLoginId;
	private Integer reportCheck;
	private Integer dialedCount;
	private String dcuserId;
	private String dcsubmittedDate;
	private String dcRemarks;
	private String dgosubmittedDate;
	private String dgoRemarks;


}
