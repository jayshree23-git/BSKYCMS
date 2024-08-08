package com.project.bsky.bean;

import java.io.Serializable;

public class Dctaggedhospitalwise implements Serializable{
	private String Hospitaname;
	private String Hospitalcode;
	public String getHospitaname() {
		return Hospitaname;
	}
	public void setHospitaname(String hospitaname) {
		Hospitaname = hospitaname;
	}
	public String getHospitalcode() {
		return Hospitalcode;
	}
	public void setHospitalcode(String hospitalcode) {
		Hospitalcode = hospitalcode;
	}
	@Override
	public String toString() {
		return "Dctaggedhospitalwise [Hospitaname=" + Hospitaname + ", Hospitalcode=" + Hospitalcode + "]";
	}
	
}
