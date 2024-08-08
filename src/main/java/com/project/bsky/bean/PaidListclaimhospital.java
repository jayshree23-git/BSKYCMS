package com.project.bsky.bean;

public class PaidListclaimhospital {
	private String urn;
	private String claim_no;
	private String patientname;
	private String packagename;
	private long SNOAPPROVEDAMOUNT;
	private String ACTUALDATEOFADMISSION;
	private String ACTUALDATEOFDISCHARGE;
	private String caseno;
	
	public String getUrn() {
		return urn;
	}
	public void setUrn(String urn) {
		this.urn = urn;
	}
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
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
	public long getSNOAPPROVEDAMOUNT() {
		return SNOAPPROVEDAMOUNT;
	}
	public void setSNOAPPROVEDAMOUNT(long sNOAPPROVEDAMOUNT) {
		SNOAPPROVEDAMOUNT = sNOAPPROVEDAMOUNT;
	}
	public String getACTUALDATEOFADMISSION() {
		return ACTUALDATEOFADMISSION;
	}
	public void setACTUALDATEOFADMISSION(String aCTUALDATEOFADMISSION) {
		ACTUALDATEOFADMISSION = aCTUALDATEOFADMISSION;
	}
	public String getACTUALDATEOFDISCHARGE() {
		return ACTUALDATEOFDISCHARGE;
	}
	public void setACTUALDATEOFDISCHARGE(String aCTUALDATEOFDISCHARGE) {
		ACTUALDATEOFDISCHARGE = aCTUALDATEOFDISCHARGE;
	}
	
	public String getCaseno() {
		return caseno;
	}
	public void setCaseno(String caseno) {
		this.caseno = caseno;
	}
	@Override
	public String toString() {
		return "PaidListclaimhospital [urn=" + urn + ", claim_no=" + claim_no + ", patientname=" + patientname
				+ ", packagename=" + packagename + ", SNOAPPROVEDAMOUNT=" + SNOAPPROVEDAMOUNT
				+ ", ACTUALDATEOFADMISSION=" + ACTUALDATEOFADMISSION + ", ACTUALDATEOFDISCHARGE="
				+ ACTUALDATEOFDISCHARGE + ", caseno=" + caseno + "]";
	}
	


}
