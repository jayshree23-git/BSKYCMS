/**
 * 
 */
package com.project.bsky.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.AuthRequest;
import com.project.bsky.service.QueryLoginService;

/**
 * @author santanu.barad
 *
 */
@RestController
@RequestMapping(value = "/query/login")
public class QueryLoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private QueryLoginService service;

	@PostMapping("/authentication")
	public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
		Map<String, Object> details = new HashMap<String, Object>();
		String response = null;
		try {
			response = service.authenticate(authRequest);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception ex) {
			logger.error(ExceptionUtils.getStackTrace(ex));
			details.put("status", "fail");
			details.put("msg", ex.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@PostMapping("/queryrequest")
	public ResponseEntity<?> queryRequest(@RequestBody AuthRequest authRequest) throws Exception {
		Map<String, Object> details = new HashMap<String, Object>();
		Map<String, Object> detail = new HashMap<String, Object>();
//		JSONObject response = null;
		try {
			detail = service.queryRequest(authRequest);
			details.put("status", "success");
			details.put("data", detail);
		} catch (Exception ex) {
			logger.error(ExceptionUtils.getStackTrace(ex));
			details.put("status", "fail");
			details.put("msg", ex.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}
}
