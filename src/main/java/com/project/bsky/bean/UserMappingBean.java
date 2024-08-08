package com.project.bsky.bean;

import java.util.List;

public class UserMappingBean {

	private int userId;
	private int createdby;
	private List<PrimaryLinkBean> primaryLinks;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCreatedby() {
		return createdby;
	}

	public void setCreatedby(int createdby) {
		this.createdby = createdby;
	}

	public List<PrimaryLinkBean> getPrimaryLinks() {
		return primaryLinks;
	}

	public void setPrimaryLinks(List<PrimaryLinkBean> primaryLinks) {
		this.primaryLinks = primaryLinks;
	}

}
