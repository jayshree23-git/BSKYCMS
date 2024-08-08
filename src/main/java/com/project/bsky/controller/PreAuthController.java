package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.project.bsky.bean.CPDPreauthActionBean;
import com.project.bsky.bean.PreAuthDetails;
import com.project.bsky.bean.PreAuthGroupBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.PreAuthService;

@Controller
@RequestMapping(value = "/api")
public class PreAuthController {

	@Autowired
	private PreAuthService preAuthService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/getPreAuthorizationList")
	public ResponseEntity<?> PreAuthorizationList(@RequestBody CPDPreauthActionBean requestBean) throws Exception {
		List<Object> preAuthorizationList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			preAuthorizationList = preAuthService.getPreAuthorizationData(requestBean);
			details.put("status", "success");
			details.put("data", preAuthorizationList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping("/updatePreAuthorizationList")
	public ResponseEntity<Response> updatePreAuthorizationList(@RequestBody PreAuthGroupBean preAuthGroupBean,
			Response response) {
		try {
			response = preAuthService.updatePreAuthorizationData(preAuthGroupBean.getUserId(),
					preAuthGroupBean.getAction(), preAuthGroupBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/downloadFileForSNA")
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
				year = dateOfAdm.substring(4, 8);
				preAuthService.downLoadPreauthFile(fileName, year, hCode, response);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}

	@ResponseBody
	@GetMapping(value = "/getPreAuthorizationDeatails")
	public ResponseEntity<?> PreAuthorizationList(@RequestParam("txnPackgId") Long txnPackgId,
			@RequestParam("urn") String urn) throws Exception {
		String preAuthorizationDetails = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			preAuthorizationDetails = preAuthService.getPreAuthDetails(txnPackgId, urn);
			details.put("status", "success");
			details.put("data", preAuthorizationDetails);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@PostMapping(value = "/updatetPreAuthorizationDeatails")
	public ResponseEntity<?> updatetPreAuthorizationDeatails(@RequestBody PreAuthDetails preAuthDetails)
			throws Exception {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = preAuthService.updatetPreAuthorizationDeatails(preAuthDetails);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@GetMapping(value = "/getPreAuthCaseDeatails")
	public ResponseEntity<?> getPreAuthCaseDetails(@RequestParam("txnPackgId") Long txnPackgId) throws Exception {
		String preAuthorizationDetails = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			preAuthorizationDetails = preAuthService.getPreAuthCaseDetails(txnPackgId);
			details.put("status", "success");
			details.put("data", preAuthorizationDetails);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}
	
	@ResponseBody
	@PostMapping(value = "/updatespecialtyrequest")
	public ResponseEntity<?> updateSpecialtyRequest(@RequestBody PreAuthDetails preAuthDetails)
			throws Exception {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = preAuthService.updateSpecialtyRequest(preAuthDetails);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

}
