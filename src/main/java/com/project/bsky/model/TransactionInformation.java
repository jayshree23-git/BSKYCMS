package com.project.bsky.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TXNTRANSACTIONINFORMATION_TMS")

public class TransactionInformation implements Serializable{
	
	@Id
	@Column(name="TRANSACTIONID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	
	private Long  transactionid;
	
	@Column(name="MEMBERSTATECODE")
	private String  memberstatecode;
	
	@Column(name="STATENAME")
	private String statename;
	
	@Column(name="DISTRICTCODE")
	private String districtcode;
	
	@Column(name="DISTRICTNAME")
	private String districtname;
	
	@Column(name="BLOCKCODE")
	private String blockcode;
	
	@Column(name="BLOCKNAME")
	private String blockname;
	
	@Column(name="PANCHAYATCODE")
	private String panchayatcode;
	
	@Column(name="PANCHAYATNAME")
	private String panchayatname;
	
	@Column(name="VILLAGECODE")
	private String villagecode;
	
	@Column(name="VILLAGENAME")
	private String villagename;
	
	@Column(name="UIDNUMBER")
	private String uidnumber;
	
	@Column(name="URN")
	private String urn;
	
	@Column(name="TRANSACTIONCODE")
	private String transactioncode;
	
	@Column(name="POLICYSTARTDATE")
	private String policystartdate;
	
	@Column(name="POLICYENDDATE")
	private String policyenddate;
	
	@Column(name="MEMBERID")
	private String memberid;
	
	@Column(name="MEMBERNAME")
	private String membername;
	
	@Column(name="PATIENTCONTACTNUMBER")
	private String patientcontactnumber;
	
	@Column(name="PATIENTGENDER")
	private String patientgender;
	
	@Column(name="AGE")
	private String age;
	
	@Column(name="HEADMEMBERID")
	private String headmemberid;
	
	@Column(name="HEADMEMBERNAME")
	private String headmembername;
	
	@Column(name="HOSPITALCODE")
	private String hospitalcode;
	
	@Column(name="HOSPITALNAME")
	private String hospitalname;
	
	@Column(name="HOSPITALSTATE")
	private String hospitalstate;
	
	@Column(name="HOSPITALDISTRICT")
	private String hospitaldistrict;
	
	@Column(name="HOSPITALAUTHORITYCODE")
	private String hospitalauthoritycode;
	
	@Column(name="TRANSACTIONDATE")
	private String transactiondate;
	
	@Column(name="MORTALITY")
	private String mortality;
	
	@Column(name="MORTALITYSUMMARY")
	private String mortalitysummary;
	
	@Column(name="ADMISSIONDATE")
	private String admissiondate;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="INVOICE")
	private String invoice;
	
	@Column(name="DISCHARGEFLAG")
	private String dischargeflag;
	
	@Column(name="TOTALAMOUNTCLAIMED")
	private String totalamoutclaimed;
	
	@Column(name="AVAILABLEBALANCE")
	private String availabebalance;
	
	@Column(name="CREATEDBY")
	private String createdby;
	
	@Column(name="CREATEDON")
	private String createdon;
	
	@Column(name="UPDATEDBY")
	private String updatedby;
	
	
	@Column(name="UPDATEDON")
	private String updatedon;
	
	@Column(name="DELETEDFLAG")
	private Integer deletedflag;

	public Long getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(Long transactionid) {
		this.transactionid = transactionid;
	}

	public String getMemberstatecode() {
		return memberstatecode;
	}

	public void setMemberstatecode(String memberstatecode) {
		this.memberstatecode = memberstatecode;
	}

	public String getStatename() {
		return statename;
	}

	public void setStatename(String statename) {
		this.statename = statename;
	}

	public String getDistrictcode() {
		return districtcode;
	}

	public void setDistrictcode(String districtcode) {
		this.districtcode = districtcode;
	}

	public String getDistrictname() {
		return districtname;
	}

	public void setDistrictname(String districtname) {
		this.districtname = districtname;
	}

	public String getBlockcode() {
		return blockcode;
	}

	public void setBlockcode(String blockcode) {
		this.blockcode = blockcode;
	}

	public String getBlockname() {
		return blockname;
	}

	public void setBlockname(String blockname) {
		this.blockname = blockname;
	}

	public String getPanchayatcode() {
		return panchayatcode;
	}

	public void setPanchayatcode(String panchayatcode) {
		this.panchayatcode = panchayatcode;
	}

	public String getPanchayatname() {
		return panchayatname;
	}

	public void setPanchayatname(String panchayatname) {
		this.panchayatname = panchayatname;
	}

	public String getVillagecode() {
		return villagecode;
	}

	public void setVillagecode(String villagecode) {
		this.villagecode = villagecode;
	}

	public String getVillagename() {
		return villagename;
	}

	public void setVillagename(String villagename) {
		this.villagename = villagename;
	}

	public String getUidnumber() {
		return uidnumber;
	}

	public void setUidnumber(String uidnumber) {
		this.uidnumber = uidnumber;
	}

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public String getTransactioncode() {
		return transactioncode;
	}

	public void setTransactioncode(String transactioncode) {
		this.transactioncode = transactioncode;
	}

	public String getPolicystartdate() {
		return policystartdate;
	}

	public void setPolicystartdate(String policystartdate) {
		this.policystartdate = policystartdate;
	}

	public String getPolicyenddate() {
		return policyenddate;
	}

	public void setPolicyenddate(String policyenddate) {
		this.policyenddate = policyenddate;
	}

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public String getPatientcontactnumber() {
		return patientcontactnumber;
	}

	public void setPatientcontactnumber(String patientcontactnumber) {
		this.patientcontactnumber = patientcontactnumber;
	}

	public String getPatientgender() {
		return patientgender;
	}

	public void setPatientgender(String patientgender) {
		this.patientgender = patientgender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getHeadmemberid() {
		return headmemberid;
	}

	public void setHeadmemberid(String headmemberid) {
		this.headmemberid = headmemberid;
	}

	public String getHeadmembername() {
		return headmembername;
	}

	public void setHeadmembername(String headmembername) {
		this.headmembername = headmembername;
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

	public String getHospitalstate() {
		return hospitalstate;
	}

	public void setHospitalstate(String hospitalstate) {
		this.hospitalstate = hospitalstate;
	}

	public String getHospitaldistrict() {
		return hospitaldistrict;
	}

	public void setHospitaldistrict(String hospitaldistrict) {
		this.hospitaldistrict = hospitaldistrict;
	}

	public String getHospitalauthoritycode() {
		return hospitalauthoritycode;
	}

	public void setHospitalauthoritycode(String hospitalauthoritycode) {
		this.hospitalauthoritycode = hospitalauthoritycode;
	}

	public String getTransactiondate() {
		return transactiondate;
	}

	public void setTransactiondate(String transactiondate) {
		this.transactiondate = transactiondate;
	}

	public String getMortality() {
		return mortality;
	}

	public void setMortality(String mortality) {
		this.mortality = mortality;
	}

	public String getMortalitysummary() {
		return mortalitysummary;
	}

	public void setMortalitysummary(String mortalitysummary) {
		this.mortalitysummary = mortalitysummary;
	}

	public String getAdmissiondate() {
		return admissiondate;
	}

	public void setAdmissiondate(String admissiondate) {
		this.admissiondate = admissiondate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getDischargeflag() {
		return dischargeflag;
	}

	public void setDischargeflag(String dischargeflag) {
		this.dischargeflag = dischargeflag;
	}

	public String getTotalamoutclaimed() {
		return totalamoutclaimed;
	}

	public void setTotalamoutclaimed(String totalamoutclaimed) {
		this.totalamoutclaimed = totalamoutclaimed;
	}

	public String getAvailabebalance() {
		return availabebalance;
	}

	public void setAvailabebalance(String availabebalance) {
		this.availabebalance = availabebalance;
	}


	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getCreatedon() {
		return createdon;
	}

	public void setCreatedon(String createdon) {
		this.createdon = createdon;
	}

	public String getUpdatedby() {
		return updatedby;
	}

	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}

	public String getUpdatedon() {
		return updatedon;
	}

	public void setUpdatedon(String updatedon) {
		this.updatedon = updatedon;
	}

	public Integer getDeletedflag() {
		return deletedflag;
	}

	public void setDeletedflag(Integer deletedflag) {
		this.deletedflag = deletedflag;
	}
	

}
