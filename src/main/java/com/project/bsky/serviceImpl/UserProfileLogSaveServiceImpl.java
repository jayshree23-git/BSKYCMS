package com.project.bsky.serviceImpl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UserDetailsProfile;
import com.project.bsky.model.UserDetailsProfileLog;
import com.project.bsky.repository.UserDetailsProfileLogRepository;
import com.project.bsky.repository.UserDetailsProfileRepository;
import com.project.bsky.service.UserProfileLogSaveService;
import com.project.bsky.util.ClassHelperUtils;

/**
 * @author ronauk
 *
 */
@Service
public class UserProfileLogSaveServiceImpl implements UserProfileLogSaveService {
	
	@Autowired
	private UserDetailsProfileLogRepository userRepo;
	
	@Autowired
	private UserDetailsProfileRepository profileRepo;
	
	@Autowired
	private Logger logger;

	@Override
	public Map<String, Object> saveProfileLog(Long userId, Long createdBy) throws Exception {
		Map<String, Object> map = new HashMap<>();
		UserDetailsProfileLog userDetailsProfile = new UserDetailsProfileLog();
		try {
			UserDetailsProfile user = profileRepo.getUserProfileFromUserId(userId);
			userDetailsProfile.setBskyUserId(user.getBskyUserId());
			userDetailsProfile.setStateCode(user.getDistrictCode().getStatecode().getStateCode());
			userDetailsProfile.setEmailId(user.getEmailId());
			userDetailsProfile.setFullName(user.getFullName());
			userDetailsProfile.setDistrictCode(user.getDistrictCode().getDistrictcode());
			userDetailsProfile.setMobileNo(user.getMobileNo());
			userDetailsProfile.setUserName(user.getUserName().toLowerCase());
			userDetailsProfile.setStatus(user.getStatus());
			userDetailsProfile.setGroupId(user.getGroupId().getTypeId());
			userDetailsProfile.setAddress(user.getAddress());
			userDetailsProfile.setCreatedOn(Calendar.getInstance().getTime());
			userDetailsProfile.setCreatedBy(createdBy.intValue());
			userDetailsProfile.setUserId(user.getUserId().getUserId());
			userDetailsProfile = userRepo.save(userDetailsProfile);
			
			map.put("message", "Log Created Successfully");
			map.put("status", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return map;
	}

}
