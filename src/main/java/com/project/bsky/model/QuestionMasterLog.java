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
@Table(name = "QUESTION_MASTER_LOG")
public class QuestionMasterLog {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "QUESTION_LOG_ID", nullable = false)
	private Long questionlogId;
	
	@Column(name = "QUESTION_ID", nullable = false)
	private Long questionId;

	@Column(name = "QUESTION_NAME", length = 500)
	private String questionName;

	@Column(name = "QUESTION_TYPE")
	private Integer questionType;

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
	
	@Column(name = "MANDOTORY_STATUS")
	private Integer mandotoryRemark;
}
