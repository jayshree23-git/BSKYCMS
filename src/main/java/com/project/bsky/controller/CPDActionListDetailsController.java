/**
 * 
 */
package com.project.bsky.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.UrnWiseDataBean;
import com.project.bsky.service.CPDActionListDetailsService;

/**
 * @author priyanka.singh
 *
 */

@RestController
@RequestMapping(value = "/api")
public class CPDActionListDetailsController {

	@Autowired
	private CPDActionListDetailsService cpDActionListDetailsService;

	@Autowired
	private Logger logger;

	@GetMapping("/getCpdActionListDetails")
	@ResponseBody
	public UrnWiseDataBean getUrnWiseDetailsReport(@RequestParam("urnNo") String urnNo,
			@RequestParam("transId") Long transId) {
		UrnWiseDataBean jsonArray = new UrnWiseDataBean();
		try {
			jsonArray = cpDActionListDetailsService.getCpdActionDetails(urnNo, transId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return jsonArray;
	}
	//For Draft Action List of CPD
	@GetMapping(value = "/getDraftHistoryofclaimno")
	@ResponseBody
	public Map<String, String> getDraftHistoryofclaimno(@RequestParam("claimId") Long claimId) {
		String claimhistory = null;
		Map<String, String> details = new HashMap<>();
		try {
			claimhistory = cpDActionListDetailsService.getDraftActionHistoryClaimNo(claimId);
			details.put("status", "success");
			details.put("details", claimhistory);
		} catch (Exception e) {
			logger.error("Exception Occurred in getDraftHistoryofclaimno Method of CPDActionListDetailsController : "+ e.getMessage());
			details.put("status", "fails");
			details.put("details", e.getMessage());
		}
		return details;
	}

}
