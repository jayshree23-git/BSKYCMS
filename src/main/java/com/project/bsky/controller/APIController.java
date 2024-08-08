package com.project.bsky.controller;

import com.project.bsky.model.MstApi;
import com.project.bsky.service.APIService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : BSKY Backend
 * @Author : Sambit Kumar Pradhan
 * @Created On : 13/06/2023 - 1:03 PM
 */

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class APIController {

	@Autowired
	private Logger logger;

	@Autowired
	private APIService apiService;

	@GetMapping(value = "/getMasterAPIServices")
	public ResponseEntity<?> getMasterAPIServices() {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			List<MstApi> mstApiList = apiService.getMasterAPIServices();
			if (mstApiList != null && mstApiList.size() > 0) {
				response.put("status", "Success");
				response.put("statusCode", HttpStatus.OK.value());
				response.put("message", "Master API List Fetched Successfully");
				response.put("data", mstApiList);
			} else {
				response.put("status", "failure");
				response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.put("message", "No List Found!");
				response.put("data", null);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Exception Occurred in getMasterAPIServices of APIServiceController" + e.getMessage());
			response.put("status", "failure");
			response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put("message", e.getMessage());
			response.put("data", null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/getReportDataList")
	public ResponseEntity<?> getReportDataList(@RequestBody String request) {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			List<Map<String, Object>> reportDataList = apiService.getReportDataList(request);
			if (reportDataList != null && reportDataList.size() > 0) {
				response.put("status", "Success");
				response.put("statusCode", HttpStatus.OK.value());
				response.put("message", "Report Data List Fetched Successfully");
				response.put("data", reportDataList);
			} else {
				response.put("status", "failure");
				response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.put("message", "No Data Found!");
				response.put("data", null);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Exception Occurred in getReportDataList of APIServiceController" + e.getMessage());
			response.put("status", "failure");
			response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put("message", e.getMessage());
			response.put("data", null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/getReportDetails")
	public ResponseEntity<?> getReportDetails(@RequestBody String request) {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			Map<String, Object> reportDetails = apiService.getReportDetails(request);
			if (reportDetails != null && reportDetails.size() > 0) {
				response.put("status", "Success");
				response.put("statusCode", HttpStatus.OK.value());
				response.put("message", "Report Details Fetched Successfully");
				response.put("data", reportDetails);
			} else {
				response.put("status", "failure");
				response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.put("message", "No Data Found!");
				response.put("data", null);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Exception Occurred in getReportDetails of APIServiceController" + e.getMessage());
			response.put("status", "failure");
			response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put("message", e.getMessage());
			response.put("data", null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/getOldDataDetails")
	public ResponseEntity<?> getOldDataDetails(@RequestBody String request) {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			Map<String, Object> oldDataDetails = apiService.getOldDataDetails(request);
			if (oldDataDetails != null && oldDataDetails.size() > 0) {
				response.put("status", "Success");
				response.put("statusCode", HttpStatus.OK.value());
				response.put("message", "Old Data Details Fetched Successfully");
				response.put("data", oldDataDetails);
			} else {
				response.put("status", "failure");
				response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.put("message", "No Data Found!");
				response.put("data", null);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Exception Occurred in getOldDataDetails of APIServiceController" + e.getMessage());
			response.put("status", "failure");
			response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put("message", e.getMessage());
			response.put("data", null);
		}
		// System.out.println("Response : " + response);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
