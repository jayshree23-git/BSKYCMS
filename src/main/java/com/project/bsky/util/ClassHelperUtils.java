package com.project.bsky.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * @Project : BSKY Card Backend
 * @Author : Sambit Kumar Pradhan
 * @Created On : 28/07/2023 - 12:24 PM
 */
public class ClassHelperUtils {

	@Autowired
	private static Logger logger;

	public static Map<String, Object> createSuccessResponse(Object data, String message) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "success");
		response.put("statusCode", HttpStatus.OK.value());
		response.put("message", message);
		response.put("data", data);
		return response;
	}

	public static Map<String, Object> createNoContentResponse(String message) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "failure");
		response.put("statusCode", HttpStatus.NO_CONTENT.value());
		response.put("message", message);
		return response;
	}

	public static Map<String, Object> createErrorResponse(String message) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "failure");
		response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.put("message", message);
		return response;
	}

	public static Map<String, Object> createSuccessEncryptedMap(Object data, String message) {
		Map<String, Object> response = new LinkedHashMap<>();

		response.put("encData", EncryptionUtils
				.encrypt(new JSONObject(ClassHelperUtils.createSuccessResponse(data, message)).toString()));
		return response;
	}

	public static Map<String, Object> createNoContentEncryptedMap(String message) {
		Map<String, Object> response = new LinkedHashMap<>();

		response.put("encData",
				EncryptionUtils.encrypt(new JSONObject(ClassHelperUtils.createNoContentResponse(message)).toString()));
		return response;
	}

	public static Map<String, Object> createErrorEncryptedMap(String message) {
		Map<String, Object> response = new LinkedHashMap<>();

		response.put("encData",
				EncryptionUtils.encrypt(new JSONObject(ClassHelperUtils.createErrorResponse(message)).toString()));
		return response;
	}

	public static JSONObject dycryptRequest(String request) throws Exception {
		JSONObject json = null;
		try {
			json = new JSONObject(EncryptionUtils.decryptCode(request));
		} catch (Exception e) {
			logger.error("Exception occurred in dycryptRequest() method of ClassHelperUtils class", e);
			throw new Exception(e.getMessage());
		}
		return json;
	}

	public static Map<String, Object> createSuccessMessageResponse(String message) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", "success");
		response.put("statusCode", HttpStatus.OK.value());
		response.put("message", message);
		return response;
	}

}
