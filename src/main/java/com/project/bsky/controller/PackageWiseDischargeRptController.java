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

import com.project.bsky.service.PackageWiseDischargeRptService;

/**
 * @author priyanka.singh
 *
 */
@RestController
@RequestMapping(value = "/api")
public class PackageWiseDischargeRptController {

	@Autowired
	private PackageWiseDischargeRptService packageWiseRptService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getpackageWiseDischargedetails")
	@ResponseBody
	public List<Object> getpackageWiseDischargedata(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "stateId1", required = false) String stateId,
			@RequestParam(value = "districtId1", required = false) String districtId,
			@RequestParam(value = "hospitalCode1", required = false) String hospitalCode) {
		List<Object> getpackageWiseDischarge = null;
		try {
			getpackageWiseDischarge = packageWiseRptService.getpackageWiseDischargedetails(userId, fromdate, todate,
					stateId, districtId, hospitalCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getpackageWiseDischarge;
	}

	@GetMapping(value = "/getpackageData")
	@ResponseBody
	public List<Object> getpackageData(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "hosp", required = false) String hosp,
			@RequestParam(value = "packageHeader", required = false) String packageHeader,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) {
		List<Object> getpackageWiseDischarge = null;
		try {
			getpackageWiseDischarge = packageWiseRptService.getpackageData(userId, state, dist, hosp, packageHeader,
					fromDate, toDate);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getpackageWiseDischarge;
	}

	@GetMapping(value = "/getpackgebenificiarydata")
	@ResponseBody
	public List<Object> getpackgebenificiarydata(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "district", required = false) String district,
			@RequestParam(value = "hospital", required = false) String hospital,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "packageCode", required = false) String packageCode) {
		List<Object> getpackageWiseDischarge = null;
		try {
			getpackageWiseDischarge = packageWiseRptService.getpackgebenificiarydata(userId, state, district, hospital,
					fromDate, toDate, packageCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getpackageWiseDischarge;

	}
}
