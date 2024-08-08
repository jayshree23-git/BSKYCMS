/**
 * 
 */
package com.project.bsky.bean;

import java.util.Date;

import javax.persistence.Column;

/**
 * @author priyanka.singh
 *
 */
public class UnprocessedConfigurationBean {

	private Long unprocessedId;
	private Long years;
	private Long months;
	private String unprocessDate;
	private String statusFlag;
	private Date createdOn;
	private Date updatedOn;
	private String createdBy;
	private String updatedBy;
	
	
	
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
	public String getUnprocessDate() {
		return unprocessDate;
	}
	public void setUnprocessDate(String unprocessDate) {
		this.unprocessDate = unprocessDate;
	}
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public String toString() {
		return "UnprocessedConfigurationBean [unprocessedId=" + unprocessedId + ", years=" + years + ", months="
				+ months + ", unprocessDate=" + unprocessDate + ", statusFlag=" + statusFlag + ", createdOn="
				+ createdOn + ", updatedOn=" + updatedOn + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ "]";
	}

}
