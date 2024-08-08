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

import com.project.bsky.service.SnaActionReportLogService;

/**
 * @author rajendra.sahoo
 *
 */

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class SnaActionReportLogController {

	@Autowired
	private SnaActionReportLogService snaactionreport;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/snaactionreport")
	public List<Object> snaactionreport(@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "year") String year,
			@RequestParam(required = false, value = "month") String month) {
		return snaactionreport.snaactionreport(userId, year, month);
	}

	@ResponseBody
	@GetMapping(value = "/snaactionreportdetails")
	public List<Object> snaactionreportdetails(@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "action") Integer actiontype,
			@RequestParam(required = false, value = "date") String date) {
		return snaactionreport.snaactionreportdetails(userId, actiontype, date);
	}

	@GetMapping(value = "/getsnawisepreauthcountdetails")
	@ResponseBody
	public ResponseEntity<?> getsnawisepreauthcountdetails(
			@RequestParam(value = "fromdate", required = false) Date fromdate,
			@RequestParam(value = "todate", required = false) Date todate,
			@RequestParam(value = "snadoctor", required = false) Long snadoctor,
			@RequestParam(value = "type", required = false) Integer type) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data", snaactionreport.getsnawisepreauthcountdetails(fromdate, todate, snadoctor, type));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
			details.put("error", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

}
