
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.MobileUserAuthClaimHistoryBean;
import com.project.bsky.model.MobileAPiRejectionReasonsResponseBean;
import com.project.bsky.model.MobileServiceGetClaimList;
import com.project.bsky.model.MobileUserAuthClaimHistory;
import com.project.bsky.model.MobileUserChangePassword;
import com.project.bsky.model.MobileUserForgotPassword;
import com.project.bsky.model.MobileUserSubmitRemarks;
import com.project.bsky.service.ClaimsMobileAPIService;

@RestController
@RequestMapping(value = "/mobileApi")
public class ClaimsMobileServiceAPIController {

	@Autowired
	private ClaimsMobileAPIService claimsMobileAPIService;
	
	@Autowired
	private Logger logger;

	@RequestMapping(value = "/getToken", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getToken() {
		String token = "";
		String apikey = "npnWXFtZEtNYghytUVLQVE4TEw5MVdXeEtta3cyMFRSeEprbDY1WG9kMWQwcz0=";
		Map<String, Object> data = new HashMap<>();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("apikey", apikey);
		try {
			token = claimsMobileAPIService.getToken(apikey);
			if (!token.isEmpty()) {
				data.put("token", token);
				data.put("status", (HttpStatus.OK).value());
				data.put("message", "Token generated successfully");
			} else {
				data.put("status", "NO-DATA-FOUND");
				data.put("msg", "No Data Found");
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			data.put("status", (HttpStatus.BAD_REQUEST).value());
			data.put("msg", e.getMessage());
		}

		return ResponseEntity.ok().headers(responseHeaders).body(data);
	}

	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> forgotPasswordgetOtp(@RequestBody MobileUserForgotPassword mobileUserForgotPassword) {
		String otp = "";
		Map<String, Object> data = new HashMap<>();
		try {
			otp = claimsMobileAPIService.forgotPasswordgetOtp(mobileUserForgotPassword);
			if (!otp.isEmpty()) {
				data.put("otp", otp);
				data.put("status", (HttpStatus.OK).value());
			} else {
				data.put("status", "NO-DATA-FOUND");
				data.put("msg", "No Data Found");
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			data.put("status", (HttpStatus.BAD_REQUEST).value());
			data.put("msg", e.getMessage());
		}

		return ResponseEntity.ok().body(data);
	}

//	@RequestMapping(value = "/requestMobileUserLogin", method = RequestMethod.POST)
//	public ResponseEntity<Map<String, Object>> requestLogin(@RequestBody MobileAuthRequest mobileAuthRequest) {
//		Map<String, Object> details = new HashMap<String, Object>();
//		MobileAuthResponse response = null;
//		try {
//			response = claimsMobileAPIService.requestLogin(mobileAuthRequest);
//			details.put("status", (HttpStatus.OK).value());
//			details.put("msg", "Login successful");
//			details.put("data", response);
//
//		} catch (Exception ex) {
//
//			details.put("status", (HttpStatus.BAD_REQUEST).value());
//			details.put("msg", ex.getMessage());
//		}
//		return ResponseEntity.status(HttpStatus.OK).body(details);
//	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> changePassword(
			@RequestBody MobileUserChangePassword mobileUserChangePassword) {
		Map<String, Object> details = new HashMap<String, Object>();
		String response = "";
		try {
			response = claimsMobileAPIService.changePassword(mobileUserChangePassword);
			details.put("status", (HttpStatus.OK).value());
			details.put("msg", response);

		} catch (Exception ex) {
			logger.error(ExceptionUtils.getStackTrace(ex));
			details.put("status", (HttpStatus.BAD_REQUEST).value());
			details.put("msg", ex.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(details);
	}
	@RequestMapping(value = "/getClaimsList", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getClaimsList(
			@RequestBody MobileServiceGetClaimList mobileServiceGetClaimList) {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> claimsList = new ArrayList<Map<String, Object>>();
		try {
			claimsList = claimsMobileAPIService.getClaimsList(mobileServiceGetClaimList);
			if (claimsList != null && !claimsList.isEmpty()) {
				data.put("status",  (HttpStatus.OK).value());
				data.put("msg", "Claims are fetched successfully");
				data.put("data", claimsList);
				
			} else {
				data.put("status", (HttpStatus.BAD_REQUEST).value());
				data.put("msg", "No Data Found");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			data.put("status", "fail");
			data.put("msg", e.getMessage());
		}
		return ResponseEntity.ok().body(data);
	}
	@RequestMapping(value = "/submitRemarks", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> submitRemarks(
			@RequestBody MobileUserSubmitRemarks mobileUserSubmitRemarks) {
		Map<String, Object> details = new HashMap<String, Object>();
		String response = "";
		try {
			response = claimsMobileAPIService.changePassword(mobileUserSubmitRemarks);
			details.put("status", (HttpStatus.OK).value());
			details.put("msg", response);

		} catch (Exception ex) {
			logger.error(ExceptionUtils.getStackTrace(ex));
			details.put("status", (HttpStatus.BAD_REQUEST).value());
			details.put("msg", ex.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(details);
	}
	@RequestMapping(value = "/getAuthClaimHistory", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getAuthClaimHistory(
			@RequestBody MobileUserAuthClaimHistory mobileUserAuthClaimHistory) {
		Map<String, Object> details = new HashMap<String, Object>();
		List<MobileUserAuthClaimHistoryBean> detailsBean = new ArrayList<>();
		try {
			detailsBean = claimsMobileAPIService.getAuthClaimHistory(mobileUserAuthClaimHistory);
			details.put("status", (HttpStatus.OK).value());
			details.put("msg", "Pre authentication log fetched successfully.");
			details.put("data:", detailsBean);

		} catch (Exception ex) {
			logger.error(ExceptionUtils.getStackTrace(ex));
			details.put("status", (HttpStatus.BAD_REQUEST).value());
			details.put("msg", ex.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(details);
	}
	@RequestMapping(value = "/getRejectionReasons", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>>  getRejectionReasons() {
		Map<String, Object> details = new HashMap<String, Object>();
		List<MobileAPiRejectionReasonsResponseBean> detailsBean = new ArrayList<>();
		try {
			detailsBean = claimsMobileAPIService.getRejectionReasons();
			details.put("status", (HttpStatus.OK).value());
			details.put("msg", "successfull");
			details.put("data", detailsBean);

		} catch (Exception ex) {
			logger.error(ExceptionUtils.getStackTrace(ex));
			details.put("status", (HttpStatus.BAD_REQUEST).value());
			details.put("msg", ex.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(details);
	}
}
