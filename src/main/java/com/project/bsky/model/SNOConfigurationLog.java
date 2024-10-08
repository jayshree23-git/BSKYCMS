package com.project.bsky.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.ToString;

/**
 * @author ronauk
 *
 */
@Entity
@Table(name = "USER_SNA_MAPPING_LOG")
@ToString
public class SNOConfigurationLog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sna_mapping_LOG_ID_SEQ")
	@SequenceGenerator(name = "sna_mapping_LOG_ID_SEQ", sequenceName = "sna_mapping_LOG_ID_SEQ", allocationSize = 1)
	@Column(name = "SNA_MAPPING_LOG_ID")
	private Integer logId;

	@Column(name = "SNA_MAPPING_ID")
	private Integer mappingId;

	@Column(name = "SNO_USER_ID")
	private Integer snoUserId;

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

	@Column(name = "SYSTEM_IP")
	private String systemIp;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getMappingId() {
		return mappingId;
	}

	public void setMappingId(Integer mappingId) {
		this.mappingId = mappingId;
	}

	public Integer getSnoUserId() {
		return snoUserId;
	}

	public void setSnoUserId(Integer snoUserId) {
		this.snoUserId = snoUserId;
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

	public String getSystemIp() {
		return systemIp;
	}

	public void setSystemIp(String systemIp) {
		this.systemIp = systemIp;
	}

}
