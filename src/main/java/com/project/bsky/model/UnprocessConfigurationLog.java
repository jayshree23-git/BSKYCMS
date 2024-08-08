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
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author priyanka.singh
 *
 */
@Data
@Entity
@Table(name = "MST_UNPROCESSED_CONFIGURATION_LOG")
public class UnprocessConfigurationLog  implements Serializable{
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "LOG_ID")
	private Integer logId;
	
	@Column(name = "UNPROCESSED_ID")
	private Long unprocessedId;
	
	@Column(name = "YEARS")
	private Long years;
	
	@Column(name = "MONTHS")
	private Long months;
	
	@Column(name = "UNPROCESSED_DATE")
	private Date unprocessDate;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	@Column(name = "UPDATED_ON")
	private Date updatedOn;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "STATUSFLAG")
	private Integer statusFlag;
	
	@Column(name = "UPDATED_BY")
	private Long updateby;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Long getUnprocessedId() {
		return unprocessedId;
	}

	public void setUnprocessedId(Long unprocessedId) {
		this.unprocessedId = unprocessedId;
	}

	public Long getYears() {
		return years;
	}

	public void setYears(Long years) {
		this.years = years;
	}

	public Long getMonths() {
		return months;
	}

	public void setMonths(Long months) {
		this.months = months;
	}

	public Date getUnprocessDate() {
		return unprocessDate;
	}

	public void setUnprocessDate(Date unprocessDate) {
		this.unprocessDate = unprocessDate;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}

	@Override
	public String toString() {
		return "UnprocessConfigurationLog [logId=" + logId + ", unprocessedId=" + unprocessedId + ", years=" + years
				+ ", months=" + months + ", unprocessDate=" + unprocessDate + ", createdOn=" + createdOn
				+ ", updatedOn=" + updatedOn + ", createdBy=" + createdBy + ", statusFlag=" + statusFlag + "]";
	}
	
	

}
