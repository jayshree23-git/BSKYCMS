package com.project.bsky.bean;

public class OldClaimPymntBean {

	private Long transid;
	private String hospitalName;
	private String hospitalCode;
	private String urn;
	private String invoiceNumber;
	private String caseNo;
	private String patientName;
	private String packageCode;
	private String packageName;
	private String actualDateOfAdmission;
	private String actualDateOfDischarge;
	private String mortality;
	private String currentTotalAmount;
	private String snaClaimStatus;
	private String snaRemarks;
	private String snaApprovedAmount;

	public Long getTransid() {
		return transid;
	}

	public void setTransid(Long transid) {
		this.transid = transid;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getActualDateOfAdmission() {
		return actualDateOfAdmission;
	}

	public void setActualDateOfAdmission(String actualDateOfAdmission) {
		this.actualDateOfAdmission = actualDateOfAdmission;
	}

	public String getActualDateOfDischarge() {
		return actualDateOfDischarge;
	}

	public void setActualDateOfDischarge(String actualDateOfDischarge) {
		this.actualDateOfDischarge = actualDateOfDischarge;
	}

	public String getMortality() {
		return mortality;
	}

	public void setMortality(String mortality) {
		this.mortality = mortality;
	}

	public String getCurrentTotalAmount() {
		return currentTotalAmount;
	}

	public void setCurrentTotalAmount(String currentTotalAmount) {
		this.currentTotalAmount = currentTotalAmount;
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

	public String getSnaApprovedAmount() {
		return snaApprovedAmount;
	}

	public void setSnaApprovedAmount(String snaApprovedAmount) {
		this.snaApprovedAmount = snaApprovedAmount;
	}

	@Override
	public String toString() {
		return "OldClaimPymntBean [transid=" + transid + ", hospitalName=" + hospitalName + ", hospitalCode="
				+ hospitalCode + ", urn=" + urn + ", invoiceNumber=" + invoiceNumber + ", caseNo=" + caseNo
				+ ", patientName=" + patientName + ", packageCode=" + packageCode + ", packageName=" + packageName
				+ ", actualDateOfAdmission=" + actualDateOfAdmission + ", actualDateOfDischarge="
				+ actualDateOfDischarge + ", mortality=" + mortality + ", currentTotalAmount=" + currentTotalAmount
				+ ", snaClaimStatus=" + snaClaimStatus + ", snaRemarks=" + snaRemarks + ", snaApprovedAmount="
				+ snaApprovedAmount + "]";
	}

}
