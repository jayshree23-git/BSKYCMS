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
public class ExpiredBeneficiaryBean {

	private String claimId;
	private String urn;
	private String memberId;
	private String fromdate;
	private String todate;
	private String stat;
	private String dist;
	private String hospitalCode;
	private String userId;
	
}
