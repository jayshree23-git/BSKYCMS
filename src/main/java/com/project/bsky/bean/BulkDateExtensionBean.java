package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkDateExtensionBean {
	
	private String claimBy;
	private String actionId;
	private Long createdBy;
	private String fromDate;
	private String toDate;
	private String stateCode;
	private String districtCode;
	private String hospitalCode;
	private Long snoid;
	private String statusflag;

}
