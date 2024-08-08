package com.project.bsky.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.TxnclamFloateDetails;
import com.project.bsky.service.SnaFloatReportService;

@RestController
@RequestMapping(value = "/api")
public class SnaFloatReportController {
	@Autowired
	private SnaFloatReportService snafloatreportservice;
	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getsnafloatdetails")
	public List<TxnclamFloateDetails> getfodetails(
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "floatno", required = false) String floatno){
		List<TxnclamFloateDetails> snaList = null;
		try {
			snaList=snafloatreportservice.getfloatdetails(userId,fromDate,toDate,floatno);
			
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return snaList;
		
	}
}
