/**
 * 
 */
package com.project.bsky.bean;

/**
 * @author priyanka.singh
 *
 */
public class HospitalAuthorityReportBean {
	private String authTaggingId;
	private String groupId;
	private String userId;
	private String totaldischarge;
	private String claimraise;
	private String pending;
	public String getAuthTaggingId() {
		return authTaggingId;
	}
	public void setAuthTaggingId(String authTaggingId) {
		this.authTaggingId = authTaggingId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTotaldischarge() {
		return totaldischarge;
	}
	public void setTotaldischarge(String totaldischarge) {
		this.totaldischarge = totaldischarge;
	}
	public String getClaimraise() {
		return claimraise;
	}
	public void setClaimraise(String claimraise) {
		this.claimraise = claimraise;
	}
	public String getPending() {
		return pending;
	}
	public void setPending(String pending) {
		this.pending = pending;
	}
	@Override
	public String toString() {
		return "HospitalAuthorityReportBean [authTaggingId=" + authTaggingId + ", groupId=" + groupId + ", userId="
				+ userId + ", totaldischarge=" + totaldischarge + ", claimraise=" + claimraise + ", pending=" + pending
				+ "]";
	}
	
	
	

}
