package com.project.bsky.serviceImpl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.model.Group;
import com.project.bsky.repository.GroupRepository;
import com.project.bsky.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private Logger logger;

	@Override
	public Group saveGroup(Group group) {

		try {
			Calendar calendar = Calendar.getInstance();
			group.setCreatedDate((calendar.getTime()));
			group.setIsActive(0);
			group.setLastupdateBy(null);
			group.setLastupdateDate((calendar.getTime()));

			group = groupRepository.save(group);
			return group;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return group;

	}

	@Override
	public List<Group> listGroup() {

		List<Group> listGrp = null;
		try {
			listGrp = groupRepository.findAllActive();
			return listGrp;

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return listGrp;
	}

	@Override
	public void delete(Integer groupId) {

		try {
			groupRepository.delete(groupId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

	}

	@Override
	public Integer updateGroup(Group group) {
		Integer checkGrp = groupRepository.getGoupIdByGroupName(group.getGroupName());
		Group group1 = groupRepository.findBygroupName(group.getGroupName());
		try {
			if (checkGrp == null) {
				group = groupRepository.save(group);
				return 1;
			} else if (group1.getGroupId().equals(group.getGroupId())
					&& group1.getGroupName().equals(group.getGroupName())) {
				group = groupRepository.save(group);
				return 1;
			} else {
				return 2;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}

	}

	@Override
	public Group findAllByGroupId(Integer groupId) {

		Group group = null;
		try {
			group = groupRepository.findById(groupId).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return group;

	}

	@Override
	public Integer checkGruopByName(String groupName) {

		Integer checkGrp = null;
		try {
			checkGrp = groupRepository.getGoupIdByGroupName(groupName);
			return checkGrp;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkGrp;

	}

}
