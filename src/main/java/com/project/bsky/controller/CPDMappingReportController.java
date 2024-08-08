package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.CPDMappingReportService;

@RestController
@RequestMapping(value = "/api")
public class CPDMappingReportController {

	@Autowired
	private Logger logger;

	@Autowired
	private CPDMappingReportService cPDMappingReportService;

	@GetMapping("/getRestrictedHospitalByStateCode")
	public List<Object> getHospitalByStateCode(@RequestParam("stateCode") String stateCode) {
		List<Object> hospitalInformation = null;
		try {
			hospitalInformation = cPDMappingReportService.getHospitalByStateCode(stateCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalInformation;
	}

	@GetMapping("/getCPDListByStateCodeAndHospitalCode")
	public List<Object> Search(@RequestParam("stateCode") String stateCode,
			@RequestParam("hospitalCode") String hospitalCode) {
		List<Object> cpdhospitalInformation = null;
		try {
			cpdhospitalInformation = cPDMappingReportService.search(stateCode, hospitalCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return cpdhospitalInformation;
	}

	@GetMapping("/getsnamappingreport")
	public Map<String, Object> getsnamappingreport(
			@RequestParam(value = "stateCode", required = false) String stateCode,
			@RequestParam(value = "distCode", required = false) String distCode,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "snastatus", required = false) Integer snastatus) {
		Map<String, Object> details = new HashMap<>();
		try {
			details.put("data", cPDMappingReportService.getsnamappingreport(stateCode, distCode, hospitalCode,snastatus));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", "Error");
			details.put("error", e.getMessage());
		}
		return details;
	}
}
