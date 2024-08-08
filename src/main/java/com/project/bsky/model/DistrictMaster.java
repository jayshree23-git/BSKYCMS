package com.project.bsky.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DISTRICT")
public class DistrictMaster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DISTRICTID")
	private Integer DistrictId;

	@ManyToOne
	@JoinColumn(name = "STATECODE", referencedColumnName = "STATECODE")
	private State statecode;

	@Column(name = "DISTRICTCODE")
	private String districtcode;

	@Column(name = "DISTRICTNAME")
	private String districtname;

	@Column(name = "S1")
	private String s1;

	@Column(name = "S2")
	private String s2;

	public Integer getDistrictId() {
		return DistrictId;
	}

	public void setDistrictId(Integer districtId) {
		DistrictId = districtId;
	}

	public State getStatecode() {
		return statecode;
	}

	public void setStatecode(State statecode) {
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

	public String getS1() {
		return s1;
	}

	public void setS1(String s1) {
		this.s1 = s1;
	}

	public String getS2() {
		return s2;
	}

	public void setS2(String s2) {
		this.s2 = s2;
	}

	@Override
	public String toString() {
		return "DistrictMaster [DistrictId=" + DistrictId + ", statecode=" + statecode + ", districtcode="
				+ districtcode + ", districtname=" + districtname + ", s1=" + s1 + ", s2=" + s2 + "]";
	}

}
