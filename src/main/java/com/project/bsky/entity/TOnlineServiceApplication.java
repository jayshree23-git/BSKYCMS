package com.project.bsky.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="t_online_service_application")
public class TOnlineServiceApplication implements Serializable {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="t_onl_ser_application_seq")
	@SequenceGenerator(name="t_onl_ser_application_seq", sequenceName="t_onl_ser_application_seq", allocationSize=1)
	private Integer intOnlineServiceId;
	private Integer intProfileId; 
	private Integer intProcessId; 
	private Integer intApplyStatus;
	private Integer intCreatedBy;
	private Date dtmCreatedOn;
	private Integer intUpdatedBy;
	private Date stmUpdatedOn;
	private Byte bitDeletedFlag;
	private Integer intApplicationStatus;
	private String vchApplicationNo;
	private Byte tinResubmitStatus;
	private Byte tinQueryStatus;
	private String vchRemark;
	private Byte tinApprovalStatus;
}
