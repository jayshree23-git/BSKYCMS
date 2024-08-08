package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.SystemRejectedBean;
import com.project.bsky.service.ClaimRejectedService;

@RestController
@RequestMapping(value = "/api")
public class ClaimRejected {

	private final Logger logger;

	@Autowired
	public ClaimRejected(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private ClaimRejectedService claimRejectedService;

	@ResponseBody
	@GetMapping(value = "/getrejectedlist")
	public List<Object> getallDataForRejected(
			@RequestParam(value = "hospitalcoderejected", required = false) String hospitalcoderejected,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "Package", required = false) String Package,
			@RequestParam(value = "packagecode", required = false) String packagecode,
			@RequestParam(value = "URN", required = false) String URN,
			@RequestParam(value = "schemeid", required = false) String schemeid,
			@RequestParam(value = "schemecategoryid", required = false) String schemecategoryid) {
		List<Object> claiList = null;
		try {
			if (Objects.equals(fromDate, "") && Objects.equals(toDate, "")) {
				claiList = claimRejectedService.getrejectedlistdata(hospitalcoderejected, null, null, Package,
						packagecode, URN,schemeid,schemecategoryid);
			} else {
				claiList = claimRejectedService.getrejectedlistdata(hospitalcoderejected, fromDate, toDate, Package,
						packagecode, URN,schemeid,schemecategoryid);
			}

		} catch (Exception e) {
			logger.error("Exception occured in getallDataForRejected method of ClaimRejected" + e.getMessage());
		}
		return claiList;
	}

	@PostMapping(value = "/rejectRequest")
	public ResponseEntity<Response> saveRejectedRequest(@RequestBody SystemRejectedBean rejBean) {
		Response response = null;
		try {
			response = claimRejectedService.saveRejectRequest(rejBean);
		} catch (Exception e) {
			logger.error("Exception occured in getallDataForClaimRequest method of ClaimMonitoring" + e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/getByTransactionId")
	@ResponseBody
	public Map<String, String> RequestsById(@RequestParam("txnId") Integer txnId) {
		String requestList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			requestList = claimRejectedService.getByDetailId(txnId);
			details.put("status", "success");
			details.put("details", requestList);
		} catch (Exception e) {
			details.put("status", "fails");
			details.put("status", e.getMessage());
			logger.error("Exception occured in RequestsById method of ClaimRejected" + e.getMessage());
		}
		return details;
	}
}
