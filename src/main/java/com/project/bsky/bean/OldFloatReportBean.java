package com.project.bsky.bean;

public class OldFloatReportBean {

	private String createdOn;
	private String actualDateOfDischargeFrom;
	private String actualDateOfDischargeTo;
	private String claimStatus;
	private String stateName;
	private String districtName;
	private String hospitalName;
	private String fileName;
	private String extn;

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getActualDateOfDischargeFrom() {
		return actualDateOfDischargeFrom;
	}

	public void setActualDateOfDischargeFrom(String actualDateOfDischargeFrom) {
		this.actualDateOfDischargeFrom = actualDateOfDischargeFrom;
	}

	public String getActualDateOfDischargeTo() {
		return actualDateOfDischargeTo;
	}

	public void setActualDateOfDischargeTo(String actualDateOfDischargeTo) {
		this.actualDateOfDischargeTo = actualDateOfDischargeTo;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getExtn() {
		return extn;
	}

	public void setExtn(String extn) {
		this.extn = extn;
	}

	@Override
	public String toString() {
		return "OldFloatReportBean [createdOn=" + createdOn + ", actualDateOfDischargeFrom=" + actualDateOfDischargeFrom
				+ ", actualDateOfDischargeTo=" + actualDateOfDischargeTo + ", claimStatus=" + claimStatus
				+ ", stateName=" + stateName + ", districtName=" + districtName + ", hospitalName=" + hospitalName
				+ ", fileName=" + fileName + ", extn=" + extn + "]";
	}

}
