/**
 * 
 */
package com.project.bsky.controller;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.SpecialityWiseDistrictReportService;

/**
 * @author priyanka.singh
 *
 */
@RestController
@RequestMapping(value = "/api")
public class SpecialityWiseDistrictReportController {

	@Autowired
	private SpecialityWiseDistrictReportService specialityWiseDistrictReportService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getSpecialityWiseDistrictRpt")
	@ResponseBody
	public String getSpecialityWiseData(@RequestParam(value = "state", required = false) String stateId,
			@RequestParam(value = "dist", required = false) String districtId,
			@RequestParam(value = "specialityCode", required = false) String specialityCode) {

		String getpackageWiseDischarge = null;
		Map<String, String> details = new HashedMap<String, String>();
		
		try {
			getpackageWiseDischarge = specialityWiseDistrictReportService.getSpecialityWiseData(stateId, districtId,
					specialityCode);
			details.put("status", "success");
			details.put("details", getpackageWiseDischarge);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("status", e.getMessage());
		}
		return getpackageWiseDischarge;

	}

}
