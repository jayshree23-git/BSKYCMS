/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.SNAWiseDischargeandClmReportService;

/**
 * @author priyanka.singh
 *
 */
@RestController
@RequestMapping(value = "/api")
public class SNAWiseDischargeandClmReportController {

	private final Logger logger;

	@Autowired
	public SNAWiseDischargeandClmReportController(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private SNAWiseDischargeandClmReportService snaWiseDischargeandservice;

	@GetMapping(value = "/getSNADischargeclaimDetails")
	@ResponseBody
	public List<Object> getSNADischargeclaimDetails(@RequestParam(value = "snadoctor", required = false) Long snoUserId,
			@RequestParam(value = "year", required = false) Long year) {
		List<Object> getSNADischargeclaimData = null;
		try {
			getSNADischargeclaimData = snaWiseDischargeandservice.getSNADischargeclaimData(snoUserId, year);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getSNADischargeclaimData;

	}

	@GetMapping(value = "/snamonthwisedischargelist")
	@ResponseBody
	public List<Object> snamonthwisedischargelist(@RequestParam(value = "userid", required = false) Long UserId,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "month", required = false) Integer month) {
		List<Object> getSNADischargeclaimData = null;
		try {
			getSNADischargeclaimData = snaWiseDischargeandservice.snamonthwisedischargelist(UserId, year, month);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getSNADischargeclaimData;

	}

	@GetMapping(value = "/hospitalwisedischargelist")
	@ResponseBody
	public List<Object> hospitalwisedischargelist(@RequestParam(value = "userid", required = false) Long UserId,
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "statecode", required = false) String statecode,
			@RequestParam(value = "distcode", required = false) String distcode,
			@RequestParam(value = "hospcode", required = false) String hospcode) {
		List<Object> getSNADischargeclaimData = null;
		try {
			getSNADischargeclaimData = snaWiseDischargeandservice.hospitalwisedischargelist(UserId, year, month,
					statecode, distcode, hospcode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getSNADischargeclaimData;
	}

}
