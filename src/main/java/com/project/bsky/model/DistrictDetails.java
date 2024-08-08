package com.project.bsky.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DISTRICTMOSARKAR")
public class DistrictDetails {
	@Id
	@Column(name = "DISTRICTID")
	private Integer DistrictId;

	@Column(name = "STATECODE")
	private String statecode;

	@Column(name = "DISTRICTCODE")
	private String districtcode;

	@Column(name = "DISTRICTNAME")
	private String districtname;

	@Column(name = "DISTRICTCODEBSKY")
	private String districtcodebsky;

	@Column(name = "DISTRICTNAMEBSKY")
	private String districtnamebsky;

	@Column(name = "S1")
	private String S1;

	@Column(name = "S2")
	private String S2;
	
	@Column(name = "STATE_ID")
	private Integer stateId;

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public Integer getDistrictId() {
		return DistrictId;
	}

	public void setDistrictId(Integer districtId) {
		DistrictId = districtId;
	}

	public String getStatecode() {
		return statecode;
	}

	public void setStatecode(String statecode) {
		this.statecode = statecode;
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

	public String getDistrictcodebsky() {
		return districtcodebsky;
	}

	public void setDistrictcodebsky(String districtcodebsky) {
		this.districtcodebsky = districtcodebsky;
	}

	public String getDistrictnamebsky() {
		return districtnamebsky;
	}

	public void setDistrictnamebsky(String districtnamebsky) {
		this.districtnamebsky = districtnamebsky;
	}

	public String getS1() {
		return S1;
	}

	public void setS1(String s1) {
		S1 = s1;
	}

	public String getS2() {
		return S2;
	}

	public void setS2(String s2) {
		S2 = s2;
	}

	@Override
	public String toString() {
		return "DistrictDetails [DistrictId=" + DistrictId + ", statecode=" + statecode + ", districtcode="
				+ districtcode + ", districtname=" + districtname + ", S1=" + S1 + ", S2=" + S2 + "]";
	}

}
