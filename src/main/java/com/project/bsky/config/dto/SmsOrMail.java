package com.project.bsky.config.dto;

import lombok.Data;

@Data
public class SmsOrMail {
	private Integer intMessageConfigType;
	private String vchSmsTemplateId;
	private String vchMessageContent;
	private String intMailTemplate;
}
