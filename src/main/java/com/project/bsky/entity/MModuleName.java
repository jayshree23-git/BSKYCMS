package com.project.bsky.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="m_module_name")
public class MModuleName implements Serializable {
	@Id
	private Integer intModuleId;
	private String vchModuleName;
	private Date dtmCreatedOn; 
	private Integer intCreatedBy;
	private Date stmUpdatedOn;
	private Integer intUpdatedBy;
	private Integer bitDeletedFlag; 
	private String tinStatus;
}
