package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.UserDetailsBean;
import com.project.bsky.model.UserDetails;
import com.project.bsky.service.UserDetailsProfileService;
import com.project.bsky.service.UserDetailsService;

/**
 * @author ronauk
 *
 */

@RestController
@RequestMapping(value = "/api")
public class UserDetailsController {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserDetailsProfileService userProfileService;
	
	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/saveUserData")
	public Integer addUserDetails(@RequestParam(required = false, value = "fullName") String fullName,
			@RequestParam(required = false, value = "userName") String userName,
			@RequestParam(required = false, value = "mobileNo") String mobileNo,
			@RequestParam(required = false, value = "emailId") String emailId,
			@RequestParam(required = false, value = "groupId") String groupId,
			@RequestParam(required = false, value = "createon") String createon) {
		////System.out.println(userName+" "+fullName+" "+mobileNo+" "+emailId+" "+groupId+" "+createon);
		int returnObj;
		try {
			returnObj = userDetailsService.saveUserDetails(fullName, userName, mobileNo, emailId, groupId, createon);
			////System.out.println(returnObj);
			return returnObj;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}
	
	@ResponseBody
	@PostMapping(value = "/updateUserData")
	public Integer updateUserDetailsSNO(@RequestParam(required = false, value = "fullName") String fullName,
			@RequestParam(required = false, value = "userName") String userName,
			@RequestParam(required = false, value = "mobileNo") String mobileNo,
			@RequestParam(required = false, value = "emailId") String emailId,
			@RequestParam(required = false, value = "groupId") String groupId,
			@RequestParam(required = false, value = "createon") String createon,
			@RequestParam(required = false, value = "userid") String userid) {
		////System.out.println(userName+" "+fullName+" "+mobileNo+" "+emailId+" "+groupId+" "+createon);
		int returnObj;
		try {
			returnObj = userDetailsService.updateUserDetails(fullName, userName, mobileNo, emailId, groupId, createon, userid);
			////System.out.println(returnObj);
			return returnObj;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return 0;
		}
	}
	
	@GetMapping(value = "/getbyuserid")
	public UserDetailsBean getbyuserid(@RequestParam("id") Long Id) {
		UserDetailsBean user = userDetailsService.getbyid(Id);
		////System.out.println(user.toString());
		return user;
	}
	
	@ResponseBody
	@GetMapping(value = "/checkDuplicateUser")
	public ResponseEntity<Response> checkDuplicateUser(
			@RequestParam(value = "userName", required = false) String userName, Response response) {
		////System.out.println("Inside------>>");
		////System.out.println("UserName : " + userName);
		try {
			Integer userDetailstblid = userDetailsService.checkUserDetailsByuserName(userName);
			Integer userProfile = userProfileService.checkUserByuserName(userName);
			if (userDetailstblid != 0 || userProfile != 0) {
				response.setStatus("Present");
			} else {
				response.setStatus("Absent");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/getAllUserDetails")
	public List<UserDetails> getAllUserDetails() {
		List<UserDetails> details = userDetailsService.findAll();
		////System.out.println(details);
		return details;
	}	
	
	@GetMapping(value = "/getDetailsFilteredByGroup")
	public List<UserDetails> getDetailsFilteredByGroup(@RequestParam(required = false, value = "groupId") String groupId) {
		List<UserDetails> details = userDetailsService.getDetailsFilteredByGroup(groupId);
		////System.out.println(details);
		return details;
	}	
	
	@ResponseBody
	@GetMapping(value = "/getuserlistformobilenoupdate")
	public ResponseEntity<Map<String,Object>> getuserlistformobilenoupdate(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "grp", required = false) Integer grp) {
		Map<String,Object> response=new HashMap<>();
		try {
			response=userDetailsService.getuserlistformobilenoupdate(userId,grp);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.put("status",HttpStatus.BAD_REQUEST.value());
			response.put("message","Something Went Wrong!");
			response.put("errormessage",e.getMessage());
		}
		return ResponseEntity.ok(response);
	}
	
	@ResponseBody
	@GetMapping(value = "/updatemobilenoofuser")
	public ResponseEntity<Map<String,Object>> updatemobilenoofuser(
			@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "rqst", required = false) String rqst,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "createby", required = false) Long createby) {
		Map<String,Object> response=new HashMap<>();
		try {
			response=userDetailsService.updatemobilenoofuser(userid,mobile,email,rqst,description,createby);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.put("status",HttpStatus.BAD_REQUEST.value());
			response.put("message","Something Went Wrong!");
			response.put("errormessage",e.getMessage());
		}
		return ResponseEntity.ok(response);
	}
	
	@ResponseBody
	@GetMapping(value = "/getmobilenoupdateloglist")
	public ResponseEntity<Map<String,Object>> getmobilenoupdateloglist(@RequestParam(value = "userId", required = false) Long userId) {
		Map<String,Object> response=new HashMap<>();
		try {
			response=userDetailsService.getmobilenoupdateloglist(userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.put("status",HttpStatus.BAD_REQUEST.value());
			response.put("message","Something Went Wrong!");
			response.put("errormessage",e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

}
