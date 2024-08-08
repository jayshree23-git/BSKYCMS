/**
 * 
 */
package com.project.bsky.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author santanu.barad
 *
 */
@Entity
@Table(name = "TBL_ME_TRIGGER_DETAILS")
@Data
public class MeTriggerDetails {
	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "SLNO")
	private Long slNo;

	@Column(name = "URN")
	private String urn;

	@Column(name = "CLAIMNO")
	private String claimNo;

	@Column(name = "CASENO")
	private String caseNo;

	@Column(name = "PATIENTNAME")
	private String patientName;

	@Column(name = "PHONENO")
	private String phoneNo;

	@Column(name = "HOSPITALNAME")
	private String hospitalName;

	@Column(name = "HOSPITALCODE")
	private String hospitalCode;

	@Column(name = "PACKAGECODE")
	private String packageCode;

	@Column(name = "PACKAGENAME")
	private String packageName;

	@Column(name = "ACTUALDATEOFADMISSION")
	private Date actualDateOfAdmission;

	@Column(name = "ACTUALDATEOFDISCHARGE")
	private Date actualDateOfDischarge;

	@Column(name = "HOSPITALCLAIMAMOUNT")
	private Long claimAmount;

	@Column(name = "REPORT_NAME")
	private String reportName;

	@Column(name = "CLAIMID")
	private Long claimId;

	@Column(name = "TRANSACTIONDETAILSID")
	private Long trnsactionDetailsId;

	@Column(name = "TXNPACKAGEDETAILSID")
	private Long txnPkgDetailsId;

	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "STATUS_FLAG")
	private Character statusFlag;

	@Column(name = "DOCTOR_REGNO")
	private String doctorRegNo;

	@Column(name = "SURGERY_DATE")
	private Date surgeryDate;
}
