/**
 * 
 */
package com.project.bsky.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.OldBlockDataReportService;

/**
 * @author priyanka.singh
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class OldBlockDataReportController {

	@Autowired
	private OldBlockDataReportService oldBlockDataReportService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getOldBlockDatareport")
	public List<Object> OldBlockDataReport(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "stateId1", required = false) String stateId,
			@RequestParam(value = "districtId1", required = false) String districtId,
			@RequestParam(value = "hospitalCode1", required = false) String hospitalCode) {
		List<Object> getOldBlockData = null;
		try {
			getOldBlockData = oldBlockDataReportService.OldBlockData(userId, fromdate, todate, stateId, districtId,
					hospitalCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getOldBlockData;

	}

	@ResponseBody
	@GetMapping(value = "/getOldBlockDataDetails")
	public List<Object> OldBlockDataReportList(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "reportData", required = false) String reportData,
			@RequestParam(value = "stat", required = false) String stateId,
			@RequestParam(value = "dist", required = false) String districtId,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode) {
		List<Object> getOldBlockData = null;
		try {
			getOldBlockData = oldBlockDataReportService.OldBlockDataReportList(userId, reportData, stateId, districtId,
					hospitalCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getOldBlockData;
	}

	@GetMapping(value = "/getoldblockgenericsearch")
	@ResponseBody
	public ResponseEntity<?> getoldblockgenericsearch(
			@RequestParam(value = "fieldvalue", required = false) String fieldvalue) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data", oldBlockDataReportService.getoldblockgenericsearch(fieldvalue));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@GetMapping(value = "/getoldblockdataviewlist")
	@ResponseBody
	public ResponseEntity<?> getoldblockdataviewlist(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "todate", required = false) Date todate,
			@RequestParam(value = "stetecode", required = false) String stetecode,
			@RequestParam(value = "distcode", required = false) String distcode,
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data", oldBlockDataReportService.getoldblockdataviewlist(formdate, todate, stetecode, distcode,
					hospitalcode));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@GetMapping(value = "/getoldblockdataviewdetails")
	@ResponseBody
	public ResponseEntity<?> getoldblockdataviewdetails(@RequestParam(value = "txnid", required = false) Long txnid) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data", oldBlockDataReportService.getoldblockdataviewdetails(txnid));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

}
