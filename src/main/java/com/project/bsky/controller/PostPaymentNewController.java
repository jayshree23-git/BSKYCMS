/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.PostPaymentRequestNew;
import com.project.bsky.bean.Response;
import com.project.bsky.service.PaymentFreezeService;

/**
 * Author Name :-Hrusikesh Mohanty Purpose :-Generate New
 * PostPaymentNewController list , Date :-18-JUN-2024
 */
@RestController
@RequestMapping(value = "/api")
public class PostPaymentNewController {
	@Autowired
	private Logger logger;

	@Autowired
	private PaymentFreezeService paymentFreezeService;

	@GetMapping(value = "/getprocessfloatreport")
	@ResponseBody
	public Map<String, Object> getprocessfloatreport(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "todate", required = false) Date todate,
			@RequestParam(value = "snadoctor", required = false) Long snadoctor,
			@RequestParam(value = "userid", required = false) Long userid) {
		Map<String, Object> list = new HashMap<>();
		try {
			list.put("data", paymentFreezeService.getprocessfloatreport(formdate, todate, snadoctor, userid));
			list.put("status", HttpStatus.OK.value());
			list.put("message", "Successful");
		} catch (Exception e) {
			e.printStackTrace();
			list.put("status", HttpStatus.BAD_REQUEST.value());
			list.put("message", "Error");
			list.put("error", e.getMessage());
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/getFloatDescriptiondata")
	public List<Object> getFloatDescriptiondata(@RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate") Date toDate, @RequestParam(value = "floatnumber") String floatnumber,
			@RequestParam(value = "snauserid") String snauserid) {
		List<Object> desclist = null;
		try {
			desclist = paymentFreezeService.getFloatDescriptiondataList(fromDate, toDate, floatnumber, snauserid);
		} catch (Exception e) {
			logger.error("Exception occured in getFloatDescriptiondata method of ReportController" + e.getMessage());
		}
		return desclist;
	}

	@ResponseBody
	@GetMapping(value = "/getfloatdetailshospitalwiseabstaact")
	public Map<String, Object> getfloatdetailshospitalwiseabstaact(
			@RequestParam(value = "floateno", required = false) String floateno) {
		Map<String, Object> map = new HashedMap<>();
		try {
			map.put("data", paymentFreezeService.getfloatdetailshospitalwiseabstaact(floateno));
			map.put("status", 200);
			map.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 400);
			map.put("error", e.getMessage());
			map.put("data", new ArrayList<>());
		}
		return map;
	}

	@PostMapping(value = "/postpaymentupdationnew")
	public ResponseEntity<?> updatePostPaymentnew(@RequestBody PostPaymentRequestNew paymentRequest) {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = paymentFreezeService.updatePostPaymentnew(paymentRequest);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@GetMapping(value = "/getfloatdetailshospitalwiseabstaactLogRecord")
	public Map<String, Object> getfloatdetailshospitalwiseabstaactLogRecord(
			@RequestParam(value = "floateno", required = false) String floateno,
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode) {
		Map<String, Object> map = new HashedMap<>();
		try {
			map.put("data", paymentFreezeService.getfloatdetailshospitalwiseabstaactLogRecord(floateno, hospitalcode));
			map.put("status", 200);
			map.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 400);
			map.put("error", e.getMessage());
			map.put("data", new ArrayList<>());
		}
		return map;
	}

	@ResponseBody
	@GetMapping(value = "/getfloatdetailshospitalwiseabstaactView")
	public Map<String, Object> getfloatdetailshospitalwiseabstaactView(
			@RequestParam(value = "floateno", required = false) String floateno,
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode) {
		Map<String, Object> map = new HashedMap<>();
		try {
			map.put("data", paymentFreezeService.getfloatdetailshospitalwiseabstaactView(floateno, hospitalcode));
			map.put("status", 200);
			map.put("msg", "success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 400);
			map.put("error", e.getMessage());
			map.put("data", new ArrayList<>());
		}
		return map;
	}
}
