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
public class HospitalWiseBean {
	
	private String stateName;
	private String stateCode;
	private String districtName;
	private String hospitalName;
	private String hospitalCode;
	private String specialityCode;
	private String specialityName;
	private String packageCode;
	private String packageame;
	private String totalPackageClaimed;
	private String totalClaimedAmt;

}
