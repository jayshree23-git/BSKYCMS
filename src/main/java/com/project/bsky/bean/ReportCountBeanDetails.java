/**
 * 
 */
package com.project.bsky.bean;

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
public class ReportCountBeanDetails {
	private Long claimId;
	private String claimNo;
	private String packageName;
	private String urn;
	private String hospitalName;
	private String actDateOfAdm;
	private String actDateOfDschrg;
	private String dateOfAdm;
	private String dateOfDschrg;
	private String hospitalCode;
	private String authorizedCode;
	private String cpdName;
	private String Approvedamount;
	private String claimamount;
	private String patentname;
	private String packagecode;
	private String Invoiceno;
	private String remarks;
	private String queryDate;
	private String claimRaisedBy;
	private String alloteddate;
	private String actiontype;
	private String count;
	private String caseno; 
	private String cpdAllotedDate;
	private String cpdactiontye;
	private String snaactiontype;
	private String cpdactiondate;
	private String snaactiondate;
	private String cpdapproveamount;
	private String snaapproveamount;

}
