package com.project.bsky.service;

public interface GroupDetailsService {
	Integer getGroupDetails(String groupName, Integer isSubgrouped, String parentGroupId);

}
