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
public class ReportCountBean {

	private Long TotalclaimRasied;
	private Long SNAApproved;
	private Long pendingatHsptl;
	private Long pendingatCPD;
	private Long pendingatSNA;
	private Long pendingatDC;
	private Long SNARejected;
	private Long TotalDischarge;
	private Long NonUploading;
	private Long CPDApproved;
	private Long CPDRejected;
	private Long nonUploadingInit;
	private Long snaQuery;
	private Long pendatcpdRstl;
	private Long pendatcpdRvrt;
	private Long systemRejected;
	private Long unprocessed;
	private Long resettlement;
	private Double totaldischargeamount;
	private Double nonuploadingamount;
	private Double nonuploadinginitamount;
	private Double totalclaimraisedamount;
	private Double pendingathospitalamount;
	private Double snaqueryamount;
	
}
