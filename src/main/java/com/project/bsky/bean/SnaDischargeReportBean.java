package com.project.bsky.bean;

public class SnaDischargeReportBean {
	private String urn;
	private String snouserid;
	private String hospitalcode;
	private String hospitalname;
	private String patientname;
	private String packagename;
	private String claimraisestatus;
	private String authorizedcode;
	private String claimno;
	private String invoiceno;




	
	
	
	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getClaimno() {
		return claimno;
	}

	public void setClaimno(String claimno) {
		this.claimno = claimno;
	}

	public String getAuthorizedcode() {
		return authorizedcode;
	}

	public void setAuthorizedcode(String authorizedcode) {
		this.authorizedcode = authorizedcode;
	}

	public String getClaimraisestatus() {
		return claimraisestatus;
	}
	
	

	
	@Override
	public String toString() {
		return "SnaDischargeReportBean [urn=" + urn + ", snouserid=" + snouserid + ", hospitalcode=" + hospitalcode
				+ ", hospitalname=" + hospitalname + ", patientname=" + patientname + ", packagename=" + packagename
				+ ", claimraisestatus=" + claimraisestatus + ", authorizedcode=" + authorizedcode + ", claimno="
				+ claimno + ", packagecode=" + packagecode + ", amountclaimed=" + amountclaimed + ", dischargedate="
				+ dischargedate + ", admissiondate=" + admissiondate + ", transactionalid=" + transactionalid + "]";
	}

	public void setClaimraisestatus(String claimraisestatus) {
		this.claimraisestatus = claimraisestatus;
	}
	public String getUrn() {
		return urn;
	}
	public void setUrn(String urn) {
		this.urn = urn;
	}
	public String getSnouserid() {
		return snouserid;
	}
	public void setSnouserid(String snouserid) {
		this.snouserid = snouserid;
	}
	public String getHospitalcode() {
		return hospitalcode;
	}
	public void setHospitalcode(String hospitalcode) {
		this.hospitalcode = hospitalcode;
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
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getPackagecode() {
		return packagecode;
	}
	public void setPackagecode(String packagecode) {
		this.packagecode = packagecode;
	}
	public String getAmountclaimed() {
		return amountclaimed;
	}
	public void setAmountclaimed(String amountclaimed) {
		this.amountclaimed = amountclaimed;
	}
	
	
	
	public String getDischargedate() {
		return dischargedate;
	}
	public void setDischargedate(String dischargedate) {
		this.dischargedate = dischargedate;
	}
	public String getAdmissiondate() {
		return admissiondate;
	}
	public void setAdmissiondate(String admissiondate) {
		this.admissiondate = admissiondate;
	}
	private String packagecode;
	private String amountclaimed;
	private String dischargedate;
	private String admissiondate;
	private Long transactionalid;




	public Long getTransactionalid() {
		return transactionalid;
	}
	public void setTransactionalid(Long transactionalid) {
		this.transactionalid = transactionalid;
	}

}
