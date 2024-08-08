package com.project.bsky.bean;

import java.util.Date;

public class SnoClaimDetails {

	private long claimid;
	private long transactionDetailsId;
	private String URN;
	private String PatientName;
	private String PackageCode;
	private String invoiceNumber;
	private String currentTotalAmount;
	private String CreatedOn;
	private String PackageName;
	private Date revisedDate;
	private Date cpdAlotteddate;
	private String claimNo;
	private String cpdApprovedAmount;
	private String hospitalName;
	private String mortality;
	private String hospitalMortality;
	private String actualDateOfDischarge;
	private String actualDateOfAdmission;
	private String actualDateOfDischarge1;
	private String actualDateOfAdmission1;
	private String hospitalCode;
	private String snaApprovedAmount;
	private Long actualCpdAmount;
	private Long actualSnaAmount;
	private String cpdClaimStatus;
	private String cpdRemarks;
	private String snaClaimStatus;
	private String snaRemarks;
	private String phone;
	private Long VerificationMode;
	private Long txnpackagedetailid;
	private String claimraisedby;
	private String nabhFlag;
	private String mandestatusflag;
	private Long triggerValue;
	private String triggerMsg;
	private String categoryName;

	public Long getVerificationMode() {
		return VerificationMode;
	}

	public void setVerificationMode(Long verificationMode) {
		VerificationMode = verificationMode;
	}

	public Long getTxnpackagedetailid() {
		return txnpackagedetailid;
	}

	public void setTxnpackagedetailid(Long txnpackagedetailid) {
		this.txnpackagedetailid = txnpackagedetailid;
	}

	private String caseNo;

	public String getCpdClaimStatus() {
		return cpdClaimStatus;
	}

	public String getClaimraisedby() {
		return claimraisedby;
	}

	public void setClaimraisedby(String claimraisedby) {
		this.claimraisedby = claimraisedby;
	}

	public void setCpdClaimStatus(String cpdClaimStatus) {
		this.cpdClaimStatus = cpdClaimStatus;
	}

	public String getCpdRemarks() {
		return cpdRemarks;
	}

	public void setCpdRemarks(String cpdRemarks) {
		this.cpdRemarks = cpdRemarks;
	}

	public String getSnaClaimStatus() {
		return snaClaimStatus;
	}

	public void setSnaClaimStatus(String snaClaimStatus) {
		this.snaClaimStatus = snaClaimStatus;
	}

	public String getSnaRemarks() {
		return snaRemarks;
	}

	public void setSnaRemarks(String snaRemarks) {
		this.snaRemarks = snaRemarks;
	}

	public String getPackageName() {
		return PackageName;
	}

	public void setPackageName(String packageName) {
		PackageName = packageName;
	}

	public long getClaimid() {
		return claimid;
	}

	public void setClaimid(long claimid) {
		this.claimid = claimid;
	}

	public long getTransactionDetailsId() {
		return transactionDetailsId;
	}

	public void setTransactionDetailsId(long transactionDetailsId) {
		this.transactionDetailsId = transactionDetailsId;
	}

	public String getURN() {
		return URN;
	}

	public void setURN(String uRN) {
		URN = uRN;
	}

	public String getPatientName() {
		return PatientName;
	}

	public void setPatientName(String patientName) {
		PatientName = patientName;
	}

	public String getPackageCode() {
		return PackageCode;
	}

	public void setPackageCode(String packageCode) {
		PackageCode = packageCode;
	}

	public String getCreatedOn() {
		return CreatedOn;
	}

	public void setCreatedOn(String createdOn) {
		CreatedOn = createdOn;
	}

	public Date getRevisedDate() {
		return revisedDate;
	}

	public void setRevisedDate(Date revisedDate) {
		this.revisedDate = revisedDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getCpdAlotteddate() {
		return cpdAlotteddate;
	}

	public void setCpdAlotteddate(Date cpdAlotteddate) {
		this.cpdAlotteddate = cpdAlotteddate;
	}

	public String getCurrentTotalAmount() {
		return currentTotalAmount;
	}

	public void setCurrentTotalAmount(String currentTotalAmount) {
		this.currentTotalAmount = currentTotalAmount;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getCpdApprovedAmount() {
		return cpdApprovedAmount;
	}

	public void setCpdApprovedAmount(String cpdApprovedAmount) {
		this.cpdApprovedAmount = cpdApprovedAmount;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getMortality() {
		return mortality;
	}

	public void setMortality(String mortality) {
		this.mortality = mortality;
	}

	public String getHospitalMortality() {
		return hospitalMortality;
	}

	public void setHospitalMortality(String hospitalMortality) {
		this.hospitalMortality = hospitalMortality;
	}

	public String getActualDateOfDischarge() {
		return actualDateOfDischarge;
	}

	public void setActualDateOfDischarge(String actualDateOfDischarge) {
		this.actualDateOfDischarge = actualDateOfDischarge;
	}

	public String getActualDateOfAdmission() {
		return actualDateOfAdmission;
	}

	public void setActualDateOfAdmission(String actualDateOfAdmission) {
		this.actualDateOfAdmission = actualDateOfAdmission;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getSnaApprovedAmount() {
		return snaApprovedAmount;
	}

	public void setSnaApprovedAmount(String snaApprovedAmount) {
		this.snaApprovedAmount = snaApprovedAmount;
	}

	public Long getActualCpdAmount() {
		return actualCpdAmount;
	}

	public void setActualCpdAmount(Long actualCpdAmount) {
		this.actualCpdAmount = actualCpdAmount;
	}

	public Long getActualSnaAmount() {
		return actualSnaAmount;
	}

	public void setActualSnaAmount(Long actualSnaAmount) {
		this.actualSnaAmount = actualSnaAmount;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getNabhFlag() {
		return nabhFlag;
	}

	public void setNabhFlag(String nabhFlag) {
		this.nabhFlag = nabhFlag;
	}

	public String getActualDateOfDischarge1() {
		return actualDateOfDischarge1;
	}

	public void setActualDateOfDischarge1(String actualDateOfDischarge1) {
		this.actualDateOfDischarge1 = actualDateOfDischarge1;
	}

	public String getActualDateOfAdmission1() {
		return actualDateOfAdmission1;
	}

	public void setActualDateOfAdmission1(String actualDateOfAdmission1) {
		this.actualDateOfAdmission1 = actualDateOfAdmission1;
	}

	public String getMandestatusflag() {
		return mandestatusflag;
	}

	public void setMandestatusflag(String mandestatusflag) {
		this.mandestatusflag = mandestatusflag;
	}

	public Long getTriggerValue() {
		return triggerValue;
	}

	public void setTriggerValue(Long triggerValue) {
		this.triggerValue = triggerValue;
	}

	public String getTriggerMsg() {
		return triggerMsg;
	}

	public void setTriggerMsg(String triggerMsg) {
		this.triggerMsg = triggerMsg;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "SnoClaimDetails [claimid=" + claimid + ", transactionDetailsId=" + transactionDetailsId + ", URN=" + URN
				+ ", PatientName=" + PatientName + ", PackageCode=" + PackageCode + ", invoiceNumber=" + invoiceNumber
				+ ", currentTotalAmount=" + currentTotalAmount + ", CreatedOn=" + CreatedOn + ", PackageName="
				+ PackageName + ", revisedDate=" + revisedDate + ", cpdAlotteddate=" + cpdAlotteddate + ", claimNo="
				+ claimNo + ", cpdApprovedAmount=" + cpdApprovedAmount + ", hospitalName=" + hospitalName
				+ ", mortality=" + mortality + ", hospitalMortality=" + hospitalMortality + ", actualDateOfDischarge="
				+ actualDateOfDischarge + ", actualDateOfAdmission=" + actualDateOfAdmission
				+ ", actualDateOfDischarge1=" + actualDateOfDischarge1 + ", actualDateOfAdmission1="
				+ actualDateOfAdmission1 + ", hospitalCode=" + hospitalCode + ", snaApprovedAmount=" + snaApprovedAmount
				+ ", actualCpdAmount=" + actualCpdAmount + ", actualSnaAmount=" + actualSnaAmount + ", cpdClaimStatus="
				+ cpdClaimStatus + ", cpdRemarks=" + cpdRemarks + ", snaClaimStatus=" + snaClaimStatus + ", snaRemarks="
				+ snaRemarks + ", phone=" + phone + ", VerificationMode=" + VerificationMode + ", txnpackagedetailid="
				+ txnpackagedetailid + ", claimraisedby=" + claimraisedby + ", nabhFlag=" + nabhFlag
				+ ", mandestatusflag=" + mandestatusflag + ", triggerValue=" + triggerValue + ", triggerMsg="
				+ triggerMsg + ", categoryName=" + categoryName + ", caseNo=" + caseNo + "]";
	}

}
