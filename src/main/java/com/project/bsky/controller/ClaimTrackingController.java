package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.bsky.model.HospitalInformation;
import com.project.bsky.service.ClaimTrackingService;

/**
 * @author jayshree.moharana
 *
 */
@CrossOrigin
@Controller
@RequestMapping(value = "/api")

public class ClaimTrackingController {
	@Autowired
	private ClaimTrackingService claimtracking;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getclaimTracking")
	public ResponseEntity<?> getclaimTracking(@RequestParam(value = "fromDate", required = false) Date fromdate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "urn", required = false) String urn,
			@RequestParam(value = "claimno", required = false) String claimno,
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode,
			@RequestParam(value = "searchby", required = false) Integer searchby,
			@RequestParam(value = "pageIn", required = false) Integer pageIn,
			@RequestParam(value = "pageEnd", required = false) Integer pageEnd) throws Exception {
		Map<String, Object> detailsadmin = new HashMap<String, Object>();
		List<Object> trackinglistclaimListadmin = new ArrayList<Object>();
		Long size = null;
		try {
			Map<Long, List<Object>> getclaimtracking = claimtracking.getclaimreport(fromdate, toDate, urn, claimno,
					hospitalcode, searchby, pageIn, pageEnd);
			for (Map.Entry<Long, List<Object>> entry : getclaimtracking.entrySet()) {
				size = entry.getKey();
				trackinglistclaimListadmin = entry.getValue();
			}
			detailsadmin.put("status", "success");
			detailsadmin.put("size", size);
			detailsadmin.put("data", trackinglistclaimListadmin);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			detailsadmin.put("status", "fail");
			detailsadmin.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(detailsadmin);
	}

	@ResponseBody
	@GetMapping(value = "/gethospitalclaimTracking")
	public ResponseEntity<?> gethospitalclaimTracking(@RequestParam(value = "userid", required = false) String userid,
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode,
			@RequestParam(value = "fromDate", required = false) Date fromdate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "urn", required = false) String urn,
			@RequestParam(value = "searchby", required = false) Long searchby,
			@RequestParam(value = "pageIn", required = false) Integer pageIn,
			@RequestParam(value = "pageEnd", required = false) Integer pageEnd) throws Exception {
		Map<String, Object> details = new HashMap<String, Object>();
		List<Object> trackinglistclaimList = new ArrayList<Object>();
		Long size = null;
		try {
			Map<Long, List<Object>> getclaimtracking = claimtracking.gethospitalclaimTracking(fromdate, userid, toDate,
					urn, searchby, hospitalcode, pageIn, pageEnd);
			for (Map.Entry<Long, List<Object>> entry : getclaimtracking.entrySet()) {
				size = entry.getKey();
				trackinglistclaimList = entry.getValue();
			}
			details.put("status", "success");
			details.put("size", size);
			details.put("data", trackinglistclaimList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/getclaimdetailsbyclaimid")
	@ResponseBody
	public String Claimdetails(@RequestParam("claimid") Integer claimid) {
		String claim = null;
		try {
			claim = claimtracking.getClaimdetails(claimid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claim;
	}

	@GetMapping(value = "/getdchospitallist")
	@ResponseBody
	public List<HospitalInformation> dchospitallist(@RequestParam(value = "userid", required = false) Long userid) {
		List<HospitalInformation> dchospital = null;
		try {
			dchospital = claimtracking.dchospitallist(userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return dchospital;
	}
	
	@GetMapping(value = "/getvitalparameterdetails")
	@ResponseBody
	public String getvilatparameterdetails(@RequestParam("urn") String urn) {
		String claim = null;
		try {
			claim = claimtracking.getvilatparameterdetails(urn);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claim;
	}
}
