package com.project.bsky.bean;
/**
 * @author preetam.mishra
 *
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimsQueriedToHospitalBySNODetailsBean {

	private String urn;
	private String invoiceNo;
	private String packageId;
	private String unblockAmt;
	private String totalBlockedAmt;
	private String totalClaimedAmt;
	private String packageCode;
	private String packageName;
	private String packageCategoryCode;
	private String currentTotalAmt;
	private String gender;
	private String age;
	private String patientName;
	private String procedureName;
	private String hospitalName;
	private String admissionSlip;
	private String treatmentSlip;
	private String addtionaldoc;
	private String dischargeSlip;
	private String investigationDoc;
	private String preSurgeryPhoto;
	private String postSurgeryPhoto;
	private String createdBy;
	private String createdOn;
	private String updatedBy;
	private String updatedOn;
	private String claimID;
	private String blockName;
	private String districtName;
	private String stateName;
	private String dateOfDischarge;
	private String dateOfAdmission;
	private String actionOn;
	private String actiontype;
	private String remarks;
	private String remark;
	private String hospitalcode;
	private String dateofadmission;
	private String actionby;
	private String approvedamount;
	private String username;
	private String transactiondetailsid;
}
