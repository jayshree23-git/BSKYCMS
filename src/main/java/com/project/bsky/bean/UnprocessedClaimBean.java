/**
 * 
 */
package com.project.bsky.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author arabinda.guin
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnprocessedClaimBean {
	
	private long claimid;
	private long transactionDetailsId;
	private String urn;
	private String patientName;
	private String packageCode;
	private String invoiceNumber;
	private String currentTotalAmount;
	private String createdOn;
	private String packageName;
	private Date revisedDate;
	private Date cpdAlotteddate;
	private String claimNo;
	private String hospitalName;
	private String cpdMortality;
	private String hosMortality;
	private String authorizedCode;
	private String Actualdateofdischarge;
	private String Actualdateofaddmission;
	private String hospitalcode;
	private String phone;
	private Long triggerValue;
	private String triggerMsg;

}
