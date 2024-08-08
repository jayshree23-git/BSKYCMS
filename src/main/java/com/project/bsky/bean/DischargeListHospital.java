package com.project.bsky.bean;

public class DischargeListHospital {
	
	private String urn;
	private String patientname;
	private String invoiceno;
	private String packagecode;
	private String packagename;
	private String actualdateofdischarge;
	private String claim_raised_by;
	private String caseno;
	private String claimno;
	private String hopitalclmcaseno;
	private long currenttotalamount;
	private String discharegedate;
	private String claimbilldate;
	
	public String getDischaregedate() {
		return discharegedate;
	}
	public void setDischaregedate(String discharegedate) {
		this.discharegedate = discharegedate;
	}
	private String authorizedcode;
	private String hospitalcode;
	
	
	
	public String getAuthorizedcode() {
		return authorizedcode;
	}
	public void setAuthorizedcode(String authorizedcode) {
		this.authorizedcode = authorizedcode;
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
	private String claimraisestatus;
	private String hospitalname;
	private String transactiondetailsid;

	
	public String getHospitalname() {
		return hospitalname;
	}
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}
	public String getUrn() {
		return urn;
	}
	public void setUrn(String urn) {
		this.urn = urn;
	}
	public String getCaseno() {
		return caseno;
	}
	public void setCaseno(String caseno) {
		this.caseno = caseno;
	}
	public String getClaimno() {
		return claimno;
	}
	public void setClaimno(String claimno) {
		this.claimno = claimno;
	}
	public String getPatientname() {
		return patientname;
	}
	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}
	public String getInvoiceno() {
		return invoiceno;
	}
	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
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
	public String getActualdateofdischarge() {
		return actualdateofdischarge;
	}
	public void setActualdateofdischarge(String actualdateofdischarge) {
		this.actualdateofdischarge = actualdateofdischarge;
	}
	public String getClaim_raised_by() {
		return claim_raised_by;
	}
	public void setClaim_raised_by(String claim_raised_by) {
		this.claim_raised_by = claim_raised_by;
	}
	public long getCurrenttotalamount() {
		return currenttotalamount;
	}
	public void setCurrenttotalamount(long currenttotalamount) {
		this.currenttotalamount = currenttotalamount;
	}
	public String getClaimraisestatus() {
		return claimraisestatus;
	}
	public void setClaimraisestatus(String claimraisestatus) {
		this.claimraisestatus = claimraisestatus;
	}
	
	public String getHopitalclmcaseno() {
		return hopitalclmcaseno;
	}
	public void setHopitalclmcaseno(String hopitalclmcaseno) {
		this.hopitalclmcaseno = hopitalclmcaseno;
	}
	
	public String getClaimbilldate() {
		return claimbilldate;
	}
	public void setClaimbilldate(String claimbilldate) {
		this.claimbilldate = claimbilldate;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DischargeListHospital [urn=");
		builder.append(urn);
		builder.append(", patientname=");
		builder.append(patientname);
		builder.append(", invoiceno=");
		builder.append(invoiceno);
		builder.append(", packagecode=");
		builder.append(packagecode);
		builder.append(", packagename=");
		builder.append(packagename);
		builder.append(", actualdateofdischarge=");
		builder.append(actualdateofdischarge);
		builder.append(", claim_raised_by=");
		builder.append(claim_raised_by);
		builder.append(", caseno=");
		builder.append(caseno);
		builder.append(", claimno=");
		builder.append(claimno);
		builder.append(", hopitalclmcaseno=");
		builder.append(hopitalclmcaseno);
		builder.append(", currenttotalamount=");
		builder.append(currenttotalamount);
		builder.append(", discharegedate=");
		builder.append(discharegedate);
		builder.append(", claimbilldate=");
		builder.append(claimbilldate);
		builder.append(", authorizedcode=");
		builder.append(authorizedcode);
		builder.append(", hospitalcode=");
		builder.append(hospitalcode);
		builder.append(", claimraisestatus=");
		builder.append(claimraisestatus);
		builder.append(", hospitalname=");
		builder.append(hospitalname);
		builder.append(", transactiondetailsid=");
		builder.append(transactiondetailsid);
		builder.append("]");
		return builder.toString();
	}

	
	
}
