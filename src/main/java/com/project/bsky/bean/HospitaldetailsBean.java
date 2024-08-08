package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitaldetailsBean {

	private long userId;
	private String hospitalCode;
	private String hospitalName;
	private String stateName;
	private String districtName;
	private String nonUploadingFlag;
	private String nonComplianceFlag;
}
