package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.FPOverrideListBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.FPOverrideCodeService;

/**
 * @author yasmin.akhtari
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class FPOverrideCodeController {

	@Autowired
	private FPOverrideCodeService fpOverrideCodeService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getOverrideCode")
	@ResponseBody
	public List<Object> getOverrideCode(@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "formDate", required = false) String formDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "aprvStatus", required = false) String aprvStatus,
			@RequestParam(value = "hospitalCode", required = false) String hospitalcode) {
		List<Object> getOverrideCode = null;
		try {
			getOverrideCode = fpOverrideCodeService.getOverrideCode(userId, formDate, toDate, action, aprvStatus,
					hospitalcode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getOverrideCode;
	}

	@ResponseBody
	@PostMapping(value = "/approveOverrideCode")
	public ResponseEntity<Response> approveOverrideCode(@RequestBody FPOverrideListBean bean, Response response) {
		try {
			response = fpOverrideCodeService.approveOverrideCode(bean.getUserId(), bean.getAction(), bean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);

	}

	@GetMapping(value = "/getPatientDetails")
	public List<Object> getPatientDetails(@RequestParam(value = "urn", required = false) String urn,
			@RequestParam(value = "memberId", required = false) Integer memberId,
			@RequestParam(value = "requestedDate", required = false) Date requestedDate,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "generatedThrough", required = false) String generatedThrough) {
		List<Object> getPatientDetails = null;
		try {
			getPatientDetails = fpOverrideCodeService.getPatientDetails(urn, memberId, requestedDate, hospitalCode,
					generatedThrough);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getPatientDetails;
	}

	@GetMapping("/downloadFileForDc")
	public String downloadFile(@RequestParam("data") String enCodedJsonString, HttpServletResponse response)
			throws JSONException {
		String resp = "";
		String year = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		String hCode = json.getString("h");
		String dateOfAdm = json.getString("d");
		try {
			if (fileName == null || fileName == "" || fileName.equalsIgnoreCase("")) {
				resp = "File not found";
			} else {
				year = dateOfAdm.substring(0, 4);
				fpOverrideCodeService.downLoadOverrideFile(fileName, year, hCode, response);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}
}
