package com.project.bsky.bean;

import lombok.Data;

@Data
public class Snawiseunprocessedbean {
	private String fromDate;
	private String toDate;
	private String stateCode;
	private String districtCode;
	private String hospitalCode;
	private String actionId;
	private Long snoid;
	private Integer searchby; 
	private Integer searchtype;
	
}
