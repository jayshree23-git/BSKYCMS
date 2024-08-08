package com.project.bsky.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Data
@Table(name="m_dynamic_sms_mail")
public class MDynamicSmsMail implements Serializable{
	@Id
	private Integer intDynamicSendId; 
	private Integer tinTypeId; 
	private String vchMailName;
	private String vchTemplateName; 
	private Integer intCreatedBy;
	private Date dtmCreatedOn;
	private Integer bitDeletedFlag;
	private Integer intUpdatedBy;
	private Date dtmUpdatedOn;
	private Integer tinSendStatus;
	private String vchEmailId;
	private String vchMobileNo;
	private String txtSmsContent;
	private Integer intProfileId;
	private String jsonMailContent;
	private Integer intOnlineServiceId; 
	private String jsonResponse;
}
