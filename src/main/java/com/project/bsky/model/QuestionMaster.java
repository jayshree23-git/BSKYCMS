package com.project.bsky.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

import java.util.Date;

/*
 * questionType / QUESTION_TYPE
 * 1=Radio button
 * 2=Text field
 * 3=Date
 * 
 * MANDOTORY_REMARK / mandotoryremark
 * 0=YES
 * 1=NO
 */
@Data
@Entity
@Table(name = "QUESTION_MASTER")
public class QuestionMaster {

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
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
