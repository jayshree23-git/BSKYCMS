/**
 * 
 */
package com.project.bsky.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author hrusikesh.mohanty
 *
 */

@Entity
@Table(name = "PACKAGEMASTERBSKY")
public class PackageMaster implements Serializable{
	
	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "PROCEDURECODE")
	private String procedurecode;
	
	@Column(name = "PROCEDURES")
	private String procedures;

	@Column(name = "STATUS_FLAG")
    private int statusflag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProcedurecode() {
		return procedurecode;
	}

	public void setProcedurecode(String procedurecode) {
		this.procedurecode = procedurecode;
	}

	public String getProcedures() {
		return procedures;
	}

	public void setProcedures(String procedures) {
		this.procedures = procedures;
	}

	public int getStatusflag() {
		return statusflag;
	}

	public void setStatusflag(int statusflag) {
		this.statusflag = statusflag;
	}

	@Override
	public String toString() {
		return "PackageMaster [id=" + id + ", procedurecode=" + procedurecode + ", procedures=" + procedures
				+ ", statusflag=" + statusflag + "]";
	}
}
