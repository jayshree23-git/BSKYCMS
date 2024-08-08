package com.project.bsky.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "M_ONLINE_POST_CONFIGURATION")
public class OnlinePostConfigModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_ONLINE_POST_CONFIGURATION_ID_SEQ ")
	@SequenceGenerator(name = "M_ONLINE_POST_CONFIGURATION_ID_SEQ ", sequenceName = "M_ONLINE_POST_CONFIGURATION_ID_SEQ ", allocationSize = 1)
	@Column(name = "CONFIGURATIONID")
	private Long configid;
	
	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "POST_ID")
	private Long postid;

	
	@Column(name = "CURRENT_JOB_DESCRIPTION")
	private String currentjobdescription;
	
	
	@Column(name = "ONLINE_APPLY_FROM")
	private Date onlineapplyfrom;
	
	@Column(name = "ONLINE_APPLY_TO")
	private Date onlineapplyto;
	
	@Column(name = "DOCUMENT_UPLOAD")
	private String docupload;
	
	@Column(name = "ADVERTISEMENT_NUMBER")
	private String advertisementnumb;
	
	@Column(name = "ADVERTISEMENT_DATE")
	private Date advertisementdate;
	
	@Column(name = "NO_OF_VACANCY")
	private Integer noofvaccancy;
	
	@Column(name = "CREATEDBY")
	private Long createdBy;

	@CreationTimestamp
	@Column(name = "CREATEDON")
	private Date createdOn;
	
	@Column(name = "UPDATEDBY")
	private Long updatedBy;

	@Column(name = "UPDATEDON")
	private Date updatedOn;

	@Column(name = "ONLINE_PUBLISH")
	private Long onlinepublish;
	
	@Column(name = "STATUSFLAG")
	private Long bitStatus;
	
	@Transient
	private String applyform;
	
	@Transient
	private String applyto;
	
	@Transient
	private String advertise;
}
