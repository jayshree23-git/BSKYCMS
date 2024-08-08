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
 * Rajendra Prasad Sahoo
 * 17-01-2024
 */
@Data
@Entity
@Table(name = "TBL_SURVEY_GROUP_MAPPING")
public class SurveyGroupMapping {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "GROUPMAPPING_ID", nullable = false)
	private Long groupmappingid;
	
	@Column(name = "SURVEY_ID")
	private Long surveyId;

	@Column(name = "GROUPTYPE_ID")
	private Long groupId;
	
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
