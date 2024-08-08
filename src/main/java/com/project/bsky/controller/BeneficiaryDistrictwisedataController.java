/**
 * 
 */
package com.project.bsky.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.BeneficiaryDistrictwisedataService;

/**
 * @author rajendra.sahoo Dt-20/11/2023
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
@RequestMapping(value = "/api")
public class BeneficiaryDistrictwisedataController {

	@Autowired
	private Logger logger;

	@Autowired
	private BeneficiaryDistrictwisedataService districtwisedataservices;

	@GetMapping(value = "/getbenificiaryblockwisetreatmentdata")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getbenificiaryblockwisedata(
			@RequestParam(value = "age", required = false) Long age,
			@RequestParam(value = "ageconditions", required = false) String ageconditions,
			@RequestParam(value = "distcode", required = false) String distcode,
			@RequestParam(value = "userid", required = false) Long userid) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data",
					districtwisedataservices.getbenificiaryblockwisedata(age, ageconditions, distcode, userid));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@GetMapping(value = "/getbenificiarygpwisetreatmentdata")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getbenificiarygpwisedata(
			@RequestParam(value = "age", required = false) Long age,
			@RequestParam(value = "ageconditions", required = false) String ageconditions,
			@RequestParam(value = "distcode", required = false) String distcode,
			@RequestParam(value = "blockcode", required = false) String blockcode,
			@RequestParam(value = "userid", required = false) Long userid) {

		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data",
					districtwisedataservices.getbenificiarygpwisedata(age, ageconditions, distcode, blockcode, userid));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@GetMapping(value = "/getbenificiaryvillagewisetreatmentdata")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getbenificiaryvillagewisedata(
			@RequestParam(value = "age", required = false) Long age,
			@RequestParam(value = "ageconditions", required = false) String ageconditions,
			@RequestParam(value = "distcode", required = false) String distcode,
			@RequestParam(value = "blockcode", required = false) String blockcode,
			@RequestParam(value = "gpcode", required = false) String gpcode,
			@RequestParam(value = "userid", required = false) Long userid) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data", districtwisedataservices.getbenificiaryvillagewisedata(age, ageconditions, distcode,
					blockcode, gpcode, userid));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	/*
	 * GET STATE DASBOARD DATA REPORT RAJENDRA PRASAD SAHOO 23-FEB-2024
	 */
	@GetMapping(value = "/getstatedashboarddata")
	@ResponseBody
	public Map<String, Object> getstatedashboarddata(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "todate", required = false) Date todate) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data", districtwisedataservices.getstatedashboarddata(formdate, todate));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("error", e.getMessage());
			details.put("message", e.getMessage());
		}
		return details;
	}

}
