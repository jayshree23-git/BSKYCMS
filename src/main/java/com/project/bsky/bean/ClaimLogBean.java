/**
 * 
 */
package com.project.bsky.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author arabinda.guin
 *
 */
@AllArgsConstructor
@NoArgsConstructor
public class ClaimLogBean {
	private Long claimId;
	private Long userId;
	private String amount;
	private String remarks;
	private Long actionRemarksId;
	private String actionRemark;
	private String urnNo;
	private Long pendingAt;
	private Long claimStatus;
	private String claimAmount;
	private String presurgeryphoto;
	private String postsurgeryphoto;
	private String additionaldocs;
	private String dischargeslip;
	private String investigationdocs;
	private String additionaldoc1;
	private String additionaldoc2;
	private Long statusflag;
	private String intrasurgery;
	private String patientpic;
	private String specimenpic;
	private String mortality;
	private String investigationdocs1;
	private String investigationdocs2;
	private int timingLogId;
	private Long remarkSelect;
	private String snamortality;
	private Long icdFlag;
	private List<ICDDetailsBean> icdFinalData;

	public Long getClaimId() {
		return claimId;
	}

	public void setClaimId(Long claimId) {
		this.claimId = claimId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getActionRemarksId() {
		return actionRemarksId;
	}

	public void setActionRemarksId(Long actionRemarksId) {
		this.actionRemarksId = actionRemarksId;
	}

	public String getActionRemark() {
		return actionRemark;
	}

	public void setActionRemark(String actionRemark) {
		this.actionRemark = actionRemark;
	}

	public String getUrnNo() {
		return urnNo;
	}

	public void setUrnNo(String urnNo) {
		this.urnNo = urnNo;
	}

	public Long getPendingAt() {
		return pendingAt;
	}

	public void setPendingAt(Long pendingAt) {
		this.pendingAt = pendingAt;
	}

	public Long getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(Long claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(String claimAmount) {
		this.claimAmount = claimAmount;
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

	public String getAdditionaldocs() {
		return additionaldocs;
	}

	public void setAdditionaldocs(String additionaldocs) {
		this.additionaldocs = additionaldocs;
	}

	public String getDischargeslip() {
		return dischargeslip;
	}

	public void setDischargeslip(String dischargeslip) {
		this.dischargeslip = dischargeslip;
	}

	public String getInvestigationdocs() {
		return investigationdocs;
	}

	public void setInvestigationdocs(String investigationdocs) {
		this.investigationdocs = investigationdocs;
	}

	public String getAdditionaldoc1() {
		return additionaldoc1;
	}

	public void setAdditionaldoc1(String additionaldoc1) {
		this.additionaldoc1 = additionaldoc1;
	}

	public String getAdditionaldoc2() {
		return additionaldoc2;
	}

	public void setAdditionaldoc2(String additionaldoc2) {
		this.additionaldoc2 = additionaldoc2;
	}

	public Long getStatusflag() {
		return statusflag;
	}

	public void setStatusflag(Long statusflag) {
		this.statusflag = statusflag;
	}

	public String getIntrasurgery() {
		return intrasurgery;
	}

	public void setIntrasurgery(String intrasurgery) {
		this.intrasurgery = intrasurgery;
	}

	public String getPatientpic() {
		return patientpic;
	}

	public void setPatientpic(String patientpic) {
		this.patientpic = patientpic;
	}

	public String getSpecimenpic() {
		return specimenpic;
	}

	public void setSpecimenpic(String specimenpic) {
		this.specimenpic = specimenpic;
	}

	public String getMortality() {
		return mortality;
	}

	public void setMortality(String mortality) {
		this.mortality = mortality;
	}

	public String getInvestigationdocs1() {
		return investigationdocs1;
	}

	public void setInvestigationdocs1(String investigationdocs1) {
		this.investigationdocs1 = investigationdocs1;
	}

	public String getInvestigationdocs2() {
		return investigationdocs2;
	}

	public void setInvestigationdocs2(String investigationdocs2) {
		this.investigationdocs2 = investigationdocs2;
	}

	public int getTimingLogId() {
		return timingLogId;
	}

	public void setTimingLogId(int timingLogId) {
		this.timingLogId = timingLogId;
	}

	public Long getRemarkSelect() {
		return remarkSelect;
	}

	public void setRemarkSelect(Long remarkSelect) {
		this.remarkSelect = remarkSelect;
	}

	public Long getIcdFlag() {
		return icdFlag;
	}

	public void setIcdFlag(Long icdFlag) {
		this.icdFlag = icdFlag;
	}

	public List<ICDDetailsBean> getIcdFinalData() {
		return icdFinalData;
	}

	public void setIcdFinalData(List<ICDDetailsBean> icdFinalData) {
		this.icdFinalData = icdFinalData;
	}

	@Override
	public String toString() {
		return "ClaimLogBean [claimId=" + claimId + ", userId=" + userId + ", amount=" + amount + ", remarks=" + remarks
				+ ", actionRemarksId=" + actionRemarksId + ", actionRemark=" + actionRemark + ", urnNo=" + urnNo
				+ ", pendingAt=" + pendingAt + ", claimStatus=" + claimStatus + ", claimAmount=" + claimAmount
				+ ", presurgeryphoto=" + presurgeryphoto + ", postsurgeryphoto=" + postsurgeryphoto
				+ ", additionaldocs=" + additionaldocs + ", dischargeslip=" + dischargeslip + ", investigationdocs="
				+ investigationdocs + ", additionaldoc1=" + additionaldoc1 + ", additionaldoc2=" + additionaldoc2
				+ ", statusflag=" + statusflag + ", intrasurgery=" + intrasurgery + ", patientpic=" + patientpic
				+ ", specimenpic=" + specimenpic + ", mortality=" + mortality + ", investigationdocs1="
				+ investigationdocs1 + ", investigationdocs2=" + investigationdocs2 + ", timingLogId=" + timingLogId
				+ ", remarkSelect=" + remarkSelect + ", icdFlag=" + icdFlag + ", icdFinalData=" + icdFinalData + "]";
	}

	public String getSnamortality() {
		return snamortality;
	}

	public void setSnamortality(String snamortality) {
		this.snamortality = snamortality;
	}

	

}
