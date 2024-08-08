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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.CpdRegistrationPreviewService;

@RestController
@RequestMapping(value = "/api")
public class CpdRegistrationPreviewController {

	@Autowired
	private CpdRegistrationPreviewService cpdRegistrationPreviewService;

	@Autowired
	private Logger logger;

	/*
	 * This method is used to get data to be bind in CPDRegistation Preview Page
	 * 
	 * @Param cpdUserId
	 */
	@GetMapping(value = "/getCpdRegPreviewData")
	@ResponseBody
	public Map<String, String> getPreviewData(@RequestParam(required = false, value = "cpdUserId") Integer cpdUserId) {

		String previewData = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			previewData = cpdRegistrationPreviewService.previewDetails(cpdUserId);
			details.put("status", "success");
			details.put("details", previewData);
		} catch (Exception e) {
			logger.error("Exception Occurred in getPreviewData Method of CpdRegistrationPreviewController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@ResponseBody
	@GetMapping(value = "/downloadfileCpdRegistrationPreview")
	public void commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		String prifix = json.getString("p");
		String userid = json.getString("u"); 
		try {
			if (fileName == null || fileName == "" || fileName.equalsIgnoreCase("")) {
				resp = "Document not found";
			} else {
				cpdRegistrationPreviewService.commonDownloadMethod(fileName, prifix, userid, response);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

}
