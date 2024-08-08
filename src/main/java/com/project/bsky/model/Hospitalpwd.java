package com.project.bsky.model;

import java.io.Serializable;


/**
 * @author hrusikesh.mohanty
 *
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "HOSPITALPWD")
public class Hospitalpwd implements Serializable {
	@Id
	@Column(name = "HOSPITALNAME")
	private String HospitalName;
	@Column(name = "HOSPITALCODE")
	private String Hospitalcode;
	@Column(name = "PASSWORD")
	private String Password;
	@Column(name = "PWDENC")
	private String pwdenc;


	public String getHospitalName() {
		return HospitalName;
	}

	public void setHospitalName(String hospitalName) {
		HospitalName = hospitalName;
	}

	public String getHospitalcode() {
		return Hospitalcode;
	}

	public void setHospitalcode(String hospitalcode) {
		Hospitalcode = hospitalcode;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getPwdenc() {
		return pwdenc;
	}

	public void setPwdenc(String pwdenc) {
		this.pwdenc = pwdenc;
	}

	@Override
	public String toString() {
		return "Hospitalpwd{" +
			   "HospitalName='" + HospitalName + '\'' +
			   ", Hospitalcode='" + Hospitalcode + '\'' +
			   ", Password='" + Password + '\'' +
			   ", pwdenc='" + pwdenc + '\'' +
			   '}';
	}
}
