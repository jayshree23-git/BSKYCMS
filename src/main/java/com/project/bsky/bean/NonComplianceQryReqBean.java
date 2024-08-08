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
public class NonComplianceQryReqBean {

	private long rejectionId;
	private long transactionDetailsId;
	private String urn;
	private String patientName;
	private String packageCode;
	private String currentTotalAmount;
	private Date createdOn;
	private String packageName;
	private String hospitalcode;
	private String authorizedcode;
	private Date lastQueryOn;
	private String claimNo;
}
