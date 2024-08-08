package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.IntGrvUserBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.UserDetailsProfileBean;
import com.project.bsky.model.OtpConfigLog;
import com.project.bsky.model.UserDetailsProfile;
import com.project.bsky.service.AdminConsoleService;
import com.project.bsky.service.UserDetailsForSaveCPdService;
import com.project.bsky.service.UserDetailsProfileService;
import com.project.bsky.service.UserDetailsService;
import com.project.bsky.service.UserProfileLogSaveService;
import com.project.bsky.util.ClassHelperUtils;

/**
 * @author ronauk
 *
 */
@RestController
@RequestMapping(value = "/api")
public class UserDetailsProfileController {

	@Autowired
	private UserDetailsProfileService userDetailsProfileService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserDetailsForSaveCPdService userDetailsforSaveCpdService;

	@Autowired
	private UserProfileLogSaveService logService;

	@Autowired
	private AdminConsoleService adminService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/saveUserProfileData")
	public Integer addUserDetailsProfile(@RequestBody UserDetailsProfileBean bean) {
		Integer returnObj = null;
		try {
			UserDetailsProfile u = userDetailsProfileService.saveUserDetailSNO(bean);
			if (u != null) {
				int userId = u.getUserId().getUserId().intValue();
				int groupId = u.getGroupId().getTypeId();
				adminService.copyPrimaryLinksForUser(userId, Integer.parseInt(bean.getCreatedUserName()), groupId);
				returnObj = 1;
			} else {
				returnObj = 0;
			}
		} catch (Exception e) {
			returnObj = 0;
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

	@PostMapping(value = "/saveProfilelog")
	public ResponseEntity<Map<String, Object>> saveUserProfileLogData(@RequestBody Map<String, String> mapRequest) {
		JSONObject json = new JSONObject();
		try {
			json = ClassHelperUtils.dycryptRequest(mapRequest.get("request"));
			Long userId = json.has("userId") ? json.getLong("userId") : 0L;
			Long createdBy = json.has("createdBy") ? json.getInt("createdBy") : 0L;
			return ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap(logService.saveProfileLog(userId, createdBy),
					"Profile Log Saved Successfully"));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return ResponseEntity.ok(ClassHelperUtils.createErrorEncryptedMap(e.getMessage()));
		}
	}


	@GetMapping(value = "/getUserProfileDetails")
	private List<UserDetailsProfile> getUserProfileDetails(
			@RequestParam(value = "groupId", required = false) String groupId,
			@RequestParam(value = "stateId", required = false) String stateId,
			@RequestParam(value = "districtId", required = false) String districtId) {
		List<UserDetailsProfile> listUserDetail = null;
		try {
			if (groupId.equalsIgnoreCase("null")) {
				groupId = null;
			}
			if (stateId.equalsIgnoreCase("null")) {
				stateId = null;
			}
			if (districtId.equalsIgnoreCase("null")) {
				districtId = null;
			}
			listUserDetail = userDetailsProfileService.findAllFilteredByDistrict(groupId, stateId, districtId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listUserDetail;
	}

	@ResponseBody
	@GetMapping("/forupdateByIdgetData")
	public UserDetailsProfile getData(@RequestParam(value = "bskyUserId", required = false) Integer bskyUserId) {
		UserDetailsProfile returnOb = null;
		try {
			returnOb = userDetailsProfileService.findAllBybskyUserId(bskyUserId);
			return returnOb;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnOb;
	}

	@PostMapping("/getUserProfile")
	public ResponseEntity<Map<String, Object>> getUserProfile(@RequestBody Map<String, String> mapRequest) {
		try {
			return ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap(
					new JSONObject(userDetailsProfileService.getUserProfileFromUserId(mapRequest)),
					"User Details Retrived Successfully"));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return ResponseEntity.ok(ClassHelperUtils.createErrorEncryptedMap(e.getMessage()));
		}
	}

	@ResponseBody
	@GetMapping(value = "/checkDuplicateuserName")
	public ResponseEntity<Response> checkDuplicateDataSNO(
			@RequestParam(value = "userName", required = false) String userName, Response response) {
		try {
			Integer IdInteger = userDetailsforSaveCpdService.checkusername(userName);
			Integer userDetailstblid = userDetailsService.checkUserDetailsByuserName(userName);
			if ((IdInteger != 0) || (userDetailstblid != 0)) {
				response.setStatus("Present");
			} else {
				response.setStatus("Absent");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@GetMapping(value = "/checkDuplicateLicense")
	public ResponseEntity<Response> checkDuplicateDoctor(
			@RequestParam(value = "license", required = false) String license,
			@RequestParam(value = "username", required = false) String username, Response response) {
		try {
			if (username.equalsIgnoreCase("")) {
				username = "0";
			}
			Integer checklicense = userDetailsforSaveCpdService.checklicense(license, username);
			if (checklicense != 0)
				response.setStatus("Present");
			else
				response.setStatus("Absent");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/updateUserProfileData")
	public Integer updateSNOuser(@RequestBody UserDetailsProfileBean bean) {
		Integer returnObject = 0;
		try {
			returnObject = userDetailsProfileService.updateSNOData(bean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			returnObject = 0;
		}
		return returnObject;

	}

	@ResponseBody
	@PostMapping("/updateUserLoginOtp")
	public Map<String, Object> updateUserLoginOtp(@RequestBody Map<String, Object> userBeans) {

		Integer returnObject = 0;
		Map<String, Object> detailMap = new HashMap<>();
		try {
			returnObject = userDetailsService.updateUserLoginOtp(userBeans);
			detailMap.put("status", "success");
			detailMap.put("data", returnObject);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			detailMap.put("status", "fail");
			detailMap.put("data", e.getMessage());
		}
		return detailMap;
	}

	@ResponseBody
	@PostMapping("/userLoginOtpLog")
	public Map<String, Object> userLoginOtpLog(@RequestBody IntGrvUserBean configLog) {

		List<OtpConfigLog> returnObject = null;
		Map<String, Object> detailMap = new HashMap<>();
		try {
			returnObject = userDetailsService.userLoginOtpLog(configLog);
			detailMap.put("status", "success");
			detailMap.put("data", returnObject);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			detailMap.put("status", "fail");
			detailMap.put("data", e.getMessage());
		}
		return detailMap;
	}
}
