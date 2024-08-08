package com.project.bsky.bean;

import java.util.Date;
import lombok.Data;

@Data
public class FloatReportBean {

	private Long userId;
	private Date fromDate;
	private Date toDate;
	private String stateId;
	private String districtId;
	private String hospitalId;
	private String mortality;
	private Integer action;
	private Long snaUserId;
	private Long searchtype;
	private String schemecategoryid;
}
