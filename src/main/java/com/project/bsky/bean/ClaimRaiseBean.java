package com.project.bsky.bean;

import java.io.Serializable;

public class ClaimRaiseBean implements Serializable {
	private long ID;
	private long transactionid;
	private String URN;
	private String PatientName;
	private String PackageCode;
	private String PackageName;
	private String procedurename;

	public String getProcedurename() {
		return procedurename;
	}

	public void setProcedurename(String procedurename) {
		this.procedurename = procedurename;
	}

	private String CurrentTotalAmount;
	private String DateOfDischarge;
	private Integer userid;
	private Integer hospitalstateCode;
	private String dateofadmission;
	private String remainingDateString;
	private String transactiondetailsid;
	private String hospitalcode;
	private String invoiceno;
    private String claimRaiseby;
    private String authorizedcode;
    private String actualdateofdischarge;
    private String actualdateofadmission;
    private String HospitalName;
    private String caseno;
    private String Preauthdocs;
    private String Claimdocs;
    private String oldCLaimStatus;
    

    
	public String getOldCLaimStatus() {
		return oldCLaimStatus;
	}

	public void setOldCLaimStatus(String oldCLaimStatus) {
		this.oldCLaimStatus = oldCLaimStatus;
	}

	public String getPreauthdocs() {
		return Preauthdocs;
	}

	public void setPreauthdocs(String preauthdocs) {
		Preauthdocs = preauthdocs;
	}

	public String getClaimdocs() {
		return Claimdocs;
	}

	public void setClaimdocs(String claimdocs) {
		Claimdocs = claimdocs;
	}

	public String getHospitalName() {
		return HospitalName;
	}

	public void setHospitalName(String hospitalName) {
		HospitalName = hospitalName;
	}

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

	public String getAuthorizedcode() {
		return authorizedcode;
	}

	public void setAuthorizedcode(String authorizedcode) {
		this.authorizedcode = authorizedcode;
	}

	public String getClaimRaiseby() {
		return claimRaiseby;
	}

	public void setClaimRaiseby(String claimRaiseby) {
		this.claimRaiseby = claimRaiseby;
	}

	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getHospitalcode() {
		return hospitalcode;
	}

	public void setHospitalcode(String hospitalcode) {
		this.hospitalcode = hospitalcode;
	}

	public String getTransactiondetailsid() {
		return transactiondetailsid;
	}

	public void setTransactiondetailsid(String transactiondetailsid) {
		this.transactiondetailsid = transactiondetailsid;
	}

	public String getRemainingDateString() {
		return remainingDateString;
	}

	public void setRemainingDateString(String remainingDateString) {
		this.remainingDateString = remainingDateString;
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

	public void setURN(String string) {
		URN = string;
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
		return CurrentTotalAmount;
	}

	public void setCurrentTotalAmount(String currentTotalAmount) {
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

	public String getCaseno() {
		return caseno;
	}

	public void setCaseno(String caseno) {
		this.caseno = caseno;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ClaimRaiseBean [ID=");
		builder.append(ID);
		builder.append(", transactionid=");
		builder.append(transactionid);
		builder.append(", URN=");
		builder.append(URN);
		builder.append(", PatientName=");
		builder.append(PatientName);
		builder.append(", PackageCode=");
		builder.append(PackageCode);
		builder.append(", PackageName=");
		builder.append(PackageName);
		builder.append(", procedurename=");
		builder.append(procedurename);
		builder.append(", CurrentTotalAmount=");
		builder.append(CurrentTotalAmount);
		builder.append(", DateOfDischarge=");
		builder.append(DateOfDischarge);
		builder.append(", userid=");
		builder.append(userid);
		builder.append(", hospitalstateCode=");
		builder.append(hospitalstateCode);
		builder.append(", dateofadmission=");
		builder.append(dateofadmission);
		builder.append(", remainingDateString=");
		builder.append(remainingDateString);
		builder.append(", transactiondetailsid=");
		builder.append(transactiondetailsid);
		builder.append(", hospitalcode=");
		builder.append(hospitalcode);
		builder.append(", invoiceno=");
		builder.append(invoiceno);
		builder.append(", claimRaiseby=");
		builder.append(claimRaiseby);
		builder.append(", authorizedcode=");
		builder.append(authorizedcode);
		builder.append(", actualdateofdischarge=");
		builder.append(actualdateofdischarge);
		builder.append(", actualdateofadmission=");
		builder.append(actualdateofadmission);
		builder.append(", HospitalName=");
		builder.append(HospitalName);
		builder.append(", caseno=");
		builder.append(caseno);
		builder.append(", Preauthdocs=");
		builder.append(Preauthdocs);
		builder.append(", Claimdocs=");
		builder.append(Claimdocs);
		builder.append(", OldClaimStatus=");
		builder.append(oldCLaimStatus);
		builder.append("]");
		return builder.toString();
	}
}
