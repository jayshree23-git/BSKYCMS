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

import com.project.bsky.model.SurveyGroupMapping;

import lombok.Data;

/**
 * Rajendra Prasad Sahoo
 * 17-01-2024
 */
@Data
@Entity
@Table(name = "TBL_SURVEY_QUESTION_MAPPING ")
public class SurveyQuestionMapping {
	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
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
