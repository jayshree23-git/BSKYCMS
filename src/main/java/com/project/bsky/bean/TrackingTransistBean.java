package com.project.bsky.bean;

import java.util.Date;

public class TrackingTransistBean {
	private String userid;
	private Date fromDate;
	private Date toDate;
	private long claimid;
	private long transactionDetailsId;
	private String URN;
	private String PatientName;
	private String PackageCode;
	private String currentTotalAmount;
	private String CreatedOn;
	private String PackageName;
	private String dateofadmission;
	private String dateofdischarge;
	public String getAuthorizedcode() {
		return authorizedcode;
	}
	public void setAuthorizedcode(String authorizedcode) {
		this.authorizedcode = authorizedcode;
	}
	private String hospitalcode;
	private String authorizedcode;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
	public String getCurrentTotalAmount() {
		return currentTotalAmount;
	}
	public void setCurrentTotalAmount(String currentTotalAmount) {
		this.currentTotalAmount = currentTotalAmount;
	}
	public String getCreatedOn() {
		return CreatedOn;
	}
	public void setCreatedOn(String createdOn) {
		CreatedOn = createdOn;
	}
	public String getPackageName() {
		return PackageName;
	}
	public void setPackageName(String packageName) {
		PackageName = packageName;
	}
	public String getDateofadmission() {
		return dateofadmission;
	}
	public void setDateofadmission(String dateofadmission) {
		this.dateofadmission = dateofadmission;
	}
	public String getDateofdischarge() {
		return dateofdischarge;
	}
	public void setDateofdischarge(String dateofdischarge) {
		this.dateofdischarge = dateofdischarge;
	}
	public String getHospitalcode() {
		return hospitalcode;
	}
	public void setHospitalcode(String hospitalcode) {
		this.hospitalcode = hospitalcode;
	}
	@Override
	public String toString() {
		return "TrackingTransistBean [userid=" + userid + ", claimid=" + claimid + ", transactionDetailsId="
				+ transactionDetailsId + ", URN=" + URN + ", PatientName=" + PatientName + ", PackageCode="
				+ PackageCode + ", currentTotalAmount=" + currentTotalAmount + ", CreatedOn=" + CreatedOn
				+ ", PackageName=" + PackageName + ", dateofadmission=" + dateofadmission + ", dateofdischarge="
				+ dateofdischarge + ", hospitalcode=" + hospitalcode + "]";
	}


}
