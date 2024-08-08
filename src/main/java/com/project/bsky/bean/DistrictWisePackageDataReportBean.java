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
public class DistrictWisePackageDataReportBean {
	
	private Integer id;
	private String stateName;
	private String districtcode;
	private String hositalCode;
	private String hospitalName;
	private String districtName;
	private String specialityName;
	private String specialityCode;
	private String packageName;
	private String packageCode;
	private Long totalAmtClaimed;
	private Long totalAmtBlocked;

}
