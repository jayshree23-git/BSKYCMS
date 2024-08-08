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
public class SNAWiseDischargeandClmReportBean {
	
	private Integer id;
	private String monthName;
	private String totalDischarge;
	private String dischargeAmt;
	private String clmSubmitted;
	private String clmSubmitAmt;
	private String totalPaid;
	private String paidAmount;
	private String snoid;
	private String snoname;
	private String hospname;
	private String hospcode;

}
