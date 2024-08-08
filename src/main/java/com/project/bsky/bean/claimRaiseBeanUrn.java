package com.project.bsky.bean;

public class claimRaiseBeanUrn {
	
	private long ID;
	private long transactionid;
	private String URN;
	private int age;
	private String gender;
	private String PatientName;
	private String PackageCode;
	private String PackageName;
	private long CurrentTotalAmount;
	private String DateOfDischarge;
	private Integer userid;
	private Integer hospitalstateCode;
	private String dateofadmission;
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
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
	public String getDateOfDischarge() {
		return DateOfDischarge;
	}
	public void setDateOfDischarge(String dateOfDischarge) {
		DateOfDischarge = dateOfDischarge;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Integer getHospitalstateCode() {
		return hospitalstateCode;
	}
	public void setHospitalstateCode(Integer hospitalstateCode) {
		this.hospitalstateCode = hospitalstateCode;
	}
	public String getDateofadmission() {
		return dateofadmission;
	}
	public void setDateofadmission(String dateofadmission) {
		this.dateofadmission = dateofadmission;
	}
	@Override
	public String toString() {
		return "claimRaiseBeanUrn [ID=" + ID + ", transactionid=" + transactionid + ", URN=" + URN + ", age=" + age
				+ ", gender=" + gender + ", PatientName=" + PatientName + ", PackageCode=" + PackageCode
				+ ", PackageName=" + PackageName + ", CurrentTotalAmount=" + CurrentTotalAmount + ", DateOfDischarge="
				+ DateOfDischarge + ", userid=" + userid + ", hospitalstateCode=" + hospitalstateCode
				+ ", dateofadmission=" + dateofadmission + "]";
	}
	
	
	
	

}
