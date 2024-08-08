package com.project.bsky.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.CpdNamewiseCountreportService;

/**
 * @author jayshree.moharana
 *
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class CpdNamewiseCountReportController {
	@Autowired
	private CpdNamewiseCountreportService cpdnamewisecountreportservice;
	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getcpdnamewisecountreport")
	public String list(@RequestParam(required = false, value = "formdate") String formdate,
			@RequestParam(required = false, value = "todate") String todate) throws Exception {
		String getcpd = null;
		try {
			return getcpd = cpdnamewisecountreportservice.list(formdate, todate);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getcpd;
	}

	@GetMapping(value = "/getcpdnamewisdetailsreport")
	public String details(@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "formdate") String formdate,
			@RequestParam(required = false, value = "todate") String todate) throws Exception {
		String getcpd = null;
		try {
			return getcpd = cpdnamewisecountreportservice.details(userId, formdate, todate);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getcpd;
	}
}
