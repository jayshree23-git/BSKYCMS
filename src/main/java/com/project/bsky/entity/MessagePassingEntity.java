package com.project.bsky.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="m_dyn_message_configuration")
public class MessagePassingEntity implements Serializable {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intMessageConfigId;
	private Integer intProcessId;
	private Integer intMessageConfigType;
	private String vchSmsTemplateId;
	private Integer intmessageType;
	private Integer intEventType;
	private String vchSubject;
	private String vchMessageContent;
	private Integer tinPublishStatus;
	private Boolean bitDeletedFlag;
	private Date dtmCreatedOn;
	private Date dtmUpdatedOn;
	private Integer intCreatedBy;
	private Integer intUpdatedBy;
	private Date dtmStartDate;
	private Date dtmEndDate;
	private String frequencyType;
	private Integer frequencyDuration;
	private String schedularUrl;
	private Integer SchedularStatus;
	private Date stopExecutionDate;
	private Integer intDocumentType;
	private String vchDocument;
	private String intMailTemplate;
}
