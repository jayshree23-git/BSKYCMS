package com.project.bsky.bean;

public class SnoClaimAction {

	private long claimid;
	private long TransactionDetailsId;
	private String URN;
	public long getClaimid() {
		return claimid;
	}
	public void setClaimid(long claimid) {
		this.claimid = claimid;
	}
	public long getTransactionDetailsId() {
		return TransactionDetailsId;
	}
	public void setTransactionDetailsId(long transactionDetailsId) {
		TransactionDetailsId = transactionDetailsId;
	}
	
	public String getURN() {
		return URN;
	}
	public void setURN(String uRN) {
		URN = uRN;
	}
	@Override
	public String toString() {
		return "SnoClaimAction [claimid=" + claimid + ", TransactionDetailsId=" + TransactionDetailsId + ", URN=" + URN
				+ "]";
	}
	
}
