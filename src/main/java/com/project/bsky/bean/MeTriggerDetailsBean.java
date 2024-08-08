/**
 * 
 */
package com.project.bsky.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author santanu.barad
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeTriggerDetailsBean {
	private Long slNo;

	private String urn;

	private String claimNo;

	private String caseNo;

	private String patientName;

	private String phoneNo;

	private String hospitalName;

	private String hospitalCode;

	private String packageCode;

	private String packageName;

	private String actualDateOfAdmission;

	private String actualDateOfDischarge;

	private Long claimAmount;

	private String reportName;

	private Long claimId;

	private Long trnsactionDetailsId;

	private Long txnPkgDetailsId;

	private Date createdOn;

	private Character statusFlag;

	private String doctorRegNo;

	private Date surgeryDate;
}
