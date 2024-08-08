/**
 * 
 */
package com.project.bsky.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.AuthResponse;
import com.project.bsky.bean.LoginBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.service.LoginService;
import com.project.bsky.util.ClassHelperUtils;

/**
 * @author santanu.barad
 *
 */
@RestController
@RequestMapping(value = "/api/login")
public class LoginController {

	@Autowired
	private Logger logger;

	@Autowired
	private LoginService service;

	@Value("${configKey}")
	private String password;

	@PostMapping("/authenticate")
	public ResponseEntity<Map<String, Object>> generateToken(HttpServletRequest httpServletRequest,@RequestBody Map<String, String> mapRequest)
			throws Exception {
		AuthResponse response = null;
		String checkPassword = null;
		try {
			response = service.authenticate(httpServletRequest,mapRequest);
			checkPassword = response.getCheckPassword();
			if (checkPassword != null) {
				return ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap("Change password", "change"));
			} else {
				JSONObject jsonObj = new JSONObject(response);
				return ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap(jsonObj, "success"));
			}
		} catch (Exception ex) {
			logger.error("Exception Occurred in Generate Token Method of LoginController" + ex.getMessage());
			return ResponseEntity.ok(ClassHelperUtils.createErrorEncryptedMap(ex.getMessage()));
		}
	}

	@PostMapping("/changePassword")
	public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> mapRequest) throws Exception {
		try {
			return ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap(service.changePassword(mapRequest), "Password has been changed successfully"));
		} catch (Exception ex) {
			logger.error("Exception Occurred in Change Password Method of LoginController" + ex.getMessage());
			return ResponseEntity.ok(ClassHelperUtils.createErrorEncryptedMap(ex.getMessage()));
		}
	}

	@PostMapping(value = "/getUserMappingList")
	public ResponseEntity<Map<String, Object>> getUserMappingList(@RequestBody Map<String, String> mapRequest) {
		try {
			return ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap(service.getUserMappingList(mapRequest),
					"Data Retrived Successfully"));
		} catch (Exception ex) {
			logger.error("Exception Occurred in getUserMappingList Method of LoginController" + ex.getMessage());
			return ResponseEntity.ok(ClassHelperUtils.createErrorEncryptedMap(ex.getMessage()));
		}
	}

	@PostMapping("/forgotPassword")
	public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody Map<String, String> mapRequest) throws Exception {
		AuthRequest response = null;
		try {
			response = service.forgotPassword(mapRequest);
			return ResponseEntity.ok(ClassHelperUtils.createSuccessEncryptedMap(new JSONObject(response),
					"Password Has Been Changed Successfully"));
		} catch (Exception ex) {
			logger.error("Exception Occurred in Forgot Password Method of LoginController" + ex.getMessage());
			return ResponseEntity.ok(ClassHelperUtils.createErrorEncryptedMap(ex.getMessage()));
		}
	}

	@PostMapping("/otpValidate")
	public ResponseEntity<?> otpValidate(@RequestBody AuthRequest authRequest) throws Exception {
		Map<String, Object> details = new HashMap<>();
		Response response = null;
		try {
			response = service.otpValidate(authRequest);
			details.put("status", response.getMessage());
			details.put("data", response);
		} catch (Exception ex) {
			logger.error("Exception Occurred in OTP Validate Method of LoginController" + ex.getMessage());
			details.put("status", "fail");
			details.put("msg", ex.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@PostMapping("/internalLogin")
	public ResponseEntity<?> internalLogin(@RequestBody AuthRequest authRequest) throws Exception {
		Map<String, Object> details = new HashMap<>();
		AuthRequest response = null;
		try {
			response = service.internalLogin(authRequest);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception ex) {
			logger.error("Exception Occurred in Internal Login Method of LoginController" + ex.getMessage());
			details.put("status", "fail");
			details.put("msg", ex.getMessage());
		}
		return new ResponseEntity<>(details, HttpStatus.OK);
	}

	@PostMapping("/changeuserpassword")
	public ResponseEntity<Map<String, Object>> changeuserpassword(@RequestBody Map<String, String> mapRequest)
			throws Exception {
		try {
			return ResponseEntity.ok(ClassHelperUtils
					.createSuccessEncryptedMap(new JSONObject(service.changeuserpassword(mapRequest)), ""));
		} catch (Exception ex) {
			logger.error("Exception Occurred in Change User Password Method of LoginController" + ex.getMessage());
			return ResponseEntity.ok(ClassHelperUtils.createErrorEncryptedMap(ex.getMessage()));
		}
	}

	@PostMapping("/otpDuringInternalLogin")
	public ResponseEntity<?> otpDuringInternalLogin(@RequestBody AuthRequest authRequest) throws Exception {
		Map<String, Object> details = new HashMap<>();
		AuthRequest response = null;
		try {
			response = service.requestOtp(authRequest);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception ex) {
			logger.error("Exception Occurred in OTP During Internal Login Method of LoginController" + ex.getMessage());
			details.put("status", "fail");
			details.put("msg", ex.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@PostMapping("/GetSSOLoginInformation")
	public ResponseEntity<?> getSSOLoginInformation(@RequestBody LoginBean loginBean) {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			response.put("status", HttpStatus.OK.value());
			response.put("data", service.getSSOLoginInformation(loginBean));
		} catch (Exception e) {
			logger.error("Exception Occurred in getSSOLoginInformation Method of LoginController" + e.getMessage());
			response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put("msg", e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/getProfilePhoto")
	public ResponseEntity<?> getProfilePhoto(@RequestBody Map<String, String> mapRequest) {
		JSONObject json = new JSONObject();
		try {
			json = ClassHelperUtils.dycryptRequest(mapRequest.get("request"));
			String profilePath = service.getProfilePath(json.has("userId") ? json.getLong("userId") : 0L);
			if (profilePath != null) {
				Path path = Paths.get(profilePath);
				ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
				return ResponseEntity.ok().contentLength(path.toFile().length()).contentType(MediaType.IMAGE_JPEG)
						.body(resource);
			}
		} catch (IOException e) {
			logger.error("Exception Occurred in getProfilePhoto Method of LoginController" + e.getMessage());
		} catch (Exception e) {
			logger.error("Exception Occurred in getProfilePhoto Method of LoginController" + e.getMessage());
		}
		return null;
	}

	@GetMapping("/mobileApiloginforweb")
	public ResponseEntity<?> mobileApilogin(@RequestParam("username") String username) {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			AuthResponse response1 = service.mobileApilogin(username);
			response.put("status", 200);
			response.put("data", response1);
			response.put("msg", "Sucessful");
		} catch (Exception e) {
			logger.error("Exception Occurred in Generate Token Method of LoginController" + e.getMessage());
			response.put("status", 400);
			response.put("msg", e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
