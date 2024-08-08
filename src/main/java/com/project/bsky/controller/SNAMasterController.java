package com.project.bsky.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.SNAMasterService;

/**
 * @author ronauk
 *
 */
@RestController
@RequestMapping(value = "/api")
public class SNAMasterController {
	
	@Autowired
	private SNAMasterService snaService;
	
	@Autowired
	private Logger logger;
	
	@GetMapping("/getStateMasterDetails")
	public String getStateMasterDetails(@RequestParam("snoId") String snoId) {
		return snaService.getStateMasterDetails(snoId);
	}
	
	@GetMapping("/getDistrictDetailsByStateId")
	public String getDistrictDetailsByStateId(@RequestParam("snoId") String snoId, @RequestParam("stateCode") String stateCode) {
		String districtMaster = null;
		try {
			districtMaster = snaService.getDistrictDetailsByStateId(snoId, stateCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return districtMaster;
	}
	
	@GetMapping("/getHospitalByDistrictId")
	public String getHospitalByDistrictId(
			@RequestParam("snoId") String snoId,
			@RequestParam("districtCode") String districtCode,
			@RequestParam("stateCode") String stateCode) {
		String hospitalInformation = null;
		try {
			hospitalInformation = snaService.getHospitalByDistrictId(snoId, districtCode, stateCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalInformation;
	}

}
