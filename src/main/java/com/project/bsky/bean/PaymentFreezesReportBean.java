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
public class PaymentFreezesReportBean {
	
	private String hospitalName;
	private String hospitalCode;
	private String snaName;
	private String totalClaimed;
	private String totlAmountClaimed;
	private String totalFrezzed;
	private String totalFreezAmount;
	private String totalPostPaymntUpdation;
	private String totalPostPaymntUpdationAmt;

}
