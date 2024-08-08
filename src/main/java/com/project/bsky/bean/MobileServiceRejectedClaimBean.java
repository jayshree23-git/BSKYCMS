/**
 * 
 */
package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author preetam.mishra
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileServiceRejectedClaimBean {
	private String patientName;
	private String urn;
	private String claimDate;
	private String claimedAmount;
	private String claimStatus;
}
