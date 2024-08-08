package com.project.bsky.bean;

import java.util.Date;

import lombok.Data;

@Data
public class PaymentFreezeBean {

	private long claimid;
	private long transactionDetailsId;
	private String URN;
	private String PatientName;
	private String PackageCode;
	private String invoiceNumber;
	private Double currentTotalAmount;
	private String CreatedOn;
	private String PackageName;
	private String hospitalcode;
	private String authorizedcode;
	private String districtname;
	private String hospitalname;
	private String statename;
	private String actualDateOfAdmission;
	private String actualDateOfDischarge;
	private Date cpdAlotteddate;
	private String claimNo;
	private Integer claimStatus;
	private String floatNumber;
	private Double snaApprovedAmount;
	private Double cpdApprovedAmount;
	private String createdByFullName;
	private String caseno;
	private String foremarks;
	private String isBulkApproved;
	private String snaDoctorName;
	private Integer pendingAt;
	private Double totalAmountBlocked;
	private Double amountDifference;
	private Integer walletRefundStatus;
	private Double refundAmount;
	private String refundedOn;
	private String refundedBy;
	private String assignedSna;
	private String assignedCpd;
	private Long txnpackagedetailid;
	private String value;
	private String messgae;
}
