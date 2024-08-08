package com.project.bsky.bean;

import java.util.Date;

import com.project.bsky.util.DateFormat;

import lombok.Data;

@Data
public class NonComplianceData {

	private Date claimRaisedBy;
	private String URN;
	private String PatientName;
	private String PackageCode;
	private String PackageName;
	private String currentTotalAmount;
	private long transactionDetailsId;
	private String hospitalCode;
	private String actualDateOfDischarge;
	private String actualDateOfAdmission;
	private boolean check;
	private String lastclaimby;
	private String currentclaimby;
	private String extendedon;
	private String extendedby;
	private String hcode;
	private String hname;
	private String statename;
	private String distname;
	private String claimno;
	
	public boolean isCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
	public Date getClaimRaisedBy() {
		return claimRaisedBy;
	}
	public void setClaimRaisedBy(Date claimRaisedBy) {
		this.claimRaisedBy = claimRaisedBy;
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
	public String getPackageName() {
		return PackageName;
	}
	public void setPackageName(String packageName) {
		PackageName = packageName;
	}
	public String getCurrentTotalAmount() {
		return currentTotalAmount;
	}
	public void setCurrentTotalAmount(String currentTotalAmount) {
		this.currentTotalAmount = currentTotalAmount;
	}
	public long getTransactionDetailsId() {
		return transactionDetailsId;
	}
	public void setTransactionDetailsId(long transactionDetailsId) {
		this.transactionDetailsId = transactionDetailsId;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
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
	@Override
	public String toString() {
		return "NonComplianceData [claimRaisedBy=" + claimRaisedBy + ", URN=" + URN + ", PatientName=" + PatientName
				+ ", PackageCode=" + PackageCode + ", PackageName=" + PackageName + ", currentTotalAmount="
				+ currentTotalAmount + ", transactionDetailsId=" + transactionDetailsId + ", hospitalCode="
				+ hospitalCode + ", actualDateOfDischarge=" + actualDateOfDischarge + ", actualDateOfAdmission="
				+ actualDateOfAdmission + "]";
	}
	
}
