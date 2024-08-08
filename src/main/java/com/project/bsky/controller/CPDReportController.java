package com.project.bsky.controller;

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

import com.project.bsky.service.CPDCountReportService;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class CPDReportController {

	@Autowired
	private CPDCountReportService cpdcountreportservice;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getcpddetailsreport")
	public Map<String, Object> getcpddetailsreport(@RequestParam(required = false, value = "userid") Long userid,
			@RequestParam(required = false, value = "date") String date,
			@RequestParam(required = false, value = "flag") String flag) {
		return cpdcountreportservice.details(userid, date, flag);
	}
}
