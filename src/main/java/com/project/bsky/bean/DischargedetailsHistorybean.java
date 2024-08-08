package com.project.bsky.bean;

import java.io.Serializable;

import lombok.Data;


public class DischargedetailsHistorybean implements Serializable{

	private String  urn;
	private String actualdateofadmission;
	private String actualdateofdischarge;
	private String statename;
	private String districtname;
	private String blockname;
	private String villagename;
	private String hospitalname;
	private String patientname;
	private String gender;
	private String age;
	private String procedurename;
	private String packagename;
	private long noofdays;
	private long invoiceno;
	private String totalamountclaimed;
	private String totalamountblocked;
	private String familyheadname;
	private String verifiername;
	private String dateofadmission;
	private String dateofdischarge;
	private String mortality;
	private String referralcode;
	private String authorizedcode;
	private String nabh;
	private String implant_data;
	private long PATIENTPHONENO;
	private String hospitalcode;
	private String currenttotalamount;
	private String dischargeslip;
	private String addtional_doc;
	private String additional_doc1;
	private String additional_doc2;
	private String patient_photo;
	private String intra_surgery_photo;
	private String specimen_removal_photo;
	private String presurgeryphoto;
	private String postsurgeryphoto;
	private String claim_no;
	private String caseno;
	private String claimbilldate;
	private String tmscaseno;

	public String getDischargeslip() {
		return dischargeslip;
	}
	public void setDischargeslip(String dischargeslip) {
		this.dischargeslip = dischargeslip;
	}
	public String getAddtional_doc() {
		return addtional_doc;
	}
	public void setAddtional_doc(String addtional_doc) {
		this.addtional_doc = addtional_doc;
	}
	public String getAdditional_doc1() {
		return additional_doc1;
	}
	public void setAdditional_doc1(String additional_doc1) {
		this.additional_doc1 = additional_doc1;
	}
	public String getAdditional_doc2() {
		return additional_doc2;
	}
	public void setAdditional_doc2(String additional_doc2) {
		this.additional_doc2 = additional_doc2;
	}
	public String getPatient_photo() {
		return patient_photo;
	}
	public void setPatient_photo(String patient_photo) {
		this.patient_photo = patient_photo;
	}
	public String getIntra_surgery_photo() {
		return intra_surgery_photo;
	}
	public void setIntra_surgery_photo(String intra_surgery_photo) {
		this.intra_surgery_photo = intra_surgery_photo;
	}
	public String getSpecimen_removal_photo() {
		return specimen_removal_photo;
	}
	public void setSpecimen_removal_photo(String specimen_removal_photo) {
		this.specimen_removal_photo = specimen_removal_photo;
	}
	public String getPresurgeryphoto() {
		return presurgeryphoto;
	}
	public void setPresurgeryphoto(String presurgeryphoto) {
		this.presurgeryphoto = presurgeryphoto;
	}
	public String getPostsurgeryphoto() {
		return postsurgeryphoto;
	}
	public void setPostsurgeryphoto(String postsurgeryphoto) {
		this.postsurgeryphoto = postsurgeryphoto;
	}
	public String getCurrenttotalamount() {
		return currenttotalamount;
	}
	public void setCurrenttotalamount(String currenttotalamount) {
		this.currenttotalamount = currenttotalamount;
	}
	public String getUrn() {
		return urn;
	}
	public void setUrn(String urn) {
		this.urn = urn;
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
	public String getVillagename() {
		return villagename;
	}
	public void setVillagename(String villagename) {
		this.villagename = villagename;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getProcedurename() {
		return procedurename;
	}
	public void setProcedurename(String procedurename) {
		this.procedurename = procedurename;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public long getNoofdays() {
		return noofdays;
	}
	public void setNoofdays(long noofdays) {
		this.noofdays = noofdays;
	}
	public long getInvoiceno() {
		return invoiceno;
	}
	public void setInvoiceno(long invoiceno) {
		this.invoiceno = invoiceno;
	}
	public String getTotalamountclaimed() {
		return totalamountclaimed;
	}
	public void setTotalamountclaimed(String totalamountclaimed) {
		this.totalamountclaimed = totalamountclaimed;
	}
	public String getTotalamountblocked() {
		return totalamountblocked;
	}
	public void setTotalamountblocked(String totalamountblocked) {
		this.totalamountblocked = totalamountblocked;
	}
	public String getFamilyheadname() {
		return familyheadname;
	}
	public void setFamilyheadname(String familyheadname) {
		this.familyheadname = familyheadname;
	}
	public String getVerifiername() {
		return verifiername;
	}
	public void setVerifiername(String verifiername) {
		this.verifiername = verifiername;
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
	public String getMortality() {
		return mortality;
	}
	public void setMortality(String mortality) {
		this.mortality = mortality;
	}
	public String getReferralcode() {
		return referralcode;
	}
	public void setReferralcode(String referralcode) {
		this.referralcode = referralcode;
	}
	public String getAuthorizedcode() {
		return authorizedcode;
	}
	public void setAuthorizedcode(String authorizedcode) {
		this.authorizedcode = authorizedcode;
	}
	public String getNabh() {
		return nabh;
	}
	public void setNabh(String nabh) {
		this.nabh = nabh;
	}
	public String getImplant_data() {
		return implant_data;
	}
	public void setImplant_data(String implant_data) {
		this.implant_data = implant_data;
	}
	public long getPATIENTPHONENO() {
		return PATIENTPHONENO;
	}
	public void setPATIENTPHONENO(long pATIENTPHONENO) {
		PATIENTPHONENO = pATIENTPHONENO;
	}
	public String getHospitalcode() {
		return hospitalcode;
	}
	public void setHospitalcode(String hospitalcode) {
		this.hospitalcode = hospitalcode;
	}
	public String getClaim_no() {
		return claim_no;
	}
	public void setClaim_no(String claim_no) {
		this.claim_no = claim_no;
	}
	public String getCaseno() {
		return caseno;
	}
	public void setCaseno(String caseno) {
		this.caseno = caseno;
	}
	public String getClaimbilldate() {
		return claimbilldate;
	}
	public void setClaimbilldate(String claimbilldate) {
		this.claimbilldate = claimbilldate;
	}
	public String getTmscaseno() {
		return tmscaseno;
	}
	public void setTmscaseno(String tmscaseno) {
		this.tmscaseno = tmscaseno;
	}
	@Override
	public String toString() {
		return "DischargedetailsHistorybean [urn=" + urn + ", actualdateofadmission=" + actualdateofadmission
				+ ", actualdateofdischarge=" + actualdateofdischarge + ", statename=" + statename + ", districtname="
				+ districtname + ", blockname=" + blockname + ", villagename=" + villagename + ", hospitalname="
				+ hospitalname + ", patientname=" + patientname + ", gender=" + gender + ", age=" + age
				+ ", procedurename=" + procedurename + ", packagename=" + packagename + ", noofdays=" + noofdays
				+ ", invoiceno=" + invoiceno + ", totalamountclaimed=" + totalamountclaimed + ", totalamountblocked="
				+ totalamountblocked + ", familyheadname=" + familyheadname + ", verifiername=" + verifiername
				+ ", dateofadmission=" + dateofadmission + ", dateofdischarge=" + dateofdischarge + ", mortality="
				+ mortality + ", referralcode=" + referralcode + ", authorizedcode=" + authorizedcode + ", nabh=" + nabh
				+ ", implant_data=" + implant_data + ", PATIENTPHONENO=" + PATIENTPHONENO + ", hospitalcode="
				+ hospitalcode + ", currenttotalamount=" + currenttotalamount + ", dischargeslip=" + dischargeslip
				+ ", addtional_doc=" + addtional_doc + ", additional_doc1=" + additional_doc1 + ", additional_doc2="
				+ additional_doc2 + ", patient_photo=" + patient_photo + ", intra_surgery_photo=" + intra_surgery_photo
				+ ", specimen_removal_photo=" + specimen_removal_photo + ", presurgeryphoto=" + presurgeryphoto
				+ ", postsurgeryphoto=" + postsurgeryphoto + ", claim_no=" + claim_no + ", caseno=" + caseno
				+ ", claimbilldate=" + claimbilldate + ", tmscaseno=" + tmscaseno + "]";
	}


}
