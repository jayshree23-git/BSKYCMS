package com.project.bsky.config.dto;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.ALWAYS)
public class Allformdetails {

	private Integer secSlno;
	private String sectionName;
	private Integer sectionid;
	private JSONArray formDetails;
	private Byte gridType;
	private JSONObject addMoreValueDetails;
	private String dataValue;
}
