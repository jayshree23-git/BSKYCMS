/**
 * 
 */
package com.project.bsky.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.MEDischargeReportBean;
import com.project.bsky.service.MonthWiseFloatDetailsReportService;

/**
 * @author priyanka.singh
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class MonthWiseFloatDetailsReportController {

	@Autowired
	private Logger logger;

	@Autowired
	private MonthWiseFloatDetailsReportService monthWiseFloatDetailsReportService;

	@ResponseBody
	@GetMapping(value = "/getMonthWiseFloatDetails")
	public String MonthWiseFloatDetails(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "stateId1", required = false) String stateId,
			@RequestParam(value = "districtId1", required = false) String districtId,
			@RequestParam(value = "hospitalCode1", required = false) String hospitalCode) throws Exception {
		logger.info("Inside MonthWiseFloatDetails Method of MonthWiseFloatDetailsReportController");
		String getclaimList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			getclaimList = monthWiseFloatDetailsReportService.MonthWiseFloatDetails(userId, fromdate, todate, stateId,
					districtId, hospitalCode);
			details.put("status", "success");
			details.put("details", getclaimList);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in MonthWiseFloatDetails Method of MonthWiseFloatDetailsReportController : "
							+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return getclaimList;

	}

}
