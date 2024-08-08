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
public class CCEMonthlyReport {
	private String userId;
	private String stateId;
	private String districtId;
	private String hospitalCode;
	private String hospitalName;
	private String totalDataRecivd;
	private String nonConnect;
	private String nonConnectPercent;
	private String succesCallCount;
	private String succesCallPercent;
	private String positiveCaseCount;
	private String negativeCaseCount;
	private String patientSatCount;
	private String negativeCountPercent;
	private String phonenegativeCount;
	private String patientPercentage;
	private String postivePercentage;
	
	

}
