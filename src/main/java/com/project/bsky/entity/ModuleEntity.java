package com.project.bsky.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
@Entity
@Table(name = "m_module")
public class ModuleEntity {
	
	@Id
	@Column(name="intModuleId")
	private int intModuleId;
	
	@Column(name="vchModuleName")
	private String vchModuleName;
	
	@Column(name="vchModuleLink")
	private String vchModuleLink;
	
	@Column(name="vchImage")
	private String vchImage;
	
	@Column(name="vchDescription")
	private String vchDescription;
	
	@Column(name="tinPublishStatus")
	private int tinPublishStatus;
	
	@Column(name="dtmCreatedOn")
	private Date dtmCreatedOn;
	
	@Column(name="dtmUpdatedOn")
	private Date dtmUpdatedOn;
	
	@Column(name="intCreatedBy")
	private int intCreatedBy;
	
	@Column(name="intUpdatedBy")
	private int intUpdatedBy;
	
	@Column(name="bitDeletedflag")
	private int bitDeletedflag;
	
	@Column(name="vchSlugName")
	private int vchSlugName;
	


}
