/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UserDetails;
import com.project.bsky.service.UserDetailsService;

/**
 * @author arabinda.guin
 *
 */
@RestController
@RequestMapping(value = "/api")
public class UserInactiveController {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getAllUsers")
	private List<UserDetails> getAllUsers() {
		List<UserDetails> listData = null;
		try {
			listData = userDetailsService.findAll();
			return listData;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listData;
	}

	@GetMapping(value = "/getAllUsersFiltered")
	private List<UserDetails> getAllUsersFiltered(@RequestParam("userId") String userId) {
		List<UserDetails> listData = null;
		try {
			listData = userDetailsService.findAllUsers(userId);
			return listData;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listData;
	}

	@GetMapping(value = "/statusChange")
	@ResponseBody
	public ResponseEntity<Response> statusChange(@RequestParam("userId") Long userId,
			@RequestParam("status") Integer status, @RequestParam("updatedBy") Long updatedBy) {
		Response response = null;
		try {
			response = userDetailsService.statusChange(userId, status, updatedBy);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/checkStatus")
	@ResponseBody
	public ResponseEntity<Response> checkStatus(@RequestParam("userId") Long userId) {
		Response response = null;
		try {
			response = userDetailsService.checkStatus(userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

}
