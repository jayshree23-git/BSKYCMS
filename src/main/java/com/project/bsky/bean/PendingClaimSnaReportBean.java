/**
 * 
 */
package com.project.bsky.bean;

import java.util.List;

import com.project.bsky.model.HospitalInformation;

/**
 * @author priyanka.singh
 *
 */
public class PendingClaimSnaReportBean {

	private Integer snoId;
	private Integer hospitalCode;
	private String hospitalName;
	private String claimNo;
	private String cpdName;
	private String allotedDate;
	private String packageCode;
	private String packageName;
	private String Urn;
	private String appliedType;
	private HospitalInformation hospitalInfo;
	private String snoName;
	private Integer SnoUserId;
	private String claimId;
	private String totalAmountClaimed;
	private String authorizedcode;

	public String getAuthorizedcode() {
		return authorizedcode;
	}

	public void setAuthorizedcode(String authorizedcode) {
		this.authorizedcode = authorizedcode;
	}

	public Integer getSnoId() {
		return snoId;
	}

	public String getUrn() {
		return Urn;
	}

	public void setUrn(String Urn) {
		this.Urn = Urn;
	}

	public void setSnoId(Integer snoId) {
		this.snoId = snoId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public HospitalInformation getHospitalInfo() {
		return hospitalInfo;
	}

	public void setHospitalInfo(HospitalInformation hospitalInfo) {
		this.hospitalInfo = hospitalInfo;
	}

	public Integer getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(Integer hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getSnoName() {
		return snoName;
	}

	public void setSnoName(String snoName) {
		this.snoName = snoName;
	}

	public Integer getSnoUserId() {
		return SnoUserId;
	}

	public void setSnoUserId(Integer snoUserId) {
		SnoUserId = snoUserId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String string) {
		this.claimId = string;
	}

	public String getTotalAmountClaimed() {
		return totalAmountClaimed;
	}

	public void setTotalAmountClaimed(String totalAmountClaimed) {
		this.totalAmountClaimed = totalAmountClaimed;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getCpdName() {
		return cpdName;
	}

	public void setCpdName(String cpdName) {
		this.cpdName = cpdName;
	}

	public String getAllotedDate() {
		return allotedDate;
	}

	public void setAllotedDate(String allotedDate) {
		this.allotedDate = allotedDate;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getAppliedType() {
		return appliedType;
	}

	public void setAppliedType(String appliedType) {
		this.appliedType = appliedType;
	}

	@Override
	public String toString() {
		return "PendingClaimSnaReportBean [snoId=" + snoId + ", hospitalCode=" + hospitalCode + ", hospitalName="
				+ hospitalName + ", claimNo=" + claimNo + ", cpdName=" + cpdName + ", allotedDate=" + allotedDate
				+ ", packageCode=" + packageCode + ", packageName=" + packageName + ", Urn=" + Urn + ", appliedType="
				+ appliedType + ", hospitalInfo=" + hospitalInfo + ", snoName=" + snoName + ", SnoUserId=" + SnoUserId
				+ ", claimId=" + claimId + ", totalAmountClaimed=" + totalAmountClaimed + ", authorizedcode="
				+ authorizedcode + "]";
	}

}
