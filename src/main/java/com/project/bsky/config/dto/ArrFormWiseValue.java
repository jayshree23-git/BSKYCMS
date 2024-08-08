package com.project.bsky.config.dto;

import java.util.List;

import org.json.JSONArray;

import lombok.Data;

@Data
public class ArrFormWiseValue {
	private String ctrlName;
	private Integer ctrlTypeId;
	private String ctrlValue;
	private List<JSONArray> addMoreDetails;
	private String ctrlHeadingType;
}
