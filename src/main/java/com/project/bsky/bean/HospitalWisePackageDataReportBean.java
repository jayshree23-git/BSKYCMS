/**
 * 
 */
package com.project.bsky.bean;

import lombok.Data;

/**
 * @author priyanka.singh
 *
 */
@Data
public class HospitalWisePackageDataReportBean {
	
	private Integer id;
	private String userId;
	private String stateName;
	private String stateCode;
	private String districtCode;
	private String districtName;
	private String hospitalName;
	private String hospitalCode;
	private String packageCode;
	private String packageName;
	private String totalpackageClaimed;
	private String procedureCode;
	private String specialityCode;
	private String specialityName;
	private String totalPackageAmt;
	

}
