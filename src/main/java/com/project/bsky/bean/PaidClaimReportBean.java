package com.project.bsky.bean;

import java.util.Date;

public class PaidClaimReportBean {
	
	private long transactiondetailsid;
	private long claimid;
	private String urn;
	private String hospitalname;

	public String getHospitalname() {
		return hospitalname;
	}
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}
	private String snapprovedamount;
	public String getSnapprovedamount() {
		return snapprovedamount;
	}
	public void setSnapprovedamount(String snapprovedamount) {
		this.snapprovedamount = snapprovedamount;
	}
	

	@Override
	public String toString() {
		return "PaidClaimReportBean [transactiondetailsid=" + transactiondetailsid + ", claimid=" + claimid + ", urn="
				+ urn + ", hospitalname=" + hospitalname + ", snapprovedamount=" + snapprovedamount + ", patientname="
				+ patientname + ", invoiceno=" + invoiceno + ", createdon=" + createdon + ", cpd_alloted_date="
				+ cpd_alloted_date + ", packagename=" + packagename + ", revised_date=" + revised_date
				+ ", packagecode=" + packagecode + ", totalamountclaimed=" + totalamountclaimed + ", claim_no="
				+ claim_no + ", PAYMENTFREEZESTATUS=" + PAYMENTFREEZESTATUS + ", mortality=" + mortality
				+ ", hospitalMortality=" + hospitalMortality + ", actualDateOfDischarge=" + actualDateOfDischarge
				+ ", actualDateOfAdmission=" + actualDateOfAdmission + ", hospitalCode=" + hospitalCode
				+ ", cpdmortality=" + cpdmortality + ", chequenumber=" + chequenumber + ", paymenttype=" + paymenttype
				+ ", bankname=" + bankname + "]";
	}
	public long getTransactiondetailsid() {
		return transactiondetailsid;
	}
	public void setTransactiondetailsid(long transactiondetailsid) {
		this.transactiondetailsid = transactiondetailsid;
	}
	public long getClaimid() {
		return claimid;
	}
	public void setClaimid(long claimid) {
		this.claimid = claimid;
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
	public String getCreatedon() {
		return createdon;
	}
	public void setCreatedon(String createdon) {
		this.createdon = createdon;
	}
	public Date getCpd_alloted_date() {
		return cpd_alloted_date;
	}
	public void setCpd_alloted_date(Date cpd_alloted_date) {
		this.cpd_alloted_date = cpd_alloted_date;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public Date getRevised_date() {
		return revised_date;
	}
	public void setRevised_date(Date revised_date) {
		this.revised_date = revised_date;
	}
	public String getPackagecode() {
		return packagecode;
	}
	public void setPackagecode(String packagecode) {
		this.packagecode = packagecode;
	}
	public String getTotalamountclaimed() {
		return totalamountclaimed;
	}
	public void setTotalamountclaimed(String totalamountclaimed) {
		this.totalamountclaimed = totalamountclaimed;
	}
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}
	public String getPAYMENTFREEZESTATUS() {
		return PAYMENTFREEZESTATUS;
	}
	public void setPAYMENTFREEZESTATUS(String pAYMENTFREEZESTATUS) {
		PAYMENTFREEZESTATUS = pAYMENTFREEZESTATUS;
	}
	public String getMortality() {
		return mortality;
	}
	public void setMortality(String mortality) {
		this.mortality = mortality;
	}
	public String getHospitalMortality() {
		return hospitalMortality;
	}
	public void setHospitalMortality(String hospitalMortality) {
		this.hospitalMortality = hospitalMortality;
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
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	private String patientname;
	private String invoiceno;
	private String createdon;
	private Date cpd_alloted_date;
	private String packagename;
	public String getCpdmortality() {
		return cpdmortality;
	}
	public void setCpdmortality(String cpdmortality) {
		this.cpdmortality = cpdmortality;
	}
	private Date revised_date;
	private String packagecode;
	private String totalamountclaimed;
	private String claim_no;
	private String PAYMENTFREEZESTATUS;
	private String mortality;
	private String hospitalMortality;
	private String actualDateOfDischarge;
	private String actualDateOfAdmission;
	private String hospitalCode;
	private String cpdmortality;
	private String chequenumber;

	private String paymenttype;

	private String bankname;

	public String getChequenumber() {
		return chequenumber;
	}
	public void setChequenumber(String chequenumber) {
		this.chequenumber = chequenumber;
	}
	public String getPaymenttype() {
		return paymenttype;
	}
	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}



}
