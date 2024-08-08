/**
 * 
 */
package com.project.bsky.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.OneDcTaggedReportService;

/**
 * @author priyanka.singh
 *
 */
@RestController
@RequestMapping(value = "/api")
public class OneDcTaggedReportController {
	
	@Autowired
	private OneDcTaggedReportService oneDcTaggedReportService;
	
	@Autowired
	private Logger logger;
	
	@GetMapping("/dctaggedReportList")
	public String getHospitalByDistrictId(
			@RequestParam("userId") Integer userId,
			@RequestParam("stateId1") String stateId1,
			@RequestParam("districtId1") String districtId1,
			@RequestParam(name = "dcId", required = false) Integer dcId ) {
		String getclaimList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			getclaimList = oneDcTaggedReportService.getHospitalByDistrictId(userId,stateId1,districtId1,dcId);
			details.put("status", "success");
			details.put("details", getclaimList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return getclaimList;
	}
	
	@GetMapping("/getDcDetailsList")
	public String getDcDetailsList(
			@RequestParam("userId") Integer userId,
			@RequestParam("assignDc") Integer assignDc,
			@RequestParam("stateId") String stateId1,
			@RequestParam("districtId") String districtId1) {
		
		String getclaimList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			getclaimList = oneDcTaggedReportService.getDcDetailsList(userId,assignDc,stateId1,districtId1);
			details.put("status", "success");
			details.put("details", getclaimList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return getclaimList;
	}

}
