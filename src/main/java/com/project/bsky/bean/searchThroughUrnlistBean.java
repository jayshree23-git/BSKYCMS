package com.project.bsky.bean;

public class searchThroughUrnlistBean {
	private long ID;
	private long transactionid;
	private String URN;
	private String age;
	private String gender;
	private String PatientName;
	private String PackageCode;
	private String PackageName;
	private long CurrentTotalAmount;
	private String dateofadmission;
	private String DateOfDischarge;
	private long hospitalstateCode;
	private String actualdateofdischarge;
	private String actualdateofadmission;
	private String claimraisestatus;
	private String hospitalname;
	private String hospitalcode;
	private String authorizedcode;
	public String getHospitalcode() {
		return hospitalcode;
	}
	public void setHospitalcode(String hospitalcode) {
		this.hospitalcode = hospitalcode;
	}
	public String getAuthorizedcode() {
		return authorizedcode;
	}
	public void setAuthorizedcode(String authorizedcode) {
		this.authorizedcode = authorizedcode;
	}
	public String getTransactiondetailsid() {
		return transactiondetailsid;
	}
	public void setTransactiondetailsid(String transactiondetailsid) {
		this.transactiondetailsid = transactiondetailsid;
	}
	private String transactiondetailsid;

	
	
	
	
	
	public String getHospitalname() {
		return hospitalname;
	}
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public long getTransactionid() {
		return transactionid;
	}
	public void setTransactionid(long transactionid) {
		this.transactionid = transactionid;
	}
	public String getURN() {
		return URN;
	}
	public void setURN(String uRN) {
		URN = uRN;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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
	public long getCurrentTotalAmount() {
		return CurrentTotalAmount;
	}
	public void setCurrentTotalAmount(long currentTotalAmount) {
		CurrentTotalAmount = currentTotalAmount;
	}
	public String getDateofadmission() {
		return dateofadmission;
	}
	public void setDateofadmission(String dateofadmission) {
		this.dateofadmission = dateofadmission;
	}
	public String getDateOfDischarge() {
		return DateOfDischarge;
	}
	public void setDateOfDischarge(String dateOfDischarge) {
		DateOfDischarge = dateOfDischarge;
	}
	public long getHospitalstateCode() {
		return hospitalstateCode;
	}
	public void setHospitalstateCode(long hospitalstateCode) {
		this.hospitalstateCode = hospitalstateCode;
	}
	public String getActualdateofdischarge() {
		return actualdateofdischarge;
	}
	public void setActualdateofdischarge(String actualdateofdischarge) {
		this.actualdateofdischarge = actualdateofdischarge;
	}
	public String getActualdateofadmission() {
		return actualdateofadmission;
	}
	public void setActualdateofadmission(String actualdateofadmission) {
		this.actualdateofadmission = actualdateofadmission;
	}
	public String getClaimraisestatus() {
		return claimraisestatus;
	}
	public void setClaimraisestatus(String claimraisestatus) {
		this.claimraisestatus = claimraisestatus;
	}
	@Override
	public String toString() {
		return "searchThroughUrnlistBean [ID=" + ID + ", transactionid=" + transactionid + ", URN=" + URN + ", age="
				+ age + ", gender=" + gender + ", PatientName=" + PatientName + ", PackageCode=" + PackageCode
				+ ", PackageName=" + PackageName + ", CurrentTotalAmount=" + CurrentTotalAmount + ", dateofadmission="
				+ dateofadmission + ", DateOfDischarge=" + DateOfDischarge + ", hospitalstateCode=" + hospitalstateCode
				+ ", actualdateofdischarge=" + actualdateofdischarge + ", actualdateofadmission="
				+ actualdateofadmission + ", claimraisestatus=" + claimraisestatus + ", hospitalname=" + hospitalname
				+ ", hospitalcode=" + hospitalcode + ", authorizedcode=" + authorizedcode + ", transactiondetailsid="
				+ transactiondetailsid + "]";
	}



}
