package com.project.bsky.bean;

public class HospObj {

	private String stateCode;
	private String stateName;
	private String districtCode;
	private String districtName;
	private String hospitalCode;
	private String hospitalName;
	private String blockCode;
	private String blockName;

	public String getBlockCode() {
		return blockCode;
	}

	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	@Override
	public String toString() {
		return "HospObj [stateCode=" + stateCode + ", stateName=" + stateName + ", districtCode=" + districtCode
				+ ", districtName=" + districtName + ", hospitalCode=" + hospitalCode + ", hospitalName=" + hospitalName
				+ ", blockCode=" + blockCode + ", blockName=" + blockName + "]";
	}

	

}
