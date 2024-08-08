package com.project.bsky.bean;

import java.sql.Timestamp;

public class CpdQueryToHospitalBean {

	private Long claimId;
	private String transactionId;
	private String transactionCode;
	private String urnNo;
	private String hospitalCode;
	private String pendingAt;
	private String claimStatus;
	private String claimAssignedCPDUserId;
	private String claimAssignedSNOUserId;
	private String docAdminssionSlip;
	private String docTreatmentSlip;
	private String docHospitalBill;
	private String docDischargeSlip;
	private String docInvestigation;
	private String patientPresurgeryPhotoUploadedPath;
	private String patientPostsurgeryPhotoUploadedPath;
	private String docPath1;
	private String docPath2;
	private String createdBy;
	private Timestamp createdDate;
	private String updatedBy;
	private Timestamp updateDate;
	private String packageCode;
	private String hospitalBill;
	private Long userid;
	private String patientname;
	private String packagecode;
	private String packagename;
	private String dateofadmission;
	private String snoapprovedamount;
	private String cpdapprovedamount;
	private String updateon;
	private String claim_no;
	private String cpdDaysleftString;

	public String getRemainingdayscpdquery() {
		return remainingdayscpdquery;
	}

	public void setRemainingdayscpdquery(String remainingdayscpdquery) {
		this.remainingdayscpdquery = remainingdayscpdquery;
	}

	private String cpdquerydateString;
	private String remainingdayscpdquery;

	public String getCpdquerydateString() {
		return cpdquerydateString;
	}

	public void setCpdquerydateString(String cpdquerydateString) {
		this.cpdquerydateString = cpdquerydateString;
	}

	public String getCaseno() {
		return caseno;
	}

	public void setCaseno(String caseno) {
		this.caseno = caseno;
	}

	private String Daysleft;
	private String invoiceno;
	private String dateofdischarge;
	private String hospitalName;
	private String caseno;

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getDateofdischarge() {
		return dateofdischarge;
	}

	public void setDateofdischarge(String dateofdischarge) {
		this.dateofdischarge = dateofdischarge;
	}

	public String getDaysleft() {
		return Daysleft;
	}

	public void setDaysleft(String daysleft) {
		Daysleft = daysleft;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private String remarks;
	private String remark;

	public String getActualdateofadmission() {
		return actualdateofadmission;
	}

	public void setActualdateofadmission(String actualdateofadmission) {
		this.actualdateofadmission = actualdateofadmission;
	}

	public String getActualdateofdischarge() {
		return actualdateofdischarge;
	}

	public void setActualdateofdischarge(String actualdateofdischarge) {
		this.actualdateofdischarge = actualdateofdischarge;
	}

	public String getTransactiondetailsid() {
		return transactiondetailsid;
	}

	public void setTransactiondetailsid(String transactiondetailsid) {
		this.transactiondetailsid = transactiondetailsid;
	}

	private String actualdateofadmission;
	private String actualdateofdischarge;
	private String transactiondetailsid;

	public String getCpdDaysleftString() {
		return cpdDaysleftString;
	}

	public void setCpdDaysleftString(String cpdDaysleftString) {
		this.cpdDaysleftString = cpdDaysleftString;
	}

	public String getClaim_no() {
		return claim_no;
	}

	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}

	public String getUpdateon() {
		return updateon;
	}

	public void setUpdateon(String updateon) {
		this.updateon = updateon;
	}

	public String getCpdapprovedamount() {
		return cpdapprovedamount;
	}

	public void setCpdapprovedamount(String cpdapprovedamount) {
		this.cpdapprovedamount = cpdapprovedamount;
	}

	public String getSnoapprovedamount() {
		return snoapprovedamount;
	}

	public void setSnoapprovedamount(String snoapprovedamount) {
		this.snoapprovedamount = snoapprovedamount;
	}

	public Long getClaimId() {
		return claimId;
	}

	public void setClaimId(Long claimId) {
		this.claimId = claimId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getUrnNo() {
		return urnNo;
	}

	public void setUrnNo(String urnNo) {
		this.urnNo = urnNo;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getPendingAt() {
		return pendingAt;
	}

	public void setPendingAt(String pendingAt) {
		this.pendingAt = pendingAt;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getClaimAssignedCPDUserId() {
		return claimAssignedCPDUserId;
	}

	public void setClaimAssignedCPDUserId(String claimAssignedCPDUserId) {
		this.claimAssignedCPDUserId = claimAssignedCPDUserId;
	}

	public String getClaimAssignedSNOUserId() {
		return claimAssignedSNOUserId;
	}

	public void setClaimAssignedSNOUserId(String claimAssignedSNOUserId) {
		this.claimAssignedSNOUserId = claimAssignedSNOUserId;
	}

	public String getDocAdminssionSlip() {
		return docAdminssionSlip;
	}

	public void setDocAdminssionSlip(String docAdminssionSlip) {
		this.docAdminssionSlip = docAdminssionSlip;
	}

	public String getDocTreatmentSlip() {
		return docTreatmentSlip;
	}

	public void setDocTreatmentSlip(String docTreatmentSlip) {
		this.docTreatmentSlip = docTreatmentSlip;
	}

	public String getDocHospitalBill() {
		return docHospitalBill;
	}

	public void setDocHospitalBill(String docHospitalBill) {
		this.docHospitalBill = docHospitalBill;
	}

	public String getDocDischargeSlip() {
		return docDischargeSlip;
	}

	public void setDocDischargeSlip(String docDischargeSlip) {
		this.docDischargeSlip = docDischargeSlip;
	}

	public String getDocInvestigation() {
		return docInvestigation;
	}

	public void setDocInvestigation(String docInvestigation) {
		this.docInvestigation = docInvestigation;
	}

	public String getPatientPresurgeryPhotoUploadedPath() {
		return patientPresurgeryPhotoUploadedPath;
	}

	public void setPatientPresurgeryPhotoUploadedPath(String patientPresurgeryPhotoUploadedPath) {
		this.patientPresurgeryPhotoUploadedPath = patientPresurgeryPhotoUploadedPath;
	}

	public String getPatientPostsurgeryPhotoUploadedPath() {
		return patientPostsurgeryPhotoUploadedPath;
	}

	public void setPatientPostsurgeryPhotoUploadedPath(String patientPostsurgeryPhotoUploadedPath) {
		this.patientPostsurgeryPhotoUploadedPath = patientPostsurgeryPhotoUploadedPath;
	}

	public String getDocPath1() {
		return docPath1;
	}

	public void setDocPath1(String docPath1) {
		this.docPath1 = docPath1;
	}

	public String getDocPath2() {
		return docPath2;
	}

	public void setDocPath2(String docPath2) {
		this.docPath2 = docPath2;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getHospitalBill() {
		return hospitalBill;
	}

	public void setHospitalBill(String hospitalBill) {
		this.hospitalBill = hospitalBill;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getPatientname() {
		return patientname;
	}

	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}

	public String getPackagecode() {
		return packagecode;
	}

	public void setPackagecode(String packagecode) {
		this.packagecode = packagecode;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public String getDateofadmission() {
		return dateofadmission;
	}

	public void setDateofadmission(String dateofadmission) {
		this.dateofadmission = dateofadmission;
	}

	public String getActionon() {
		return actionon;
	}

	public void setActionon(String actionon) {
		this.actionon = actionon;
	}

	public String getActionby() {
		return actionby;
	}

	public void setActionby(String actionby) {
		this.actionby = actionby;
	}

	public String getCLAIMAMOUNT() {
		return CLAIMAMOUNT;
	}

	public void setCLAIMAMOUNT(String cLAIMAMOUNT) {
		CLAIMAMOUNT = cLAIMAMOUNT;
	}

	private String actionon;
	private String actionby;
	private String CLAIMAMOUNT;
	private String CREATEDON;

	public String getCREATEDON() {
		return CREATEDON;
	}

	public void setCREATEDON(String cREATEDON) {
		CREATEDON = cREATEDON;
	}

	@Override
	public String toString() {
		return "CpdQueryToHospitalBean [claimId=" + claimId + ", transactionId=" + transactionId + ", transactionCode="
				+ transactionCode + ", urnNo=" + urnNo + ", hospitalCode=" + hospitalCode + ", pendingAt=" + pendingAt
				+ ", claimStatus=" + claimStatus + ", claimAssignedCPDUserId=" + claimAssignedCPDUserId
				+ ", claimAssignedSNOUserId=" + claimAssignedSNOUserId + ", docAdminssionSlip=" + docAdminssionSlip
				+ ", docTreatmentSlip=" + docTreatmentSlip + ", docHospitalBill=" + docHospitalBill
				+ ", docDischargeSlip=" + docDischargeSlip + ", docInvestigation=" + docInvestigation
				+ ", patientPresurgeryPhotoUploadedPath=" + patientPresurgeryPhotoUploadedPath
				+ ", patientPostsurgeryPhotoUploadedPath=" + patientPostsurgeryPhotoUploadedPath + ", docPath1="
				+ docPath1 + ", docPath2=" + docPath2 + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", updatedBy=" + updatedBy + ", updateDate=" + updateDate + ", packageCode=" + packageCode
				+ ", hospitalBill=" + hospitalBill + ", userid=" + userid + ", patientname=" + patientname
				+ ", packagecode=" + packagecode + ", packagename=" + packagename + ", dateofadmission="
				+ dateofadmission + ", snoapprovedamount=" + snoapprovedamount + ", cpdapprovedamount="
				+ cpdapprovedamount + ", updateon=" + updateon + ", claim_no=" + claim_no + ", cpdDaysleftString="
				+ cpdDaysleftString + ", cpdquerydateString=" + cpdquerydateString + ", remainingdayscpdquery="
				+ remainingdayscpdquery + ", Daysleft=" + Daysleft + ", invoiceno=" + invoiceno + ", dateofdischarge="
				+ dateofdischarge + ", hospitalName=" + hospitalName + ", caseno=" + caseno + ", remarks=" + remarks
				+ ", remark=" + remark + ", actualdateofadmission=" + actualdateofadmission + ", actualdateofdischarge="
				+ actualdateofdischarge + ", transactiondetailsid=" + transactiondetailsid + ", actionon=" + actionon
				+ ", actionby=" + actionby + ", CLAIMAMOUNT=" + CLAIMAMOUNT + ", CREATEDON=" + CREATEDON + "]";
	}
}
