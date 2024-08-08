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

import com.project.bsky.service.SnaRejectedLIstService;

@RestController
@RequestMapping(value = "/api")
public class SnaRejetedList {
	
	@Autowired
	private SnaRejectedLIstService snarejetdlist;
	
	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getsnaRejetdList")
	public List<Object>snaRejetdList(@RequestParam(value = "userId", required = false) Long userId,
//			@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "fromDate", required = false) Date fromdate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "stateCode", required = false) String stateCode,
			@RequestParam(value = "distCode", required = false) String distCode,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode) throws Exception {
		////System.out.println(fromdate + "\t" + toDate + "\t" + stateCode + "\t" + distCode + "\t" + hospitalCode);

		List<Object> PaymentList = null;
//		Date fromdate=new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate);
//		Date toDate=new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate);	
		try {
			PaymentList = snarejetdlist.snaRejetdList(userId, fromdate, toDate, stateCode, distCode,
					hospitalCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return PaymentList;

	}

	

}