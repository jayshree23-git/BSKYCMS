package com.project.bsky.bean;

import java.util.List;

import lombok.Data;

@Data
public class Cpdrejecteddetailsbean {

	private long claimid;
	private String URN;
	private String PatientName;
	private String PackageCode;
	private String CreatedOn;
	private Integer assignedsno;
	private String CURRENTTOTALAMOUNT;
	private Long TRANSACTIONDETAILSID;
	private String stateCode;
	private String districtCode;
	private List<Hospital> hospitalCode;
	private String snoName;
	private String stateName;
	private String districtName;
	private String hospitalName;
	private String CPD_ALLOTED_DATE;
	private String invoiceno;
	private String pid;
	private String claimNo;
	private String packageName;
	private String mortality;
	private String hospitalMortality;
	private String actualDateOfDischarge;
	private String actualDateOfAdmission;
	private String hospital;
	private String phone;
	private Long VerificationMode;
	private Long txnpackagedetailid;
	private Long triggerValue;
	private String triggerMsg;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getCPD_ALLOTED_DATE() {
		return CPD_ALLOTED_DATE;
	}

	public void setCPD_ALLOTED_DATE(String cPD_ALLOTED_DATE) {
		CPD_ALLOTED_DATE = cPD_ALLOTED_DATE;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public List<Hospital> getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(List<Hospital> hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getSnoName() {
		return snoName;
	}

	public void setSnoName(String snoName) {
		this.snoName = snoName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public long getClaimid() {
		return claimid;
	}

	public void setClaimid(long claimid) {
		this.claimid = claimid;
	}

	public String getURN() {
		return URN;
	}

	public void setURN(String uRN) {
		URN = uRN;
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

	public String getCreatedOn() {
		return CreatedOn;
	}

	public void setCreatedOn(String createdOn) {
		CreatedOn = createdOn;
	}

	public Integer getAssignedsno() {
		return assignedsno;
	}

	public void setAssignedsno(Integer assignedsno) {
		this.assignedsno = assignedsno;
	}

	public String getCURRENTTOTALAMOUNT() {
		return CURRENTTOTALAMOUNT;
	}

	public void setCURRENTTOTALAMOUNT(String cURRENTTOTALAMOUNT) {
		CURRENTTOTALAMOUNT = cURRENTTOTALAMOUNT;
	}

	public Long getTRANSACTIONDETAILSID() {
		return TRANSACTIONDETAILSID;
	}

	public void setTRANSACTIONDETAILSID(Long tRANSACTIONDETAILSID) {
		TRANSACTIONDETAILSID = tRANSACTIONDETAILSID;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
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

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getVerificationMode() {
		return VerificationMode;
	}

	public void setVerificationMode(Long verificationMode) {
		VerificationMode = verificationMode;
	}

	public Long getTxnpackagedetailid() {
		return txnpackagedetailid;
	}

	public void setTxnpackagedetailid(Long txnpackagedetailid) {
		this.txnpackagedetailid = txnpackagedetailid;
	}

	public Long getTriggerValue() {
		return triggerValue;
	}

	public void setTriggerValue(Long triggerValue) {
		this.triggerValue = triggerValue;
	}

	public String getTriggerMsg() {
		return triggerMsg;
	}

	public void setTriggerMsg(String triggerMsg) {
		this.triggerMsg = triggerMsg;
	}

	@Override
	public String toString() {
		return "Cpdrejecteddetailsbean [claimid=" + claimid + ", URN=" + URN + ", PatientName=" + PatientName
				+ ", PackageCode=" + PackageCode + ", CreatedOn=" + CreatedOn + ", assignedsno=" + assignedsno
				+ ", CURRENTTOTALAMOUNT=" + CURRENTTOTALAMOUNT + ", TRANSACTIONDETAILSID=" + TRANSACTIONDETAILSID
				+ ", stateCode=" + stateCode + ", districtCode=" + districtCode + ", hospitalCode=" + hospitalCode
				+ ", snoName=" + snoName + ", stateName=" + stateName + ", districtName=" + districtName
				+ ", hospitalName=" + hospitalName + ", CPD_ALLOTED_DATE=" + CPD_ALLOTED_DATE + ", invoiceno="
				+ invoiceno + ", pid=" + pid + ", claimNo=" + claimNo + ", packageName=" + packageName + ", mortality="
				+ mortality + ", hospitalMortality=" + hospitalMortality + ", actualDateOfDischarge="
				+ actualDateOfDischarge + ", actualDateOfAdmission=" + actualDateOfAdmission + ", hospital=" + hospital
				+ ", phone=" + phone + ", VerificationMode=" + VerificationMode + ", txnpackagedetailid="
				+ txnpackagedetailid + ", triggerValue=" + triggerValue + ", triggerMsg=" + triggerMsg + "]";
	}

}
