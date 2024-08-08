package com.project.bsky.bean;

import java.util.List;

public class GroupMappingBean {

	private int groupId;
	private int createdby;
	private List<PrimaryLinkBean> primaryLinks;

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
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

	@Override
	public String toString() {
		return "GroupMappingBean [groupId=" + groupId + ", createdby=" + createdby + ", primaryLinks=" + primaryLinks
				+ "]";
	}

}
