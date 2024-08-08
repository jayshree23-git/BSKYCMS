package com.project.bsky.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.DashboardBean;
import com.project.bsky.bean.FloatReportBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.SnaPaymentStatusBean;
import com.project.bsky.service.ClaimReportService;
import com.project.bsky.service.DashboardService;
import com.project.bsky.util.CommonClassHelper;

/**
 * @author arabinda.guin
 *
 */
@CrossOrigin
@Controller
@RequestMapping(value = "/api")
public class ReportController {

	@Autowired
	private ClaimReportService claimcount;

	@Autowired
	private DashboardService dashService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/getSnaDashboardReport")
	public String getSnaDashboardReport(@RequestBody DashboardBean requestBean) {
		JSONObject claimCount = null;
		try {
			claimCount = dashService.getSnaDashboardReport(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount.toString();
	}

	@ResponseBody
	@PostMapping(value = "/getHospitalDashboardReport")
	public String getHospitalDashboardReport(@RequestBody DashboardBean requestBean) {
		JSONObject claimCount = null;
		try {
			claimCount = dashService.getHospitalDashboardReport(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount.toString();
	}

	@ResponseBody
	@PostMapping(value = "/getCpdDashboardReport")
	public String getCpdDashboardReport(@RequestBody DashboardBean requestBean) {
		JSONObject claimCount = null;
		try {
			claimCount = dashService.getCpdDashboardReport(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount.toString();
	}

	@ResponseBody
	@GetMapping(value = "/getclaimActionCountDetails")
	public List<Object> getclaimActionCountDetails(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "fromDate", required = false) Date fromdate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "eventName", required = false) String eventName,
			@RequestParam(value = "stateId", required = false) String stateId,
			@RequestParam(value = "districtId", required = false) String districtId,
			@RequestParam(value = "hospitalId", required = false) String hospitalId) {
		List<Object> getclaimCountDetails = null;
		try {
			if (stateId.equalsIgnoreCase("null") || stateId.equalsIgnoreCase("undefined")) {
				stateId = null;
			}
			if (districtId.equalsIgnoreCase("null") || districtId.equalsIgnoreCase("undefined")) {
				districtId = null;
			}
			if (hospitalId.equalsIgnoreCase("null") || hospitalId.equalsIgnoreCase("undefined")) {
				hospitalId = null;
			}
			getclaimCountDetails = claimcount.getActionCountDetails(userId, fromdate, toDate, eventName, stateId,
					districtId, hospitalId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getclaimCountDetails;
	}

	@PostMapping(value = "/getUnProcessedClaim")
	@ResponseBody
	public ResponseEntity<?> getSNAClaimApprove(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		List<Object> snoclaimList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			snoclaimList = claimcount.getUnprocessedClaim(requestBean);
			details.put("status", "success");
			details.put("data", snoclaimList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);

	}

	@GetMapping(value = "/getUnProcessedClaimDetailsById")
	@ResponseBody
	public Map<String, String> SnoClaimById(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = claimcount.getUnprocessedClaimDetailsById(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@PostMapping(value = "/snoUnprocessedAction")
	public ResponseEntity<Response> saveClaimRaiseHospita(@RequestBody ClaimLogBean logBean) {
		Response response = null;
		try {
			response = claimcount.saveUnprocessedClaimSNADetails(logBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@GetMapping(value = "/getclaimRecievedReportCount")
	public Map<String, String> getclaimRecievedReportCount(
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "month", required = false) String month,
			@RequestParam(value = "year", required = false) String year) throws Exception {
		String getclaimCount = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			getclaimCount = claimcount.getclaimRecievedReportCount(userId, year, month);
			details.put("status", "success");
			details.put("details", getclaimCount);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@ResponseBody
	@GetMapping(value = "/getTransactionCountDetails")
	public String getTransactionCountDetails(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "years", required = false) String years,
			@RequestParam(value = "months", required = false) String months,
			@RequestParam(value = "days", required = false) String days,
			@RequestParam(value = "eventName", required = false) String eventName) throws Exception {
		String getclaimCountDetails = null;
		try {
			getclaimCountDetails = claimcount.getTransactionsCountDetails(userId, years, months, days, eventName);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getclaimCountDetails;

	}

	@ResponseBody
	@GetMapping(value = "/getClaimRecievedCountDetails")
	public String getClaimRecievedCountDetails(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "years", required = false) String years,
			@RequestParam(value = "months", required = false) String months,
			@RequestParam(value = "days", required = false) String days,
			@RequestParam(value = "eventName", required = false) String eventName) throws Exception {
		String getclaimCountDetails = null;
		try {
			getclaimCountDetails = claimcount.getRecievedCountDetails(userId, years, months, days, eventName);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getclaimCountDetails;

	}

	@ResponseBody
	@GetMapping(value = "/getclaimcountprogressreportdetails")
	public List<Object> getclaimcountprogressreportdetails(
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "fromDate", required = false) Date fromdate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "eventName", required = false) String eventName,
			@RequestParam(value = "stateId", required = false) String stateId,
			@RequestParam(value = "districtId", required = false) String districtId,
			@RequestParam(value = "hospitalId", required = false) String hospitalId,
			@RequestParam(value = "groupid", required = false) String groupid) {

		List<Object> getclaimCountDetails = null;
		try {
			if (stateId.equalsIgnoreCase("null") || stateId.equalsIgnoreCase("undefined")) {
				stateId = null;
			}
			if (districtId.equalsIgnoreCase("null") || districtId.equalsIgnoreCase("undefined")) {
				districtId = null;
			}
			if (hospitalId.equalsIgnoreCase("null") || hospitalId.equalsIgnoreCase("undefined")) {
				hospitalId = null;
			}
			getclaimCountDetails = claimcount.getclaimcountprogressreportdetails(userId, fromdate, toDate, eventName,
					stateId, districtId, hospitalId, groupid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getclaimCountDetails;
	}

	@ResponseBody
	@GetMapping(value = "/getclaimbyUrnAndClaimNo")
	public Map<String, String> getclaimbyUrnAndClaimNo(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "searchBy", required = false) String searchBy,
			@RequestParam(value = "fieldValue", required = false) String fieldValue) throws Exception {
		String getclaimList = null;
		if (fieldValue != null && fieldValue != "") {
			fieldValue = fieldValue.trim();
		}
		Map<String, String> details = new HashMap<String, String>();
		try {
			getclaimList = claimcount.getclaimByUrnAndClamiNo(userId, searchBy, fieldValue);
			details.put("status", "success");
			details.put("details", getclaimList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@ResponseBody
	@PostMapping(value = "/getSnaWisePaymentStatus")
	public String DCCCECountReport(@RequestBody SnaPaymentStatusBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = claimcount.getSNAWisePaymentStatusReport(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;

	}

	/*
	 * @Auther: Sambit Kumar Pradhan
	 * 
	 * @Date: 25-OCT-2023
	 * 
	 * @Description: This method is used to get the hospital wise treatment report.
	 */
	@PostMapping(value = "hospitalWiseTreatment")
	public ResponseEntity<Map<String, Object>> hospitalWiseTreatment(@RequestBody Map<String, Object> request)
			throws SQLException {
		Map<String, Object> response;
		try {
			List<Map<String, Object>> responseList = claimcount.hospitalWiseTreatment(request);
			if (!responseList.isEmpty())
				response = CommonClassHelper.createSuccessResponse(responseList, "List Fetched Successfully.");
			else
				response = CommonClassHelper.createNoContentResponse("No Data Found!");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return ResponseEntity.ok(CommonClassHelper.createErrorResponse(e.getMessage()));
		}
	}
}
