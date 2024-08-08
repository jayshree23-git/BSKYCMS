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
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.YearWiseGenderDataReportService;

/**
 * @author priyanka.singh
 *
 */
@RestController
@RequestMapping(value = "/api")
public class YearWiseGenderDataReportController {

	@Autowired
	private YearWiseGenderDataReportService yearWiseGenderDataReportservice;

	@Autowired
	private Logger logger;

	@GetMapping("/getbenificiarygenderdtls")
	public List<Object> getbenificiarygenderdetails(@RequestParam(value = "age", required = false) Long age,
			@RequestParam(value = "ageconditions", required = false) String ageconditions) {
		List<Object> getbenificiarygenderdtls = null;
		try {
			getbenificiarygenderdtls = yearWiseGenderDataReportservice.getbenificiarygenderdtls(age, ageconditions);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getbenificiarygenderdtls;
	}

	@GetMapping("/getbenificiarygenderblockdetails")
	public List<Object> getbenificiarygenderblock(@RequestParam("districtId") String districtId) {
		List<Object> getbenificiarygenderblock = null;
		try {
			getbenificiarygenderblock = yearWiseGenderDataReportservice.getbenificiarygenderblockdata(districtId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getbenificiarygenderblock;
	}

	@GetMapping("/getbenificiarygendergramdetail")
	public List<Object> getbenificiarygendergramdata(@RequestParam("districtId") String districtId,
			@RequestParam("blockId") String blockId) {
		List<Object> getbenificiarygendergramdata = null;
		try {
			getbenificiarygendergramdata = yearWiseGenderDataReportservice.getbenificiarygendergramdetails(districtId,
					blockId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getbenificiarygendergramdata;
	}

	@GetMapping("/getbenificiaryvillagedetails")
	public List<Object> getbenificiarygendervillagedata(@RequestParam("districtId") String districtId,
			@RequestParam("blockId") String blockId, @RequestParam("gramId") String gramId) {
		List<Object> getbenificiarygendervillagedata = null;
		try {
			getbenificiarygendervillagedata = yearWiseGenderDataReportservice
					.getbenificiarygendervillagedata(districtId, blockId, gramId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getbenificiarygendervillagedata;
	}

	@GetMapping("/getbenificiarydata")
	public List<Object> getbenificiarydetails(@RequestParam("districtId") String districtId,
			@RequestParam("blockId") String blockId, @RequestParam("gramId") String gramId,
			@RequestParam("villageId") String villageId) {
		List<Object> getbenificiarydata = null;
		try {
			getbenificiarydata = yearWiseGenderDataReportservice.getbenificiarydata(districtId, blockId, gramId,
					villageId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getbenificiarydata;
	}

}
