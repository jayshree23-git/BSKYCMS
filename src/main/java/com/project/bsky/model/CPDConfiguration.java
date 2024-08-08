/**
 * 
 */
package com.project.bsky.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;

/**
 * @author ronauk
 *
 *	Status/STATUS_FLAG
 *	0-Active
 *	1-In Active
 *	2-Request For Inclusion
 *	3-Request For Exclusion
 *	4-Cancel
 */
@Data
@ToString
@Entity
@Table(name = "USER_CPD_MAPPING")
public class CPDConfiguration {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "CPD_MAPPING_ID")
	private Integer mappingId;

	@Column(name = "CPD_USER_ID")
	private Integer cpdUserId;

	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;

	@Column(name = "DISTRICT_CODE")
	private String districtCode;

	@Column(name = "STATE_CODE")
	private String stateCode;

	@Column(name = "STATUS_FLAG")
	private Integer status;

	@Column(name = "CREATED_BY")
	private Integer createdBy;

	@Column(name = "CREATED_ON")
	private Timestamp createdOn;

	@Column(name = "UPDATED_BY")
	private Integer updatedBy;

	@Column(name = "UPDATED_ON")
	private Timestamp updatedOn;
	
	public Integer getMappingId() {
		return mappingId;
	}

	public void setMappingId(Integer mappingId) {
		this.mappingId = mappingId;
	}

	public Integer getCpdUserId() {
		return cpdUserId;
	}

	public void setCpdUserId(Integer cpdUserId) {
		this.cpdUserId = cpdUserId;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	

}
