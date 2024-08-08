/**
 * 
 */
package com.project.bsky.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.bsky.bean.Response;
import com.project.bsky.service.Cardbalanceupdateservice;

/**
 * @author hrusikesh.mohanty
 *
 */
@Controller
@RequestMapping(value = "/api")
public class Cardbalanceupdatecontroller {

	@Autowired
	private Cardbalanceupdateservice cardbalanceupdateservice;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getcarbalancedetailsbyurn")
	@ResponseBody
	public ResponseEntity<?> getCardBalanceDetails(@RequestParam("urn") String urn) throws Exception {
		// //System.out.println("URN: " + urn);
		String cardlistdetails = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			cardlistdetails = cardbalanceupdateservice.getCardBalanceDetails(urn);
			details.put("status", "success");
			details.put("data", cardlistdetails);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/refundAmount")
	@ResponseBody
	public ResponseEntity<?> refundAmount(@RequestParam("userid") Long userid,
			@RequestParam("memberId") String memberId, @RequestParam("urn") String urn,
			@RequestParam("balanceAmount") String balanceAmount, @RequestParam("claimid") Long claimid,
			@RequestParam("remarks") String remarks) throws Exception {
		Response response = new Response();
		try {
			response = cardbalanceupdateservice.refundAmount(userid, memberId, urn, Double.parseDouble(balanceAmount),
					claimid, remarks);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage(e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

}
