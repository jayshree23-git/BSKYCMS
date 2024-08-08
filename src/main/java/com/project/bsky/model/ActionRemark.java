/**
 * 
 */
package com.project.bsky.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author arabinda.guin
 *
 */

/**
 * @author Priyanka Singh
 * Status/STATUS_FLAG
 *	1-CPD Remark
 *	2-SNA Remark
 *	3-Both SNA and CPD
 *
 */


@Entity
@Table(name = "ACTIONREMARK")
public class ActionRemark {
	
	@Id
	@Column(name = "REMARK_ID")
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	private Long id;
	@Column(name = "REMARK")
	private String remarks;
	
	@Column(name = "STATUSFLAG")
	private Integer statusFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}

	@Override
	public String toString() {
		return "ActionRemark [id=" + id + ", remarks=" + remarks + ", statusFlag=" + statusFlag + "]";
	}
	
	

}
