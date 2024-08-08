/**
 * 
 */
package com.project.bsky.bean;

/**
 * @author rajendra.sahoo
 *
 */
public class SnaSysRejectedList {
	
	private String urn;
	private String snouserid;
	private String hospitalcode;
	private String hospitalname;
	private String patientname;
	private String packagename;
	private String packagecode;
	private String amountclaimed;
	private String claimraisedby;
	private String dischargedate;
	
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
	public String getClaim_raised_by() {
		return claimraisedby;
	}
	public void setClaim_raised_by(String claim_raised_by) {
		this.claimraisedby = claim_raised_by;
	}
	public String getDischargedate() {
		return dischargedate;
	}
	public void setDischargedate(String dischargedate) {
		this.dischargedate = dischargedate;
	}
	@Override
	public String toString() {
		return "SnaSysRejectedList [urn=" + urn + ", snouserid=" + snouserid + ", hospitalcode=" + hospitalcode
				+ ", hospitalname=" + hospitalname + ", patientname=" + patientname + ", packagename=" + packagename
				+ ", packagecode=" + packagecode + ", amountclaimed=" + amountclaimed + ", claim_raised_by="
				+ claimraisedby + ", dischargedate=" + dischargedate + "]";
	}
	
	

}
