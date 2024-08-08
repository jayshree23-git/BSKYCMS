package com.project.bsky.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ClaimRaiseSearch {
	private long id;
	private long transactionid;
	private String invoiceno;
	private String URN;
	private String noofdays;
	private String actualdateofadmission;
	private String actualdateofdischarge;
	private String totalamountclaimed;
	private String procedurename;
	private int age;
	private String gender;
	private String PatientName;
	private String PackageCode;
	private String PackageName;
	private long CurrentTotalAmount;
	private String DateOfDischarge;
	private Integer userid;
	private Integer hospitalstateCode;
	private String dateofadmission;
	private long transactiondetailsid;
	private String totalamountblocked;
	private String hospitalcode;
	private String download;
	private String statename;
	private String districtname;
	private String blockname;
	private String panchayatname;
	private String villagename;
	private Integer statusflag;
	private String authorizedcode;
	private String patientphoneno;
    private String noofdatscalculation;
	private String verificationmode;
    private String ispatientotpverifiedString;
	private String REFERRALAUTHSTATUS;
    private String intrasurgery;
	private String POSTSURGERY;
    private String PRESURGERY;
	private String SPECIMENREMOVAL;
	private String caseno;
	private String discharge_doc;
	private String patientphoto;
	private String packageCode1;
	private String packageName1;
	private String subPackageCode;
	private String subPackageName;
	private String procedureCode;
	private String procedureName;
	private String Preauthdoc;
	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	private String claimdoc;
	private String categoryname;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(long transactionid) {
		this.transactionid = transactionid;
	}

	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getURN() {
		return URN;
	}

	public void setURN(String URN) {
		this.URN = URN;
	}

	public String getNoofdays() {
		return noofdays;
	}

	public void setNoofdays(String noofdays) {
		this.noofdays = noofdays;
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

	public String getTotalamountclaimed() {
		return totalamountclaimed;
	}

	public void setTotalamountclaimed(String totalamountclaimed) {
		this.totalamountclaimed = totalamountclaimed;
	}

	public String getProcedurename() {
		return procedurename;
	}

	public void setProcedurename(String procedurename) {
		this.procedurename = procedurename;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public long getCurrentTotalAmount() {
		return CurrentTotalAmount;
	}

	public void setCurrentTotalAmount(long currentTotalAmount) {
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

	public long getTransactiondetailsid() {
		return transactiondetailsid;
	}

	public void setTransactiondetailsid(long transactiondetailsid) {
		this.transactiondetailsid = transactiondetailsid;
	}

	public String getTotalamountblocked() {
		return totalamountblocked;
	}

	public void setTotalamountblocked(String totalamountblocked) {
		this.totalamountblocked = totalamountblocked;
	}

	public String getHospitalcode() {
		return hospitalcode;
	}

	public void setHospitalcode(String hospitalcode) {
		this.hospitalcode = hospitalcode;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getStatename() {
		return statename;
	}

	public void setStatename(String statename) {
		this.statename = statename;
	}

	public String getDistrictname() {
		return districtname;
	}

	public void setDistrictname(String districtname) {
		this.districtname = districtname;
	}

	public String getBlockname() {
		return blockname;
	}

	public void setBlockname(String blockname) {
		this.blockname = blockname;
	}

	public String getPanchayatname() {
		return panchayatname;
	}

	public void setPanchayatname(String panchayatname) {
		this.panchayatname = panchayatname;
	}

	public String getVillagename() {
		return villagename;
	}

	public void setVillagename(String villagename) {
		this.villagename = villagename;
	}

	public Integer getStatusflag() {
		return statusflag;
	}

	public void setStatusflag(Integer statusflag) {
		this.statusflag = statusflag;
	}

	public String getAuthorizedcode() {
		return authorizedcode;
	}

	public void setAuthorizedcode(String authorizedcode) {
		this.authorizedcode = authorizedcode;
	}

	public String getPatientphoneno() {
		return patientphoneno;
	}

	public void setPatientphoneno(String patientphoneno) {
		this.patientphoneno = patientphoneno;
	}

	public String getNoofdatscalculation() {
		return noofdatscalculation;
	}

	public void setNoofdatscalculation(String noofdatscalculation) {
		this.noofdatscalculation = noofdatscalculation;
	}

	public String getVerificationmode() {
		return verificationmode;
	}

	public void setVerificationmode(String verificationmode) {
		this.verificationmode = verificationmode;
	}

	public String getIspatientotpverifiedString() {
		return ispatientotpverifiedString;
	}

	public void setIspatientotpverifiedString(String ispatientotpverifiedString) {
		this.ispatientotpverifiedString = ispatientotpverifiedString;
	}

	public String getREFERRALAUTHSTATUS() {
		return REFERRALAUTHSTATUS;
	}

	public void setREFERRALAUTHSTATUS(String REFERRALAUTHSTATUS) {
		this.REFERRALAUTHSTATUS = REFERRALAUTHSTATUS;
	}

	public String getIntrasurgery() {
		return intrasurgery;
	}

	public void setIntrasurgery(String intrasurgery) {
		this.intrasurgery = intrasurgery;
	}

	public String getPOSTSURGERY() {
		return POSTSURGERY;
	}

	public void setPOSTSURGERY(String POSTSURGERY) {
		this.POSTSURGERY = POSTSURGERY;
	}

	public String getPRESURGERY() {
		return PRESURGERY;
	}

	public void setPRESURGERY(String PRESURGERY) {
		this.PRESURGERY = PRESURGERY;
	}

	public String getSPECIMENREMOVAL() {
		return SPECIMENREMOVAL;
	}

	public void setSPECIMENREMOVAL(String SPECIMENREMOVAL) {
		this.SPECIMENREMOVAL = SPECIMENREMOVAL;
	}

	public String getCaseno() {
		return caseno;
	}

	public void setCaseno(String caseno) {
		this.caseno = caseno;
	}

	public String getDischarge_doc() {
		return discharge_doc;
	}

	public void setDischarge_doc(String discharge_doc) {
		this.discharge_doc = discharge_doc;
	}

	public String getPatientphoto() {
		return patientphoto;
	}

	public void setPatientphoto(String patientphoto) {
		this.patientphoto = patientphoto;
	}

	public String getPackageCode1() {
		return packageCode1;
	}

	public void setPackageCode1(String packageCode1) {
		this.packageCode1 = packageCode1;
	}

	public String getPackageName1() {
		return packageName1;
	}

	public void setPackageName1(String packageName1) {
		this.packageName1 = packageName1;
	}

	public String getSubPackageCode() {
		return subPackageCode;
	}

	public void setSubPackageCode(String subPackageCode) {
		this.subPackageCode = subPackageCode;
	}

	public String getSubPackageName() {
		return subPackageName;
	}

	public void setSubPackageName(String subPackageName) {
		this.subPackageName = subPackageName;
	}

	public String getProcedureCode() {
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode) {
		this.procedureCode = procedureCode;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}


	public String getPreauthdoc() {
		return Preauthdoc;
	}

	public void setPreauthdoc(String preauthdoc) {
		Preauthdoc = preauthdoc;
	}

	public String getClaimdoc() {
		return claimdoc;
	}

	public void setClaimdoc(String claimdoc) {
		this.claimdoc = claimdoc;
	}

	@Override
	public String toString() {
		return "ClaimRaiseSearch [id=" + id + ", transactionid=" + transactionid + ", invoiceno=" + invoiceno + ", URN="
				+ URN + ", noofdays=" + noofdays + ", actualdateofadmission=" + actualdateofadmission
				+ ", actualdateofdischarge=" + actualdateofdischarge + ", totalamountclaimed=" + totalamountclaimed
				+ ", procedurename=" + procedurename + ", age=" + age + ", gender=" + gender + ", PatientName="
				+ PatientName + ", PackageCode=" + PackageCode + ", PackageName=" + PackageName
				+ ", CurrentTotalAmount=" + CurrentTotalAmount + ", DateOfDischarge=" + DateOfDischarge + ", userid="
				+ userid + ", hospitalstateCode=" + hospitalstateCode + ", dateofadmission=" + dateofadmission
				+ ", transactiondetailsid=" + transactiondetailsid + ", totalamountblocked=" + totalamountblocked
				+ ", hospitalcode=" + hospitalcode + ", download=" + download + ", statename=" + statename
				+ ", districtname=" + districtname + ", blockname=" + blockname + ", panchayatname=" + panchayatname
				+ ", villagename=" + villagename + ", statusflag=" + statusflag + ", authorizedcode=" + authorizedcode
				+ ", patientphoneno=" + patientphoneno + ", noofdatscalculation=" + noofdatscalculation
				+ ", verificationmode=" + verificationmode + ", ispatientotpverifiedString="
				+ ispatientotpverifiedString + ", REFERRALAUTHSTATUS=" + REFERRALAUTHSTATUS + ", intrasurgery="
				+ intrasurgery + ", POSTSURGERY=" + POSTSURGERY + ", PRESURGERY=" + PRESURGERY + ", SPECIMENREMOVAL="
				+ SPECIMENREMOVAL + ", caseno=" + caseno + ", discharge_doc=" + discharge_doc + ", patientphoto="
				+ patientphoto + ", packageCode1=" + packageCode1 + ", packageName1=" + packageName1
				+ ", subPackageCode=" + subPackageCode + ", subPackageName=" + subPackageName + ", procedureCode="
				+ procedureCode + ", procedureName=" + procedureName + ", Preauthdoc=" + Preauthdoc + ", claimdoc="
				+ claimdoc + ", categoryname=" + categoryname + "]";
	}

}








