package com.project.bsky.serviceImpl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.bsky.model.GroupTypeDetails;
import com.project.bsky.repository.GroupTypeRepository;
import com.project.bsky.service.GroupTypeService;

@Service
public class GroupTypeServiceImpl implements GroupTypeService {

	@Autowired
	private GroupTypeRepository groupTypeRepository;

	@Autowired
	private Logger logger;

	@Override
	public Integer saveDetails(GroupTypeDetails grouptype) {
		try {
			GroupTypeDetails grouptypedetails=groupTypeRepository.findByGroupTypeNameIgnoreCase(grouptype.getGroupTypeName());
			if(grouptypedetails==null) {
				Calendar calendar = Calendar.getInstance();
				grouptype.setCreatedOn((calendar.getTime()));
				grouptype.setStatus(0);
				grouptype = groupTypeRepository.save(grouptype);
				if (grouptype != null) {
					return 1;
				} else {
					return 0;
				}
			}else {
				return 2;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@Override
	public List<GroupTypeDetails> getDetails() {
		return groupTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "groupTypeName"));
	}

	@Override
	public void deleteById(Integer typeId) {
		try {
			groupTypeRepository.deleteById(typeId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public GroupTypeDetails getbyid(Integer typeId) {
		try {
			return groupTypeRepository.findById(typeId).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}

	}

	@Override
	public Integer update(GroupTypeDetails groupdetails) {		
		try {
			GroupTypeDetails grouptypedetails=groupTypeRepository.findByGroupTypeNameIgnoreCase(groupdetails.getGroupTypeName());
			if(grouptypedetails==null) {
				GroupTypeDetails grouptype = groupTypeRepository.findByTypeId(groupdetails.getTypeId());
				Calendar calendar = Calendar.getInstance();
				grouptype.setGroupTypeName(groupdetails.getGroupTypeName());
				grouptype.setStatus(groupdetails.getStatus());
				grouptype.setUpdatedBy(groupdetails.getUpdatedBy());
				grouptype.setUpdatedOn((calendar.getTime()));
				groupTypeRepository.save(grouptype);
				return 1;
			}else {
				if(groupdetails.getTypeId()==grouptypedetails.getTypeId()) {
					Calendar calendar = Calendar.getInstance();
					grouptypedetails.setGroupTypeName(groupdetails.getGroupTypeName());
					grouptypedetails.setStatus(groupdetails.getStatus());
					grouptypedetails.setUpdatedBy(groupdetails.getUpdatedBy());
					grouptypedetails.setUpdatedOn((calendar.getTime()));
					groupTypeRepository.save(grouptypedetails);
					return 1;
				}else {
					return 2;
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@Override
	public Integer checkGruopByName(String groupTypeName) {
		Integer checkGrp = null;
		try {
			checkGrp = groupTypeRepository.getGoupIdByGroupName(groupTypeName);

			return checkGrp;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkGrp;

	}

	@Override
	public Integer checkGroupTypeId(Integer typeId) {
		Integer checkGrp = null;
		try {
			checkGrp = groupTypeRepository.checkTypeId(typeId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkGrp;
	}
}
