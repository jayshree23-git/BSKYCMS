package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SCHEMEMASTER")
public class Scheme implements Serializable{

	/**
	 * @author arabinda.guin
	 * @purpose Scheme Master Entity
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "SCHEMEID")
	private Integer schemeId;

	@Column(name = "SCHEMENAME")
	private String schemeName;

	@Column(name = "DESCRIPTION")
	private String description;
	
	
	@Column(name = "CREATEDON")
	private Date createdOn;
	
	@Column(name = "CREATEDBY")
	private Integer createdBy;
	
	@Column(name = "UPDATEDON")
	private Date updatedOn;
	
	@Column(name = "UPDATEDBY")
	private Integer updatedBy;
	
	@Column(name = "STATUSFLAG")
	private Integer statusFlag;

	public Integer getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}

	@Override
	public String toString() {
		return "Scheme [schemeId=" + schemeId + ", schemeName=" + schemeName + ", description=" + description
				+ ", createdOn=" + createdOn + ", createdBy=" + createdBy + ", updatedOn=" + updatedOn + ", updatedBy="
				+ updatedBy + ", statusFlag=" + statusFlag + "]";
	}
}
