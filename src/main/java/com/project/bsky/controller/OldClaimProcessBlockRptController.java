/**
 * 
 */
package com.project.bsky.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.OldClaimProcessBlockRptService;

/**
 * @author priyanka.singh
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class OldClaimProcessBlockRptController {

	@Autowired
	private OldClaimProcessBlockRptService oldProcessBlockRptService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/oldclaimprocessblockDataReport")
	public String oldclaimprocessblockData(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "stateId1", required = false) String stateId,
			@RequestParam(value = "districtId1", required = false) String districtId,
			@RequestParam(value = "hospitalCode1", required = false) String hospitalCode) {

		String getclaimList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			getclaimList = oldProcessBlockRptService.oldclaimprocessblockData(userId, fromdate, todate, stateId,
					districtId, hospitalCode);
			details.put("status", "success");
			details.put("details", getclaimList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return getclaimList;
	}

	@ResponseBody
	@GetMapping(value = "/oldprocessdischargeReport")
	public String oldprocessdischargeReport(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "stateId1", required = false) String stateId,
			@RequestParam(value = "districtId1", required = false) String districtId,
			@RequestParam(value = "hospitalCode1", required = false) String hospitalCode) {

		String getclaimList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			getclaimList = oldProcessBlockRptService.getOldDischargeData(userId, fromdate, todate, stateId, districtId,
					hospitalCode);
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
