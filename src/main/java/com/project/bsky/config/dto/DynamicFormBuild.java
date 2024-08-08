package com.project.bsky.config.dto;


import java.util.Date;

import org.json.JSONArray;

import lombok.Data;

@Data
public class DynamicFormBuild {
	private Integer configurationId;
	private Integer itemId;
	private Integer sectionId;
	private JSONArray formDetails;
	private Byte status;
	private Date createdOn;
	private String vchProcessName;
	private Byte tinFinalSubmitStatus;
	private String sectionName;
}
