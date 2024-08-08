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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.HospitalPaidClaimBean;
import com.project.bsky.service.HospitalPaidClaimService;

/**
 * @author jayshree.moharana
 *
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class HospitalPaidClaimReportController {

	@Autowired
	private HospitalPaidClaimService hospitalpaidclaimservice;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/gethospitalwusecountresult")
	public List<HospitalPaidClaimBean> gethospitalwisesummaryreportcountresult(
			@RequestParam(required = false, value = "formdate") Date formdate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "searchtype") Integer searchtype,
			@RequestParam(required = false, value = "userId") String userId) {
		return hospitalpaidclaimservice.gethospitalwisesummaryreportcountresult(formdate, todate, userId, searchtype);
	}

	@GetMapping(value = "/gethospitaldetailsinnerpage")
	public ResponseEntity<?> gethospitaldetailsinnerpage(
			@RequestParam(required = false, value = "formdate") Date formdate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "searchtype") Integer searchtype,
			@RequestParam(required = false, value = "userId") String userId) {
		Map<String, Object> details = new HashMap<String, Object>();
		List<Object> listdetails = new ArrayList<Object>();
		try {
			listdetails = hospitalpaidclaimservice.gethospitaldetailsinnerpage(formdate, todate, searchtype, userId);
			details.put("statusCode", 200);
			details.put("status", "Success");
			details.put("data", listdetails);
			details.put("msg", "gethospitaldetailsinnerpage List Fetched Successfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("statusCode", 500);
			details.put("status", "Fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok().body(details);

	}
}
