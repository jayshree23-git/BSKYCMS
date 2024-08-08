package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.OldandnewclaimDetailsService;

@RestController
@RequestMapping(value = "/api")
public class OldandnewclaimDetails {
	private final Logger logger;

	@Autowired
	public OldandnewclaimDetails(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private OldandnewclaimDetailsService oldandnewclaimDetailsService;

	@GetMapping(value = "/getdetailsoldandnewclaimdetailsforview")
	public ResponseEntity<?> getdetailsoldandnewclaimdetailsforview(
			@RequestParam(value = "urnnumber", required = false) String urnnumber,
			@RequestParam(value = "claimid", required = false) String claimid,
			@RequestParam(value = "selctedvalue", required = false) String selctedvalue,
			@RequestParam(value = "claimnumber", required = false) String claimnumber,
			@RequestParam(value = "transactiondetailsid", required = false) String transactiondetailsid,
			@RequestParam(value = "authorizedcode", required = false) String authorizedcode,
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			Map<String, Object> listdetails = oldandnewclaimDetailsService.detailsoldclaimandnewclaimforview(urnnumber,
					claimid, selctedvalue, claimnumber, transactiondetailsid, authorizedcode, hospitalcode);
			details.put("status", "success");
			details.put("data", listdetails);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getdetailsoldandnewclaimdetailsforview Method of detailsoldclaimandnewclaimforview : "
							+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}
}
