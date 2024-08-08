package com.project.bsky.bean;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class FloatExcelBean {

	private Long userId;
	private Date fromDate;
	private Date toDate;
	private String stateId;
	private String districtId;
	private String hospitalId;
	private String mortality;
	private List<String> heading;
	private List<List<Object>> report;
	private String filename;
	private String stateName;
	private String districtName;
	private String hospitalName;
	private Long createdBy;
	private Long searchtype;
	private String schemecategoryid;
	private String actionName;
	
}
