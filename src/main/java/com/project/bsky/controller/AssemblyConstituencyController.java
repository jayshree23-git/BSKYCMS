/**
 * 
 */
package com.project.bsky.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.CceService;

/**
 * @Project : BSKY Backend
 * @Author : HrusiKesh Mohanty
 * @Created On : 30/04/2024 - 9:03 AM
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AssemblyConstituencyController {
	
	@Autowired
	private Logger logger;
		
	@Autowired
	private CceService cceservice;
	
	
	@GetMapping(value = "/assemblyConstituencyLgdCode")
	public ResponseEntity<?> getassemblyConstituencyLgdCode() {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			Map<String, Object>  mstApiList = cceservice.getassemblyConstituencyLgdCode();
			if (mstApiList != null && mstApiList.size() > 0) {
				response.put("status", "Success");
				response.put("statusCode", HttpStatus.OK.value());
				response.put("message", "AssemblyConstituencyLgdCode List Fetched Successfully");
				response.put("data", mstApiList);
			} else {
				response.put("status", "failure");
				response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.put("message", "No List Found!");
				response.put("data", null);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Exception Occurred in apiService of AssemblyConstituencyController" + e.getMessage());
			response.put("status", "failure");
			response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put("message", e.getMessage());
			response.put("data", null);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
