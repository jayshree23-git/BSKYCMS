package com.project.bsky.service;

import java.util.List;

import com.project.bsky.model.Group;

public interface GroupService {

	Group saveGroup(Group group);

	List<Group> listGroup();

	void delete(Integer groupId);

	Integer updateGroup(Group group);

	Group findAllByGroupId(Integer groupId);
	
	Integer checkGruopByName(String groupName);
	

	
	
	

}
