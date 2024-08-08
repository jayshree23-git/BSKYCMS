package com.project.bsky.bean;

import java.util.List;

public class Snawiserununprocessedupdate {
	private Long userId;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUnprocesseddate() {
		return unprocesseddate;
	}
	public void setUnprocesseddate(String unprocesseddate) {
		this.unprocesseddate = unprocesseddate;
	}
	public List<Long> getClaimid() {
		return claimid;
	}
	public void setClaimid(List<Long> claimid) {
		this.claimid = claimid;
	}
	private String unprocesseddate;
	private List<Long> claimid;
	private Long searchby;
	public Long getSearchby() {
		return searchby;
	}
	public void setSearchby(Long searchby) {
		this.searchby = searchby;
	}
	@Override
	public String toString() {
		return "Snawiserununprocessedupdate [userId=" + userId + ", unprocesseddate=" + unprocesseddate + ", claimid="
				+ claimid + ", searchby=" + searchby + "]";
	}
	
}
