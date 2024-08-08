package com.project.bsky.config.dto;

import lombok.Data;

@Data
public class MessagePassingDto {
	private Integer intMessageConfigId;
	private Integer intMessageConfigType;
	private Integer intProcessId;
	private Integer formId;
	private String vchSmsTemplateId;
	private Integer intmessageType;
	private String dtmCreatedOn;
	private Boolean bitDeletedFlag;
	private Integer tinPublishStatus;
	private Integer SchedularStatus;
	private String stopExecutionDate;
	private String vchProcessName;
}
