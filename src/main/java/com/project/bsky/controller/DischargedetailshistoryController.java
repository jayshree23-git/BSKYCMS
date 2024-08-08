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

import com.project.bsky.service.DischargedetailshistoryService;

/**
 * @author hrusikesh.mohanty
 *
 */
@Controller
@RequestMapping(value = "/api")
public class DischargedetailshistoryController {
	
	@Autowired
	private Logger logger;
	
	@Autowired
	private DischargedetailshistoryService dischargedetailshistory;
	
	@GetMapping(value = "/DischargedetailsHistory")
	@ResponseBody
	public List<Object> Claimdetails(@RequestParam("transactiondetailsid") String transactiondetailsid) {
		List<Object> claim = null;		
		try {
			claim = dischargedetailshistory.getClaimdetails(transactiondetailsid);			
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claim;

	}
}
