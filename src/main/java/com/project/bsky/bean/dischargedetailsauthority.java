package com.project.bsky.bean;

import java.io.Serializable;

import org.hibernate.boot.model.relational.SqlStringGenerationContext;

public class dischargedetailsauthority implements Serializable{
	
	private String urn;
	private String patientname;
	private String invoiceno;
	private String packagecode;
	private String packagename;
	private String dateofdischarge;
	private String actualdateofdischarge;
	private String claim_raised_by;
	private long currenttotalamount;
	private String claimraisestatus;
	private String HOSPITALNAME;
	private String  TRANSACTIONDETAILSID;
	private String AUTHORIZEDCODE;
	private String hospitalcode;
	public String getTRANSACTIONDETAILSID() {
		return TRANSACTIONDETAILSID;
	}
	public void setTRANSACTIONDETAILSID(String tRANSACTIONDETAILSID) {
		TRANSACTIONDETAILSID = tRANSACTIONDETAILSID;
	}
	public String getAUTHORIZEDCODE() {
		return AUTHORIZEDCODE;
	}
	public void setAUTHORIZEDCODE(String aUTHORIZEDCODE) {
		AUTHORIZEDCODE = aUTHORIZEDCODE;
	}
	public String getHospitalcode() {
		return hospitalcode;
	}
	public void setHospitalcode(String hospitalcode) {
		this.hospitalcode = hospitalcode;
	}
	public String getUrn() {
		return urn;
	}
	public void setUrn(String urn) {
		this.urn = urn;
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
	public String getDateofdischarge() {
		return dateofdischarge;
	}
	public void setDateofdischarge(String dateofdischarge) {
		this.dateofdischarge = dateofdischarge;
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
	public String getHOSPITALNAME() {
		return HOSPITALNAME;
	}
	public void setHOSPITALNAME(String hOSPITALNAME) {
		HOSPITALNAME = hOSPITALNAME;
	}
	@Override
	public String toString() {
		return "dischargedetailsauthority [urn=" + urn + ", patientname=" + patientname + ", invoiceno=" + invoiceno
				+ ", packagecode=" + packagecode + ", packagename=" + packagename + ", dateofdischarge="
				+ dateofdischarge + ", actualdateofdischarge=" + actualdateofdischarge + ", claim_raised_by="
				+ claim_raised_by + ", currenttotalamount=" + currenttotalamount + ", claimraisestatus="
				+ claimraisestatus + ", HOSPITALNAME=" + HOSPITALNAME + ", TRANSACTIONDETAILSID=" + TRANSACTIONDETAILSID
				+ ", AUTHORIZEDCODE=" + AUTHORIZEDCODE + ", hospitalcode=" + hospitalcode + "]";
	}

	
	
	
	
	
	
	
	
	
}
