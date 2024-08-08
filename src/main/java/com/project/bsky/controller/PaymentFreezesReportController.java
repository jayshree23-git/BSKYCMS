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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.PaymentFreezesReportService;

/**
 * @author priyanka.singh
 *
 */
@RestController
@RequestMapping(value = "/api")
public class PaymentFreezesReportController {

	@Autowired
	private PaymentFreezesReportService paymentFreezesReportService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getpaymentfreezdetails")
	@ResponseBody
	public List<Object> getpaymentfreezdata(@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "snadoctor", required = false) Long snoUserId,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode) {
		List<Object> getpaymentfreezdata = null;
		try {
			getpaymentfreezdata = paymentFreezesReportService.getpaymentfreezdetails(fromdate, todate, snoUserId,
					hospitalCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getpaymentfreezdata;

	}

}
