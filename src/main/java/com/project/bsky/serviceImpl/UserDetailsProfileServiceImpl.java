package com.project.bsky.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.UserDetailsProfileBean;
import com.project.bsky.model.DistrictMaster;
import com.project.bsky.model.GroupTypeDetails;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsProfile;
import com.project.bsky.repository.DistrictMasterRepository;
import com.project.bsky.repository.GroupTypeRepository;
import com.project.bsky.repository.UserDetailsProfileRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.UserDetailsProfileService;
import com.project.bsky.util.ClassHelperUtils;

/**
 * @author ronauk
 *
 */

@Service
public class UserDetailsProfileServiceImpl implements UserDetailsProfileService {

	@Autowired
	private UserDetailsProfileRepository userDetailsProfileRepository;

	@Autowired
	private UserDetailsRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private GroupTypeRepository groupRepo;

	@Autowired
	private DistrictMasterRepository districtRepo;

	@Autowired
	private Logger logger;

	@Autowired
	private Environment env;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public UserDetailsProfile saveUserDetailSNO(UserDetailsProfileBean bean) {
		UserDetailsProfile userDetailsProfile = null;
		UserDetails userDetails = new UserDetails();
//		//System.out.println("User Details Profile :" + bean);
		try {
			userDetailsProfile = new UserDetailsProfile();
//			userDetailsProfile.setState(bean.getStateCode());
			userDetailsProfile.setEmailId(bean.getEmailId());
			userDetailsProfile.setFullName(bean.getFullName());
			DistrictMaster d = districtRepo.getdistrict(bean.getStateCode(), bean.getDistrictCode());
			userDetailsProfile.setDistrictCode(d);
			userDetailsProfile.setMobileNo(Long.parseLong(bean.getMobileNo()));
			userDetailsProfile.setUserName(bean.getUserName().toLowerCase());
			userDetailsProfile.setStatus(0);
			GroupTypeDetails gr = new GroupTypeDetails();
			gr = groupRepo.findByTypeId(Integer.parseInt(bean.getGroupId()));
			userDetailsProfile.setGroupId(gr);
			userDetailsProfile.setAddress(bean.getAddress());
			userDetailsProfile.setCreatedBy(Long.parseLong(bean.getCreatedUserName()));
			userDetailsProfile.setCreatedOn(Calendar.getInstance().getTime());
			if (Integer.parseInt(bean.getGroupId()) == 14) {
				userDetailsProfile.setDateofjoin(new SimpleDateFormat("dd-MMM-yyyy").parse(bean.getDate()));
			}
			userDetails.setCompanyCode(null);
			userDetails.setCreateDateTime(Calendar.getInstance().getTime());
			userDetails.setCreatedUserName(bean.getCreatedUserName());
			userDetails.setEmail(bean.getEmailId());
			userDetails.setFullname(bean.getFullName());
			userDetails.setGroupId(gr);
			userDetails.setStatus(0);
			String password = passwordEncoder.encode(env.getProperty("configKey"));
			userDetails.setPassword(password);
			userDetails.setPhone(Long.parseLong(bean.getMobileNo()));
			userDetails.setTPACode(null);
			userDetails.setUserName(bean.getUserName().toLowerCase());
			userDetails.setIsOtpAllowed(1);
			userDetailsProfile.setUserId(userDetails);
			userDetailsProfile = userDetailsProfileRepository.save(userDetailsProfile);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			userDetailsProfile = null;
//			throw e;
		}
		return userDetailsProfile;
	}

	@Override
	public List<UserDetailsProfile> listSNODetail() {
		List<UserDetailsProfile> listSNODetails = null;
		try {
			listSNODetails = userDetailsProfileRepository.findAllActiveSNOUser();
			return listSNODetails;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listSNODetails;
	}

	@Override
	public Integer checkSNOByUserName(String userName) {
		Integer checkSNOUsername = null;
		try {
			checkSNOUsername = userDetailsProfileRepository.getIdByUserName(userName);
			//// System.out.println(checkSNOUsername);
			return checkSNOUsername;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkSNOUsername;
	}

	@Override
	public Integer checkUserByuserName(String userName) {
		Integer checkUserDetailsuserName = null;
		try {

			checkUserDetailsuserName = userDetailsProfileRepository.checkusername(userName);
			//// System.out.println(checkUserDetailsuserName);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkUserDetailsuserName;
	}

	@Override
	public UserDetailsProfile findAllBybskyUserId(Integer bskyUserId) {
		UserDetailsProfile userDetailsProfile = null;
		try {
			userDetailsProfile = userDetailsProfileRepository.findById(bskyUserId).get();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return userDetailsProfile;
	}

	@Override
	public int updateSNOData(UserDetailsProfileBean bean) {
		Integer userId = bean.getuId();
		UserDetailsProfile userDetailsProfile = null;
		UserDetails userDetails = null;
		try {
			if (userId == null) {
				//// System.out.println("to save:");
				userDetailsProfile = new UserDetailsProfile();
				userDetails = userRepo.getByUserName(bean.getUserName()).get(0);
			} else {
				//// System.out.println("to update:");
				userDetailsProfile = userDetailsProfileRepository.findById(userId).get();
				userDetails = userDetailsProfile.getUserId();
			}
			userDetails.setStatus(Integer.parseInt(bean.getStatus()));
			userRepo.save(userDetails);
			userDetailsProfile.setStatus(Integer.parseInt(bean.getStatus()));
			//// System.out.println(userDetails.getUserName());
//			userDetailsProfile.setState(bean.getStateCode());
			userDetailsProfile.setEmailId(bean.getEmailId());
			userDetailsProfile.setFullName(bean.getFullName());
			DistrictMaster d = districtRepo.getdistrict(bean.getStateCode(), bean.getDistrictCode());
			userDetailsProfile.setDistrictCode(d);
//			userDetailsProfile.setStatus(Integer.parseInt(bean.getStatus()));
			userDetailsProfile.setMobileNo(Long.parseLong(bean.getMobileNo()));
//			userDetailsProfile.setUserName(bean.getUserName());
			GroupTypeDetails gr = new GroupTypeDetails();
			gr = groupRepo.findByTypeId(Integer.parseInt(bean.getGroupId()));
			userDetailsProfile.setGroupId(gr);
			if (Integer.parseInt(bean.getGroupId()) == 14) {
				userDetailsProfile.setDateofjoin(new SimpleDateFormat("dd-MMM-yyyy").parse(bean.getDate()));
			}
			userDetailsProfile.setAddress(bean.getAddress());
			userDetailsProfile.setUpdatedBy(Long.parseLong(bean.getCreatedUserName()));
			userDetailsProfile.setUpdatedOn(Calendar.getInstance().getTime());
			userDetails.setCompanyCode(null);
			userDetails.setEmail(bean.getEmailId());
			userDetails.setFullname(bean.getFullName());
			userDetails.setGroupId(gr);
			userDetails.setPhone(Long.parseLong(bean.getMobileNo()));
			userDetails.setTPACode(null);
//			userDetails.setUserName(bean.getUserName());
			userDetailsProfile.setUserId(userDetails);
			userDetailsProfile = userDetailsProfileRepository.save(userDetailsProfile);
			return 1;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}

	@Override
	public UserDetailsProfile getsno(Integer userId) {
		return userDetailsProfileRepository.findById(userId).get();
	}

	@Override
	public UserDetailsProfile getUserProfileFromUserId(Map<String, String> mapRequest) throws Exception {
		UserDetailsProfile userDetailsProfile = null;
		JSONObject json = new JSONObject();
		try {
			json = ClassHelperUtils.dycryptRequest(mapRequest.get("request"));
			Long userId = json.has("userId") ? json.getLong("userId") : 0L;
			userDetailsProfile = userDetailsProfileRepository.getUserProfileFromUserId(userId);
			if (userDetailsProfile == null) {
				Optional<UserDetails> optional = userRepo.findById(userId);
				if (optional.isPresent()) {
					UserDetails userDetails = null;
					userDetails = optional.get();
					userDetailsProfile = new UserDetailsProfile();
					userDetailsProfile.setUserId(userDetails);
					userDetailsProfile.setUserName(userDetails.getUserName());
					userDetailsProfile.setFullName(userDetails.getFullname());
					userDetailsProfile.setMobileNo(userDetails.getPhone());
					userDetailsProfile.setEmailId(userDetails.getEmail());
					userDetailsProfile.setGroupId(userDetails.getGroupId());
					userDetailsProfile.setDistrictCode(null);
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return userDetailsProfile;
	}

	@Override
	public List<UserDetailsProfile> findAllFilteredByDistrict(String groupId, String stateId, String districtId) {

		return userDetailsProfileRepository.findUsers(groupId, stateId, districtId);
	}
}
