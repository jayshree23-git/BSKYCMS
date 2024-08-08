package com.project.bsky.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.HospitalInformation;
import com.project.bsky.service.HospitalWiseClaimReportService;

@RestController
@RequestMapping(value = "/api")
public class HospitalWiseClaimReportController {
	
	@Autowired
	private HospitalWiseClaimReportService hospitalWiseClaimReportService;
	
	@Autowired
	private Logger logger;
	
	@ResponseBody
	@GetMapping(value="/getAllHospitalListForAdmin")
	public List<HospitalInformation> getHospitalListForAdmin(){
		List<HospitalInformation> hospitalList=null;
		try {
			hospitalList=hospitalWiseClaimReportService.getDetails();
			return hospitalList;
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalList;	
	}
	
	
	@GetMapping("/getAllDischargeCountAndAllsubmittedCount")
	public List<Object> Search(@RequestParam("userId") Long userId,@RequestParam("fromDate") Date fromDate,@RequestParam("toDate") Date toDate,@RequestParam("hospitalId") String hospitalId) {
		List<Object> hospitalwisedischargecount = null;
		try {
			hospitalwisedischargecount = hospitalWiseClaimReportService.search(userId,fromDate,toDate,hospitalId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalwisedischargecount;
	}
	
	@GetMapping("/getAllDischargeCountAndAllsubmittedDetails")
	public List<Object> Searchdetails(@RequestParam("userId") Long userId,@RequestParam("fromDate") Date fromDate,@RequestParam("toDate") Date toDate,@RequestParam("hospitalId") String hospitalId,@RequestParam("serchby") String serchby) {
		List<Object> hospitalwisedischargecountdetails = null;
		try {
			////System.out.println(userId+" "+fromDate+" "+toDate+" "+hospitalId+" "+serchby);
			hospitalwisedischargecountdetails = hospitalWiseClaimReportService.details(userId,fromDate,toDate,hospitalId,serchby);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalwisedischargecountdetails;
	}
	
}
