package com.project.bsky.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PACKAGEMASTERBSKY")
public class Specialitydoctor {
	
	@Id
	@Column(name = "ID")
	private Integer ID;
	
	@Column(name = "PROCEDURECODE")
	private String ProcedureCode;
	
	@Column(name = "PROCEDURES")
	private String Procedures;

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public String getProcedureCode() {
		return ProcedureCode;
	}

	public void setProcedureCode(String procedureCode) {
		ProcedureCode = procedureCode;
	}

	public String getProcedures() {
		return Procedures;
	}

	public void setProcedures(String procedures) {
		Procedures = procedures;
	}

	@Override
	public String toString() {
		return "Specialitydoctor [ID=" + ID + ", ProcedureCode=" + ProcedureCode + ", Procedures=" + Procedures + "]";
	}
	
	


}
