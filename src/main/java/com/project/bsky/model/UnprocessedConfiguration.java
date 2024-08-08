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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author priyanka.singh
 *
 */

@Entity
@Table(name = "MST_UNPROCESSED_CONFIGURATION")
public class UnprocessedConfiguration implements Serializable{
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "UNPROCESSED_ID")
	private Long unprocessedId;
	
	@Column(name = "YEARS")
	private Long years;
	
	@Column(name = "MONTHS")
	private Long months;
	
	@Column(name = "UNPROCESSED_DATE")
	private Date unprocessDate;
	
	@Column(name = "STATUSFLAG")
	private Integer statusFlag;
	
	@Column(name = "CREATED_ON")
	private Date createdOn;
	
	@Column(name = "UPDATED_ON")
	private Date updatedOn;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;

//	@ManyToOne
//	@JoinColumn(name = "CREATED_BY")
//	private UserDetails createdBy;
//	
	@Column(name = "UPDATED_BY")
	private Long updatedBy;

	@Transient
	private String fdate;
	
	@Transient
	private String screate;

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

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public String getScreate() {
		return screate;
	}

	public void setScreate(String screate) {
		this.screate = screate;
	}

	public Integer getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(Integer statusFlag) {
		this.statusFlag = statusFlag;
	}

	@Override
	public String toString() {
		return "UnprocessedConfiguration [unprocessedId=" + unprocessedId + ", years=" + years + ", months=" + months
				+ ", unprocessDate=" + unprocessDate + ", statusFlag=" + statusFlag + ", createdOn=" + createdOn
				+ ", updatedOn=" + updatedOn + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", fdate="
				+ fdate + ", screate=" + screate + "]";
	}

	
	

	


	
	
	

}
