package com.project.bsky.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="m_sms_template")
public class MSmsTemplate {
	@Id
	private Integer intTemplateId;
	private Integer intConfigId;
	private String vchProjectName;
	private String vchTemplateName;
	private String vchTemplateDltId;
	private String vchHeader;
	private String vchCommunicationType; 
	private String vchType;
	private String txtContent;
	private Integer intStatus;
	private Integer bitDeletedFlag;
}
