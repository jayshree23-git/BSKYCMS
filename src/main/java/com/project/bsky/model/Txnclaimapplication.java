/**
 *
 */
package com.project.bsky.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author suprava.parida
 *
 */
@Entity
@Table(name = "TXNCLAIM_APPLICATION")
public class Txnclaimapplication implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "CLAIMID")
	private Integer claimId;

	@Column(name ="TRANSACTIONDETAILSID")
	private Integer transactionDetailsId;

	@Column(name="URN")
	private String urn;

	@Column(name="HOSPITALCODE")
	private String hospitalCode;

	@Column(name="PENDINGAT")
	private Integer pendingAt;

	@Column(name = "CLAIMSTATUS")
	private Integer claimStatus;

	@Column(name = "ASSIGNEDCPD")
	private Integer assignedCPD;

	@Column(name="ASSIGNEDSNO")
	private Integer assignedSNO;

	@Column(name = "ADMINSSIONSLIP")
	private String adminssionSlip;

	@Column(name = "TREATMENTSLIP")
	private String treatmentSlip;

	@Column(name = "HOSPITALBILL")
	private String hospitalBill;

	@Column(name = "DISCHARGESLIP")
	private String dischargeSlip;

	@Column(name = "INVESTIGATIONDOC")
	private String investigationDoc;

	@Column(name = "PRESURGERYPHOTO")
	private String preSurgeryPhoto;

	@Column(name = "POSTSURGERYPHOTO")
	private String postSurgeryPhoto;

	@Column(name = "CREATEDBY")
	private Integer createdBy;

	@Column(name = "CREATEDON")
	private Date createdOn;

	@Column(name = "UPDATEDBY")
	private Integer updatedBy;

	@Column(name = "UPDATEON")
	private Date updateOn;

	@Column(name = "INVESTIGATIONDOC2")
	private String investigationDoc2;

	@Column(name = "HOSPITALBILL2")
	private String hospitalBill2;

	@Column(name = "INVOICENO")
	private Integer invoiceno;

	@Column(name = "CHIPSERIALNO")
	private String chipSerialNo;

	@Column(name = "PACKAGECODE")
	private String packageCode;

	@Column(name = "REFTRANSACTIONID")
	private Integer refTransactionId;

	@Column(name = "STATUSFLAG")
	private Integer statusFlag;

	@Column(name = "CPDAPPROVEDAMOUNT")
	private String cpdApprovedAmount;

	@Column(name = "SNOAPPROVEDAMOUNT")
	private String snoApprovedAmount;

	@Column(name = "USER_IP")
	private String userIp;

	@Column(name = "REMARK_ID")
	private Integer remarkId;

	@Column(name = "REMARKS")
	private String remarks;

	public Integer getClaimId() {
		return claimId;
	}

	public void setClaimId(Integer claimId) {
		this.claimId = claimId;
	}

	public Integer getTransactionDetailsId() {
		return transactionDetailsId;
	}

	public void setTransactionDetailsId(Integer transactionDetailsId) {
		this.transactionDetailsId = transactionDetailsId;
	}

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public Integer getPendingAt() {
		return pendingAt;
	}

	public void setPendingAt(Integer pendingAt) {
		this.pendingAt = pendingAt;
	}

	public Integer getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(Integer claimStatus) {
		this.claimStatus = claimStatus;
	}

	public Integer getAssignedCPD() {
		return assignedCPD;
	}

	public void setAssignedCPD(Integer assignedCPD) {
		this.assignedCPD = assignedCPD;
	}

	public Integer getAssignedSNO() {
		return assignedSNO;
	}

	public void setAssignedSNO(Integer assignedSNO) {
		this.assignedSNO = assignedSNO;
	}

	public String getAdminssionSlip() {
		return adminssionSlip;
	}

	public void setAdminssionSlip(String adminssionSlip) {
		this.adminssionSlip = adminssionSlip;
	}

	public String getTreatmentSlip() {
		return treatmentSlip;
	}

	public void setTreatmentSlip(String treatmentSlip) {
		this.treatmentSlip = treatmentSlip;
	}

	public String getHospitalBill() {
		return hospitalBill;
	}

	public void setHospitalBill(String hospitalBill) {
		this.hospitalBill = hospitalBill;
	}

	public String getDischargeSlip() {
		return dischargeSlip;
	}

	public void setDischargeSlip(String dischargeSlip) {
		this.dischargeSlip = dischargeSlip;
	}

	public String getInvestigationDoc() {
		return investigationDoc;
	}

	public void setInvestigationDoc(String investigationDoc) {
		this.investigationDoc = investigationDoc;
	}

	public String getPreSurgeryPhoto() {
		return preSurgeryPhoto;
	}

	public void setPreSurgeryPhoto(String preSurgeryPhoto) {
		this.preSurgeryPhoto = preSurgeryPhoto;
	}

	public String getPostSurgeryPhoto() {
		return postSurgeryPhoto;
	}

	public void setPostSurgeryPhoto(String postSurgeryPhoto) {
		this.postSurgeryPhoto = postSurgeryPhoto;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdateOn() {
		return updateOn;
	}

	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}

	public String getInvestigationDoc2() {
		return investigationDoc2;
	}

	public void setInvestigationDoc2(String investigationDoc2) {
		this.investigationDoc2 = investigationDoc2;
	}

	public String getHospitalBill2() {
		return hospitalBill2;
	}

	public void setHospitalBill2(String hospitalBill2) {
		this.hospitalBill2 = hospitalBill2;
	}

	public Integer getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(Integer invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getChipSerialNo() {
		return chipSerialNo;
	}

	public void setChipSerialNo(String chipSerialNo) {
		this.chipSerialNo = chipSerialNo;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public Integer getRefTransactionId() {
		return refTransactionId;
	}

	public void setRefTransactionId(Integer refTransactionId) {
		this.refTransactionId = refTransactionId;
	}

	public Integer getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}

	public String getCpdApprovedAmount() {
		return cpdApprovedAmount;
	}

	public void setCpdApprovedAmount(String cpdApprovedAmount) {
		this.cpdApprovedAmount = cpdApprovedAmount;
	}

	public String getSnoApprovedAmount() {
		return snoApprovedAmount;
	}

	public void setSnoApprovedAmount(String snoApprovedAmount) {
		this.snoApprovedAmount = snoApprovedAmount;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public Integer getRemarkId() {
		return remarkId;
	}

	public void setRemarkId(Integer remarkId) {
		this.remarkId = remarkId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "TreatmentHistory [claimId=" + claimId + ", transactionDetailsId=" + transactionDetailsId + ", urn="
				+ urn + ", hospitalCode=" + hospitalCode + ", pendingAt=" + pendingAt + ", claimStatus=" + claimStatus
				+ ", assignedCPD=" + assignedCPD + ", assignedSNO=" + assignedSNO + ", adminssionSlip=" + adminssionSlip
				+ ", treatmentSlip=" + treatmentSlip + ", hospitalBill=" + hospitalBill + ", dischargeSlip="
				+ dischargeSlip + ", investigationDoc=" + investigationDoc + ", preSurgeryPhoto=" + preSurgeryPhoto
				+ ", postSurgeryPhoto=" + postSurgeryPhoto + ", createdBy=" + createdBy + ", createdOn=" + createdOn
				+ ", updatedBy=" + updatedBy + ", updateOn=" + updateOn + ", investigationDoc2=" + investigationDoc2
				+ ", hospitalBill2=" + hospitalBill2 + ", invoiceno=" + invoiceno + ", chipSerialNo=" + chipSerialNo
				+ ", packageCode=" + packageCode + ", refTransactionId=" + refTransactionId + ", statusFlag="
				+ statusFlag + ", cpdApprovedAmount=" + cpdApprovedAmount + ", snoApprovedAmount=" + snoApprovedAmount
				+ ", userIp=" + userIp + ", remarkId=" + remarkId + ", remarks=" + remarks + "]";
	}





}
