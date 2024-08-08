package com.project.bsky.controller;

import java.util.ArrayList;
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

import com.project.bsky.service.PaymentfreezeReportService;

@RestController
@RequestMapping(value = "/api")
public class PaymentfreezeReportController {
	
	@Autowired
	private PaymentfreezeReportService paymentservice;
	
	@Autowired
	private Logger logger;
	
	@ResponseBody
	@GetMapping(value = "/getpaymentfreezereport")
	public List<Object> getpaymentfreezereport(@RequestParam(required = false, value = "formdate") Date formdate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId,
			@RequestParam(required = false, value = "hospitalId") String hospitalId,
			@RequestParam(required = false, value = "userId") String userId
			) {
		List<Object> getpaymentfreezereport = new ArrayList<Object>();
		try {
			getpaymentfreezereport=paymentservice.getpaymentfreezereport(formdate,todate,stateId,districtId,hospitalId,userId);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	return getpaymentfreezereport;
	}

}
