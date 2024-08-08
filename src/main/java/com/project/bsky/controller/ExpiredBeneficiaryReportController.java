/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.ExpiredBeneficiaryBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.service.ExpiredBeneficiaryReportService;

/**
 * @author priyanka.singh
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class ExpiredBeneficiaryReportController {

	@Autowired
	private Logger logger;

	@Autowired
	private ExpiredBeneficiaryReportService expiredBenefReportService;

	@ResponseBody
	@GetMapping(value = "/getExpiredBeneficiaryDetails")
	public String expiredBeneficiaryDetails(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "stateId1", required = false) String stateId,
			@RequestParam(value = "districtId1", required = false) String districtId,
			@RequestParam(value = "urn", required = false) String urn,
			@RequestParam(value = "hospitalCode1", required = false) String hospitalCode) throws Exception {
		logger.info("Inside ExpiredBeneficiaryDetails Method of ExpiredBeneficiaryReportController");
		String getclaimList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			getclaimList = expiredBenefReportService.expiredBeneficiaryDetails(userId, fromdate, todate, stateId,
					districtId, hospitalCode,urn);
			details.put("status", "success");
			details.put("details", getclaimList);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in ExpiredBeneficiaryDetails Method of ExpiredBeneficiaryReportController : "
							+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return getclaimList;

	}

	@ResponseBody
	@GetMapping(value = "/getExpiredUpdateData")
	public Response expiredBeneficiary(ExpiredBeneficiaryBean expiredBeneficiaryBean) throws Exception {
		logger.info("Inside ExpiredBeneficiaryUpdate Method of ExpiredBeneficiaryReportController");
		Response rsponse = new Response();
		try {
			rsponse = expiredBenefReportService.expiredBeneficiaryUpdate(expiredBeneficiaryBean);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in ExpiredBeneficiaryUpdate Method of ExpiredBeneficiaryReportController : "
							+ e.getMessage());
			rsponse.setStatus("400");
			rsponse.setMessage("Some Error Happen");
		}
		return rsponse;
	}

	@ResponseBody
	@GetMapping(value = "/genrateOtpFormExpired")
	public AuthRequest generateotp(@RequestParam(value = "userid", required = false) String userid) {
		AuthRequest auth = new AuthRequest();
		try {
			Long userId;
			userId = Long.parseLong(userid);
			auth = expiredBenefReportService.generateotp(userId);
		} catch (Exception e) {
			auth.setStatus("fail");
			auth.setMessage("Some error happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return auth;
	}

	@ResponseBody
	@GetMapping(value = "/validateotpforexpiredBeneficiary")
	public AuthRequest validateotpchkbalance(@RequestParam(value = "otp", required = false) String otp,
			@RequestParam(value = "accessid", required = false) Long accessid) {
		AuthRequest auth = new AuthRequest();
		try {
			auth = expiredBenefReportService.validateotphosp(accessid, otp);

		} catch (Exception e) {
			auth.setStatus("fail");
			auth.setMessage("Some error happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return auth;
	}

	@ResponseBody
	@GetMapping(value = "/getMakeAliveListData")
	public String aliveBeneficiaryDetails(@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "stateId1", required = false) String stateId,
			@RequestParam(value = "districtId1", required = false) String districtId,
			@RequestParam(value = "hospitalCode1", required = false) String hospitalCode) throws Exception {
		logger.info("Inside ExpiredBeneficiaryDetails Method of ExpiredBeneficiaryReportController");
		String getclaimList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			getclaimList = expiredBenefReportService.aliveBeneficiaryDetails(userId, fromdate, todate, stateId,
					districtId, hospitalCode);
			details.put("status", "success");
			details.put("details", getclaimList);
		} catch (Exception e) {
			logger.error("Exception Occurred in AliveBeneficiaryDetails Method of ExpiredBeneficiaryReportController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return getclaimList;

	}
	
	@ResponseBody
	@GetMapping(value = "/getactionlogofmakealive")
	public List<Object> getactionlogofmakealive(@RequestParam(value = "claimid", required = false) Long claimid) throws Exception {
		List<Object> details = new ArrayList<>();
		try {
			details = expiredBenefReportService.getactionlogofmakealive(claimid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return details;

	}
	
	@ResponseBody
	@GetMapping(value = "/getmortality")
	public Map<String,Object> getmortality(@RequestParam(value = "claimid", required = false) Long claimid) throws Exception {
		Map<String,Object> details = new HashMap<>();
		try {
			details = expiredBenefReportService.getmortality(claimid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return details;

	}
}
