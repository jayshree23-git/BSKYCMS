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
@Table(name = "TBL_SURVEY_QUESTION_MAPPING_LOG")
public class SurveyQuestionMappingLog {
	
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "QUESTIONMAPPING_LOG_ID", nullable = false)
	private Long questionmappinglogid;
	
	@Column(name = "QUESTIONMAPPING_ID", nullable = false)
	private Long questionmappingid;
	
	@Column(name = "SURVEY_ID")
	private Long surveyId;

	@Column(name = "QUESTION_ID")
	private Long questionId;
	
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

	@Column(name = "STATUS_FLAG")
	private Integer statusFlag;
}
