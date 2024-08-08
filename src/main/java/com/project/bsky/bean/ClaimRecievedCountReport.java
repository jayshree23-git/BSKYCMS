package com.project.bsky.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimRecievedCountReport {
    private String DateOfMonth;
	private Long TotalTransactionCount;
	private Long TotalDischargedCount;
	private Long TotalClaimSubmitted;
	private Long TotalClaimPending;
	private String DateOfMonth1;
}
