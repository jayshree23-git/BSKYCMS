package com.project.bsky.bean;

public class OngoingtreatmentReportBean {
	private String patientname;
	private String hospitalCode;
	private String invoiceno;
	private String urn;
	private String actualDateOfAdmission;
	private String packagecode;
	private String packagename;
	private String hospitalname;
	private String totalhospitalname;
	private String totalpatient;

	public String getTotalhospitalname() {
		return totalhospitalname;
	}

	public void setTotalhospitalname(String totalhospitalname) {
		this.totalhospitalname = totalhospitalname;
	}

	public String getTotalpatient() {
		return totalpatient;
	}

	public void setTotalpatient(String totalpatient) {
		this.totalpatient = totalpatient;
	}

	public String getHospitalname() {
		return hospitalname;
	}

	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}

	public String getPatientname() {
		return patientname;
	}

	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public String getActualDateOfAdmission() {
		return actualDateOfAdmission;
	}

	public void setActualDateOfAdmission(String actualDateOfAdmission) {
		this.actualDateOfAdmission = actualDateOfAdmission;
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

	private String procedurename;

	public String getProcedurename() {
		return procedurename;
	}

	public void setProcedurename(String procedurename) {
		this.procedurename = procedurename;
	}

	@Override
	public String toString() {
		return "OngoingtreatmentReportBean [patientname=" + patientname + ", hospitalCode=" + hospitalCode
				+ ", invoiceno=" + invoiceno + ", urn=" + urn + ", actualDateOfAdmission=" + actualDateOfAdmission
				+ ", packagecode=" + packagecode + ", packagename=" + packagename + ", hospitalname=" + hospitalname
				+ ", totalhospitalname=" + totalhospitalname + ", totalpatient=" + totalpatient + ", procedurename="
				+ procedurename + "]";
	}

	


}
