package com.project.bsky.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="TXNPACKAGEDETAILS_TMS")
public class TxnPackageDetails implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "TXNPACKAGEDETAILID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	private Long id;
	
	@Column(name = "TRANSACTIONID")
	private Long  transactionId;
	
	@Column(name = "URN")
	private String  urn;
	
	@Column(name = "MEMBERID")
	private String  memberId;
	
	@Column(name = "HOSPITALCODE")
	private String  hospitalCode;
	
//	@Column(name = "HOSPITALNAME")
//	private String  hospitalName;
//	
//	@Column(name = "HOSPITALDISTRICT")
//	private String  hospitalDistrict;
	
	@Column(name = "BLOCKINGINVOICENUMBER")
	private String  blockingInvoiceNumber;
	
	@Column(name = "BLOCKINGUSERDATE")
	private String  blockingUserDate;
	
	@Column(name = "UNBLOCKINGINVOICENUMBER")
	private String  unBlockingInvoiceNumber;
	
	@Column(name = "UNBLOCKINGDESC")
	private String  unBlockingDesc;
	
	@Column(name = "UNBLOCKINGSYSTEMDATE")
	private String  unBlockingSystemDate;
	
	@Column(name = "PACKAGEHEADERID")
	private String  packageHeaderId;
	
	@Column(name = "PACKAGEHEADERCODE")
	private String  packageHeaderCode;
	
	@Column(name = "PACKAGEHEADERNAME")
	private String  packageHeaderName;
	
	@Column(name = "PACKAGESUBCATEGORYID")
	private String  packageSubCategoryId;
	
	@Column(name = "PACKAGESUBCATEGORYCODE")
	private String  packageSubCategoryCode;
	
	@Column(name = "PACKAGESUBCATEGORYNAME")
	private String  packageSubCategoryName;
	
	@Column(name = "PROCEDUREID")
	private String  procedureId;
	
	@Column(name = "PROCEDURECODE")
	private String  procedureCode;
	
	@Column(name = "PROCEDURENAME")
	private String  procedureName;
	
	@Column(name = "HOSPITALCATEGORYID")
	private String  hospitalCategoryId;
	
	@Column(name = "PACKAGECATEGORYTYPE")
	private String  packageCategoryType;
	
	@Column(name = "STAYTYPE")
	private String  staytype;
	
	@Column(name = "DAYCARE")
	private String  dayCare;
	
	@Column(name = "NOOFDAYS")
	private String  noOfDays;
	
	@Column(name = "AMOUNTBLOCKED")
	private String  amountBlocked;
	
	@Column(name = "TRANSACTIONCODE")
	private String  transactionCode;
	
	@Column(name = "PREAUTHSTATUS")
	private String  preauthstatus;
	
	@Column(name = "ISEMERGENCY")
	private String  isemergency;
	
	@Column(name = "DOCTORNAME")
	private String  doctorName;
	
	@Column(name = "DOCTORPHNO")
	private String  doctorPhoneNo;
	
	@Column(name = "OVERRIDECODE")
	private String  overrideCode;
	
	@Column(name = "REFERALCODE")
	private String  referalCode;
	
	@Column(name = "DESCRIPTION")
	private String  description;
	
	@Column(name = "VERIFIEDDATA")
	private String  verifieddata;
	
	@Column(name = "VERIFICATIONMODE")
	private String  verificationMode;
	
	@Column(name = "VERIFIEDMEMBERID")
	private String  verifiedMemberId;
	
	@Column(name = "VERIFIEDMEMBERNAME")
	private String  verifiedMemberName;
	
	@Column(name = "PREAUTHAPROVESTATUS")
	private String  preauthaproveStatus;
	
	@Column(name = "INSUFFICIENTAMOUNT")
	private String  inSufficientAmount;
	
//	@Column(name = "DUMMY3")
//	private String  dummy3;
//	
//	@Column(name = "DUMMY4")
//	private String  dummy4;
	
//	@Column(name = "DUMMY5")
//	private String  dummy5;
	
//	@Column(name = "DUMMY6")
//	private String  dummy6;
	
	@Column(name = "CREATEDBY")
	private String  createdBy;
	
	@Column(name = "CREATEDON")
	private String  createdOn;
	
	@Column(name = "UPDATEDBY")
	private String  updatedBy;
	
	@Column(name = "UPDATEDON")
	private String  updatedOn;

//	public String getHospitalName() {
//		return hospitalName;
//	}
//
//	public void setHospitalName(String hospitalName) {
//		this.hospitalName = hospitalName;
//	}
//
//	public String getHospitalDistrict() {
//		return hospitalDistrict;
//	}
//
//	public void setHospitalDistrict(String hospitalDistrict) {
//		this.hospitalDistrict = hospitalDistrict;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getBlockingInvoiceNumber() {
		return blockingInvoiceNumber;
	}

	public void setBlockingInvoiceNumber(String blockingInvoiceNumber) {
		this.blockingInvoiceNumber = blockingInvoiceNumber;
	}

	public String getBlockingUserDate() {
		return blockingUserDate;
	}

	public void setBlockingUserDate(String blockingUserDate) {
		this.blockingUserDate = blockingUserDate;
	}

	public String getUnBlockingInvoiceNumber() {
		return unBlockingInvoiceNumber;
	}

	public void setUnBlockingInvoiceNumber(String unBlockingInvoiceNumber) {
		this.unBlockingInvoiceNumber = unBlockingInvoiceNumber;
	}

	public String getUnBlockingDesc() {
		return unBlockingDesc;
	}

	public void setUnBlockingDesc(String unBlockingDesc) {
		this.unBlockingDesc = unBlockingDesc;
	}

	public String getUnBlockingSystemDate() {
		return unBlockingSystemDate;
	}

	public void setUnBlockingSystemDate(String unBlockingSystemDate) {
		this.unBlockingSystemDate = unBlockingSystemDate;
	}

	public String getPackageHeaderId() {
		return packageHeaderId;
	}

	public void setPackageHeaderId(String packageHeaderId) {
		this.packageHeaderId = packageHeaderId;
	}

	public String getPackageHeaderCode() {
		return packageHeaderCode;
	}

	public void setPackageHeaderCode(String packageHeaderCode) {
		this.packageHeaderCode = packageHeaderCode;
	}

	public String getPackageHeaderName() {
		return packageHeaderName;
	}

	public void setPackageHeaderName(String packageHeaderName) {
		this.packageHeaderName = packageHeaderName;
	}

	public String getPackageSubCategoryId() {
		return packageSubCategoryId;
	}

	public void setPackageSubCategoryId(String packageSubCategoryId) {
		this.packageSubCategoryId = packageSubCategoryId;
	}

	public String getPackageSubCategoryCode() {
		return packageSubCategoryCode;
	}

	public void setPackageSubCategoryCode(String packageSubCategoryCode) {
		this.packageSubCategoryCode = packageSubCategoryCode;
	}

	public String getPackageSubCategoryName() {
		return packageSubCategoryName;
	}

	public void setPackageSubCategoryName(String packageSubCategoryName) {
		this.packageSubCategoryName = packageSubCategoryName;
	}

	public String getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(String procedureId) {
		this.procedureId = procedureId;
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

	public String getHospitalCategoryId() {
		return hospitalCategoryId;
	}

	public void setHospitalCategoryId(String hospitalCategoryId) {
		this.hospitalCategoryId = hospitalCategoryId;
	}

	public String getPackageCategoryType() {
		return packageCategoryType;
	}

	public void setPackageCategoryType(String packageCategoryType) {
		this.packageCategoryType = packageCategoryType;
	}

	public String getStaytype() {
		return staytype;
	}

	public void setStaytype(String staytype) {
		this.staytype = staytype;
	}

	public String getDayCare() {
		return dayCare;
	}

	public void setDayCare(String dayCare) {
		this.dayCare = dayCare;
	}

	public String getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(String noOfDays) {
		this.noOfDays = noOfDays;
	}

	public String getAmountBlocked() {
		return amountBlocked;
	}

	public void setAmountBlocked(String amountBlocked) {
		this.amountBlocked = amountBlocked;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getPreauthstatus() {
		return preauthstatus;
	}

	public void setPreauthstatus(String preauthstatus) {
		this.preauthstatus = preauthstatus;
	}

	public String getIsemergency() {
		return isemergency;
	}

	public void setIsemergency(String isemergency) {
		this.isemergency = isemergency;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDoctorPhoneNo() {
		return doctorPhoneNo;
	}

	public void setDoctorPhoneNo(String doctorPhoneNo) {
		this.doctorPhoneNo = doctorPhoneNo;
	}

	public String getOverrideCode() {
		return overrideCode;
	}

	public void setOverrideCode(String overrideCode) {
		this.overrideCode = overrideCode;
	}

	public String getReferalCode() {
		return referalCode;
	}

	public void setReferalCode(String referalCode) {
		this.referalCode = referalCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVerifieddata() {
		return verifieddata;
	}

	public void setVerifieddata(String verifieddata) {
		this.verifieddata = verifieddata;
	}

	public String getVerificationMode() {
		return verificationMode;
	}

	public void setVerificationMode(String verificationMode) {
		this.verificationMode = verificationMode;
	}

	public String getVerifiedMemberId() {
		return verifiedMemberId;
	}

	public void setVerifiedMemberId(String verifiedMemberId) {
		this.verifiedMemberId = verifiedMemberId;
	}

	public String getVerifiedMemberName() {
		return verifiedMemberName;
	}

	public void setVerifiedMemberName(String verifiedMemberName) {
		this.verifiedMemberName = verifiedMemberName;
	}

	public String getPreauthaproveStatus() {
		return preauthaproveStatus;
	}

	public void setPreauthaproveStatus(String preauthaproveStatus) {
		this.preauthaproveStatus = preauthaproveStatus;
	}

	public String getInSufficientAmount() {
		return inSufficientAmount;
	}

	public void setInSufficientAmount(String inSufficientAmount) {
		this.inSufficientAmount = inSufficientAmount;
	}

//	public String getDummy3() {
//		return dummy3;
//	}
//
//	public void setDummy3(String dummy3) {
//		this.dummy3 = dummy3;
//	}
//
//	public String getDummy4() {
//		return dummy4;
//	}
//
//	public void setDummy4(String dummy4) {
//		this.dummy4 = dummy4;
//	}
//
//	public String getDummy5() {
//		return dummy5;
//	}
//
//	public void setDummy5(String dummy5) {
//		this.dummy5 = dummy5;
//	}

//	public String getDummy6() {
//		return dummy6;
//	}
//
//	public void setDummy6(String dummy6) {
//		this.dummy6 = dummy6;
//	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
