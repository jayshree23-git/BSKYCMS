/**
 * 
 */
package com.project.bsky.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "SURVEY_MASTER")
public class SurveyMaster {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
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
	
	@Transient
	private String sfromdate;
	
	@Transient
	private String stodate;
	
	@Transient
	private String screatedOn;
	
	@Transient
	private Integer endstatus;

}
