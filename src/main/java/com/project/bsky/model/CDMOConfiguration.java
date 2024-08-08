package com.project.bsky.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;


@Data
@Entity
@Table(name = "USER_CDMO_MAPPING")
public class CDMOConfiguration implements Serializable{

	@Id
	@GenericGenerator(name = "catInc", strategy = "increment")
	@GeneratedValue(generator = "catInc")
	@Column(name = "CDMO_MAPPING_ID")
	private Integer mappingId;

	@Column(name = "CDMO_USER_ID",unique=true)
	private Integer cdmoUserId;

	@Column(name = "DISTRICT_CODE",unique=true)
	private String districtCode;

	@Column(name = "STATE_CODE")
	private String stateCode;

	@Column(name = "STATUS_FLAG")
	private Integer status;

	@Column(name = "CREATED_BY")
	private Integer createdBy;

	@Column(name = "CREATED_ON")
	private Timestamp createdOn;

	@Column(name = "UPDATED_BY")
	private Integer updatedBy;

	@Column(name = "UPDATED_ON")
	private Timestamp updatedOn;
	
	@Transient
	private String name;
	
	@Transient
	private String statename;
	
	@Transient
	private String distname;
	
}
