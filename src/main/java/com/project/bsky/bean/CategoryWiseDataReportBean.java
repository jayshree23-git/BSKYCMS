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
public class CategoryWiseDataReportBean {
	private Integer id;
	private String stateName;
	private String reserved;
	private String open;
	private String referal;
	private String claimedAmount;
	private String hospitalCode;
	private String hospitalName;

}
