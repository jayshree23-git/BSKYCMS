package com.project.bsky.service;

import java.util.List;

import com.project.bsky.model.GroupTypeDetails;

public interface GroupTypeService {

	Integer saveDetails(GroupTypeDetails grouptype);

	List<GroupTypeDetails> getDetails();

	void deleteById(Integer typeId);

	GroupTypeDetails getbyid(Integer typeId);

	Integer update(GroupTypeDetails groupdetails);

	Integer checkGruopByName(String groupTypeName);

	Integer checkGroupTypeId(Integer typeId);

}
