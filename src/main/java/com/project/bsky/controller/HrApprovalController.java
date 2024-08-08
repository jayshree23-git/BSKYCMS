/**
 * 
 */
package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
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

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.HrApprovalService;

/**
 * @author santanu.barad
 *
 */

@RestController
@RequestMapping(value = "/api")
public class HrApprovalController {
	@Autowired
	private Logger logger;

	@Autowired
	private HrApprovalService approvalService;

	@ResponseBody
	@PostMapping(value = "/getfreshapplication")
	public ResponseEntity<?> getFreshApplication(@RequestBody CPDApproveRequestBean requestData) throws Exception {
		String freshApplication = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			freshApplication = approvalService.getFreshApplication(requestData);
			details.put("status", "success");
			details.put("data", freshApplication);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@GetMapping(value = "/getfreshapplicationdetails")
	public ResponseEntity<?> getFreshApplicationDetails(
			@RequestParam(value = "userId", required = false) Long cpdUserId) throws Exception {
		String freshApplication = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			freshApplication = approvalService.getFreshApplicationDetails(cpdUserId);
			details.put("status", "success");
			details.put("data", freshApplication);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@PostMapping(value = "/scheduleapplication")
	public ResponseEntity<?> scheduleApplication(@RequestBody Map<String, Object> requestData) throws Exception {
		Response freshApplication = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			freshApplication = approvalService.scheduleApplication(requestData);
			details.put("status", "success");
			details.put("data", freshApplication);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@PostMapping(value = "/getviewapplication")
	public ResponseEntity<?> getViewApplication(@RequestBody CPDApproveRequestBean requestData) throws Exception {
		String freshApplication = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			freshApplication = approvalService.getViewApplication(requestData);
			details.put("status", "success");
			details.put("data", freshApplication);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@GetMapping(value = "/downloadfileCpdRegistration")
	public void commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		String prifix = json.getString("p");
		String userid = json.getString("u");
		System.out.println("name: " + fileName);
		try {
			if (fileName == null || fileName == "" || fileName.equalsIgnoreCase("")) {
				resp = "Document not found";
			} else {
				approvalService.commonDownloadMethod(fileName, prifix, userid, response);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@ResponseBody
	@GetMapping(value = "/getapprovedapplicationdetails")
	public ResponseEntity<?> getApprovedApplicationDetails(
			@RequestParam(value = "userId", required = false) Long cpdUserId) throws Exception {
		String freshApplication = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			freshApplication = approvalService.getApprovedApplicationDetails(cpdUserId);
			details.put("status", "success");
			details.put("data", freshApplication);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}
	
	@ResponseBody
	@PostMapping(value = "/finalapproveapplication")
	public ResponseEntity<?> finalApproveApplication(@RequestBody Map<String, Object> requestData) throws Exception {
		Response freshApplication = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			freshApplication = approvalService.finalApproveApplication(requestData);
			details.put("status", "success");
			details.put("data", freshApplication);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

}
