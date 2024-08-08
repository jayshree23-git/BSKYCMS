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
public class UrnWiseActionBean {
	
	private Long id;
	private String urn;
	private String patientName;
	private String invoiceNumber;
	private String caseNo;
	private String hospitalName;
	private String hospitalCode;
	private String actualDateDischarge;
	private String actualDateAdmission;
	private String claimStatus;
	private String claimRaiseStatus;
	private String claimNo;
	private String transactionId;
	private String claimId;
	private String createdon;

	

}
