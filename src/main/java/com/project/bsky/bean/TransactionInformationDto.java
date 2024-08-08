package com.project.bsky.bean;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class TransactionInformationDto {

	private Long executiveUserId;

	private String allottedDate;
	private Long transactionId;
	private String memberStateCode;
	private String stateName;
	private String districtCode;
	private String districtName;
	private String blockCode;
	private String blockName;
	private String panchayatCode;
	private String panchayatName;
	private String villageCode;
	private String villageName;
	private String uidnumber;
	private String urn;
	private String transactionCode;
	private String policystartDate;
	private String packageName;
	private String procedureName;
	private String policyenddate;	
	private String memberid;	
	private String patientName;
	private String patientContactNumber;
	private String patientgender;	
	private String age;	
	private String headmemberid;	
	private String headmembername;
	private String hospitalCode;
	private String hospitalName;
	private String hospitalState;
	private String hospitalDistrict;
	private String hospitalauthoritycode;
	private String transactiondate;
	private String mortality;
	private String mortalitysummary;
	private String admissionDate;
	private String remarks;
	private String invoice;
	private String dischargeflag;
	private String totalAmoutClaimed;
	private String availabebalance;
	private String createdby;
	private String createdon;
	private String updatedby;
	private String updatedon;
	private Integer deletedflag;

	private String caseNo;
	private String actStat;
	private String catg;
	private Integer attemptCount;

	private String reAssignRemark;
	private Date reAssignDate;
	private Long id;
}
