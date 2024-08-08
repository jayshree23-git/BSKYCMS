/**
 * 
 */
package com.project.bsky.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author priyanka.singh
 *
 */
@Entity
@Table(name = "EMP_MST_FACILITYDETAILS")
public class FacilityDetails implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc") // for oracle sequence generate
	@Column(name = "FACILITYDETAILS_ID")
	private Integer facilityDetailId;

	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "FACILITYDETAILS_NAME")
	private String facilityName;

	@Column(name = "STATUSFLAG")
	private Integer statusFlag;
	
	@Column(name = "CREATEDON")
	private Date createdOn;
	
//	@Column(name = "CREATEDBY")
//	private Integer createdBy;
	
	@ManyToOne
	@JoinColumn(name = "CREATEDBY")
	private UserDetails createdBy;

//	@Column(name = "UPDATEDBY")
//	private Integer updatedBy;
	
	@ManyToOne
	@JoinColumn(name = "UPDATEDBY")
	private UserDetails updatedBy;
	
	@Column(name = "UPDATEDON")
	private Date updatedOn;

	public Integer getFacilityDetailId() {
		return facilityDetailId;
	}

	public void setFacilityDetailId(Integer facilityDetailId) {
		this.facilityDetailId = facilityDetailId;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public Integer getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public UserDetails getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserDetails createdBy) {
		this.createdBy = createdBy;
	}

	public UserDetails getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(UserDetails updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Override
	public String toString() {
		return "FacilityDetails [facilityDetailId=" + facilityDetailId + ", facilityName=" + facilityName
				+ ", statusFlag=" + statusFlag + ", createdOn=" + createdOn + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + ", updatedOn=" + updatedOn + "]";
	}

	
	
		

}
