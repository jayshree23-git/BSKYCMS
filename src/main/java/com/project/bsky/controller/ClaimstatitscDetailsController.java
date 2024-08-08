/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.bsky.service.ClaimstatitscDetailsService;

/**
 * @author hrusikesh.mohanty

 *
 */



@Controller
@RequestMapping(value = "/api")
public class ClaimstatitscDetailsController {

	@Autowired
	private ClaimstatitscDetailsService claimstatitscDetailsService;
	
	@Autowired
	private Logger logger;
	
	@GetMapping(value = "/getClaimStatictscDetails")
	@ResponseBody
	public List<Object> getClaimStatictsDetails(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "stateid", required = false) String stateid,
			@RequestParam(value = "districtvalue", required = false) String districtvalue,
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode,
			@RequestParam(value = "eventName", required = false) String eventName
//			@RequestParam(value = "userid", required = false) String userid
			) {
		List<Object> claimstatisticsdetails = null;		
		try {
			claimstatisticsdetails = claimstatitscDetailsService.getclaimStaticStiDetailsReport(fromDate,toDate,stateid,districtvalue,hospitalcode,eventName);			
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimstatisticsdetails;

	}
}
	
	
	
