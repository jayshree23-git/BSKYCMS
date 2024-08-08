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
public class SaveDischargeReports {
	
	private String pdf;
	private String userId;
	private String searchName;
	private String fromdate;
	private String todate;
	private String statename;
	private String districtName;
	private String hospitalname;
	private String showSearch;
	
	

}
