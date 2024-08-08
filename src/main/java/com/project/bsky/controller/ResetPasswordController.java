package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsPassResetLog;
import com.project.bsky.service.ResetPasswordService;

@RestController
@RequestMapping(value = "/api")
public class ResetPasswordController {

	@Autowired
	private ResetPasswordService resetPasswordService;
	
	@Autowired
	private Logger logger;

	@GetMapping(value = "/getResetPasswordData")
	private Map<String,Object> getResetPasswordData(@RequestParam(value = "userid", required = false) Long userId) {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data", resetPasswordService.listData(userId));
			map.put("status", 200);
			map.put("message", "Api Called Successfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			map.put("status", 400);
			map.put("message", "Something Went Wrong");
			map.put("error", e.getMessage());
		}
		return map;

	}

	@ResponseBody
	@GetMapping(value = "/resetpassword")
	private Response resetpassword(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "createdBy") Long createdBy) {
		Response response = new Response();
		response = resetPasswordService.resetpassword(userId, createdBy);
		return response;
	}

	@ResponseBody
	@GetMapping(value = "/DetailsofUserDetailsPassResetLog")
	private Map<String,Object> getData(@RequestParam(value = "userId", required = false) Long userId) {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data", resetPasswordService.listview(userId));
			map.put("status", 200);
			map.put("message", "Api Called Successfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			map.put("status", 400);
			map.put("message", "Something Went Wrong");
			map.put("error", e.getMessage());
		}
		return map;
	}
}
