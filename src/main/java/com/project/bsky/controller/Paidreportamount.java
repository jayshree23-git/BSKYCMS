/**
 * 
 */
package com.project.bsky.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.PaidreportamountService;

/**
 * @author hrusikesh.mohanty
 *
 */
@RestController
@RequestMapping(value = "/api")
public class Paidreportamount {
	
	@Autowired
	private PaidreportamountService paidreportamountService;
	
	@Autowired
	private Logger logger;


	@ResponseBody
	@GetMapping(value = "/getdatapaidreport")
	public Map<String, Object> getserachvaluepaidreport(
			@RequestParam(value = "userid") Long userid,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "fromdate") String fromdate,
			@RequestParam(value = "todate") String todate,
			@RequestParam(value = "groupId") Long groupId,
			@RequestParam(value = "state") String state,
			@RequestParam(value = "districtId") String districtId,
			@RequestParam(value = "hospitalCode") String hospitalCode
			) throws Exception {
		
		Map<String, Object> paidamount =new LinkedHashMap<>();
		try {
			paidamount = paidreportamountService.getsearchvalue(userid,username,fromdate,todate,groupId,state,districtId,hospitalCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return paidamount;
	}
	
	@ResponseBody
	@GetMapping(value = "/getdataforinternals")
	public Map<String, Object> getserachvaluepaidreport(
			@RequestParam(value = "paymentdate") String paymentdate,
			@RequestParam(value = "number") Integer number,
			@RequestParam(value = "totaldischarge") String totaldischarge,
			@RequestParam(value = "Hospitalcode") String Hospitalcode,
			@RequestParam(value = "groupId") String groupId
			) throws Exception {
		
		Map<String, Object> internaldata =new LinkedHashMap<>();
		try {
			internaldata = paidreportamountService.getsearchvalue(paymentdate,number,totaldischarge,Hospitalcode,groupId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return internaldata;
	}
	
	
	
	
	
	
	
	
}
