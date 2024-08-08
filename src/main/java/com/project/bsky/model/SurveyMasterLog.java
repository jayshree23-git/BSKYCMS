/**
 * 
 */
package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * Rajendra Prasad
 * Created by = Who create the log record;
 * updated by = main table updated by;  
 */
@Data
@Entity
@Table(name = "SURVEY_MASTER_LOG")
public class SurveyMasterLog {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "SURVEY_LOG_ID", nullable = false)
	private Long surveylogId;
	
	@Column(name = "SURVEY_ID", nullable = false)
	private Long surveyId;

	@Column(name = "SURVEY_NAME", length = 500)
	private String surveyName;

	@Column(name = "EFFECTIVE_FROM")
	@Temporal(TemporalType.DATE)
	private Date effectiveFrom;

	@Column(name = "EFFECTIVE_TO")
	@Temporal(TemporalType.DATE)
	private Date effectiveTo;

	@Column(name = "CREATED_BY")
	private Long createdBy;

	@Column(name = "CREATED_ON")
	@Temporal(TemporalType.DATE)
	private Date createdOn;

	@Column(name = "UPDATED_BY")
	private Long updatedBy;

	@Column(name = "UPDATED_ON")
	@Temporal(TemporalType.DATE)
	private Date updatedOn;

	@Column(name = "STATUSFLAG")
	private Integer statusFlag;
}
