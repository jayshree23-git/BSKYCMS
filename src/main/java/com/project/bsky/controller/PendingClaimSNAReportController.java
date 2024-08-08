/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.PendingClaimSNAReportService;

/**
 * @author priyanka.singh
 *
 */
@RestController
@RequestMapping(value = "/api")
public class PendingClaimSNAReportController {

	@Autowired
	private PendingClaimSNAReportService snoPendingClaimReportService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getSNATagHospitalName")
	public String getHospital(@RequestParam("userid") Integer snoUserId) {
		JSONArray hospitalList = null;
		try {

			hospitalList = snoPendingClaimReportService.getHospitalListBySNOId(snoUserId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return hospitalList.toString();
	}

	@GetMapping(value = "/getSNADischargeDetails")
	@ResponseBody
	public List<Object> getSnoPendingClaimDetails(@RequestParam(value = "snadoctor", required = false) Long snoUserId,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode) {
		List<Object> getSnoPendingClaimDetails = null;
		try {
			getSnoPendingClaimDetails = snoPendingClaimReportService.getPendingSnoClaimDetails(snoUserId, hospitalCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getSnoPendingClaimDetails;

	}

}
