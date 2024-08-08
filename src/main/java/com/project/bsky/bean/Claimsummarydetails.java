package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Claimsummarydetails {
	private String dates;
	private String uniquefamilytreated;
	private String uniquepatienttreated;
	private String totalpackagedischarged;
	private String totaldischargeamount;
	private String totalclaimsubmitted;
	private String totalclaimamount;
	private String total_pendingforclaim_raise;
	private String totalpendingraiseamount;
	private String claimnotsubmittedafter7daysofdischarge;
	private String cnsamountafter7daysofdischarge;
}
