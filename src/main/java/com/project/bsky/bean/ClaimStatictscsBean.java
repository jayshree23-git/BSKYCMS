package com.project.bsky.bean;

public class ClaimStatictscsBean {
	
	private Long claimid;
	private String claim_no;
	private String urn;
	private String packagename;
	private String hospitalname;
	private String actualdateofadmission;
	private String actualdateofdischarge;
	private String dateofadmission;
	private String dateofdischarge;
	private String authorizedcode;
	private String hospitalcode;
	private String fullname;
	private String invoiceno;

	public String getInvoiceno() {
		return invoiceno;
	}
	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}
	public Long getClaimid() {
		return claimid;
	}
	public void setClaimid(Long claimid) {
		this.claimid = claimid;
	}
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}
	public String getUrn() {
		return urn;
	}
	public void setUrn(String urn) {
		this.urn = urn;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getHospitalname() {
		return hospitalname;
	}
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
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
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	@Override
	public String toString() {
		return "ClaimStatictscsBean [claimid=" + claimid + ", claim_no=" + claim_no + ", urn=" + urn + ", packagename="
				+ packagename + ", hospitalname=" + hospitalname + ", actualdateofadmission=" + actualdateofadmission
				+ ", actualdateofdischarge=" + actualdateofdischarge + ", dateofadmission=" + dateofadmission
				+ ", dateofdischarge=" + dateofdischarge + ", authorizedcode=" + authorizedcode + ", hospitalcode="
				+ hospitalcode + ", fullname=" + fullname + ", invoiceno=" + invoiceno + "]";
	}



}
