package com.project.bsky.config.dto;

import org.json.JSONArray;

import lombok.Data;
@Data
public class ResponseForPreview {
	private JSONArray arrFormWiseValue;
	private Integer secSlno;
	private String sectionId;
	private String sectionName;
}
