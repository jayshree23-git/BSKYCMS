package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author ronauk
 *
 */
@Entity
@Table(name = "OLD_FLOAT_REPORT_LOG")
public class OldFloatReportLog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_old_float_report_log_id")
	@SequenceGenerator(name = "seq_old_float_report_log_id", sequenceName = "seq_old_float_report_log_id", allocationSize = 1)
	@Column(name = "FLOAT_REPORT_LOG_ID")
	private Integer logId;

	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "ACTUAL_DATE_OF_DISCHARGE_FROM")
	private Date actualDateOfDischargeFrom;

	@Column(name = "ACTUAL_DATE_OF_DISCHARGE_TO")
	private Date actualDateOfDischargeTo;

	@Column(name = "STATE_CODE")
	private String stateCode;

	@Column(name = "DISTRICT_CODE")
	private String districtCode;

	@Column(name = "HOSPITAL_CODE")
	private String hospitalCode;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "CREATED_BY")
	private Long createdBy;

	@Column(name = "STATUSFLAG")
	private Integer statusflag;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getActualDateOfDischargeFrom() {
		return actualDateOfDischargeFrom;
	}

	public void setActualDateOfDischargeFrom(Date actualDateOfDischargeFrom) {
		this.actualDateOfDischargeFrom = actualDateOfDischargeFrom;
	}

	public Date getActualDateOfDischargeTo() {
		return actualDateOfDischargeTo;
	}

	public void setActualDateOfDischargeTo(Date actualDateOfDischargeTo) {
		this.actualDateOfDischargeTo = actualDateOfDischargeTo;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getStatusflag() {
		return statusflag;
	}

	public void setStatusflag(Integer statusflag) {
		this.statusflag = statusflag;
	}

	@Override
	public String toString() {
		return "OldFloatReportLog [logId=" + logId + ", userId=" + userId + ", fileName=" + fileName
				+ ", actualDateOfDischargeFrom=" + actualDateOfDischargeFrom + ", actualDateOfDischargeTo="
				+ actualDateOfDischargeTo + ", stateCode=" + stateCode + ", districtCode=" + districtCode
				+ ", hospitalCode=" + hospitalCode + ", createdOn=" + createdOn + ", createdBy=" + createdBy
				+ ", statusflag=" + statusflag + "]";
	}

}
