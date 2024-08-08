/**
 * 
 */
package com.project.bsky.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.exception.CPDClaimProcessingException;
import com.project.bsky.service.CPDClaimReapprovalService;

/**
 * @author Debendra Nayak
 * @author : model Kumar Pradhan
 *
 */
@RestController
@RequestMapping(value = "/api")
@PropertySource("classpath:error-message.properties")
public class CPDClaimReapprovelController {

	private final Logger logger;

	@Autowired
	public CPDClaimReapprovelController(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private CPDClaimReapprovalService cpdClaimProcess;

	@Value("${cpdclaim.fetchrecord.error.message}")
	private String fetchMessage;

	/**
	 * @author Debendra Nayak
	 * @date 26-08-2022 desc- This method is used to get the list of claim raised by
	 *       hospital
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/getCPDReapprovalClaimList")
	public String getCPDReapprovalClaimList(@RequestParam("userId") String userId,
			@RequestParam(value = "orderValue", required = false) String orderValue,
			@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "authMode", required = false) Integer authMode,
			@RequestParam(value = "trigger", required = false) Integer trigger,
			@RequestParam(value = "schemeid", required = false) Integer schemeid,
			@RequestParam(value = "schemecategoryid", required = false) String schemecategoryid) {
		String claimList = null;
		String fromDate1 = null;
		String toDate1 = null;
		if (orderValue == null)
			orderValue = "URN";
		if (fromDate != null && toDate != null) {
			fromDate1 = new SimpleDateFormat("dd-MM-yy").format(fromDate);
			toDate1 = new SimpleDateFormat("dd-MM-yy").format(toDate);
		} else {
			fromDate1 = null;
			toDate1 = null;
		}
		try {
			claimList = cpdClaimProcess.getAllClaimRaised(Integer.parseInt(userId), orderValue, fromDate1, toDate1,
					authMode, trigger, schemeid, schemecategoryid);
		} catch (Exception e) {
			logger.error("Exception occured in getCPDReapprovalClaimList method of CPDClaimReapprovelController"
					+ e.getMessage());
		}
		return claimList;
	}

	/**
	 * @author Debendra Nayak
	 * @date 27-08-2022
	 * @param transaction_id
	 * @param urn
	 * @param claimId
	 * @return
	 * @throws SQLException
	 */
	@GetMapping(value = "/getReapprovalClaimDetails")
	public String getReapprovalClaimDetails(@RequestParam("transaction_id") String transaction_id,
			@RequestParam("urn") String urn, @RequestParam("claimId") String claimId,
			@RequestParam("authorizedCode") String authorizedCode, @RequestParam("hospitalCode") String hospitalCode,
			@RequestParam("actualDate") String actualDate, @RequestParam(value = "caseNo") String caseNo,
			@RequestParam(value = "userId") Long userId, @RequestParam(value = "claimNo") String claimNo)
			throws SQLException {
		String mod = cpdClaimProcess.getClaimDetails(transaction_id, urn, claimId, authorizedCode, hospitalCode,
				actualDate, caseNo, userId, new Date(), claimNo);
		if (mod == null) {
			throw new CPDClaimProcessingException(String.format(fetchMessage, urn), new Throwable("DNF"));
		}
		return mod;
	}

	@ResponseBody
	@PostMapping(value = "/saveCpdReapprovalClaimRequest")
	public ResponseEntity<Map<String, Object>> saveCpdReapprovalClaimRequest(@RequestBody ClaimLogBean requestBean) {
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> result;
		try {
			Integer claimId = (int) (long) requestBean.getClaimId();
			result = cpdClaimProcess.saveCpdClaimAction(claimId, requestBean.getActionRemark(),
					String.valueOf(requestBean.getUserId()), requestBean.getRemarks(),
					String.valueOf(requestBean.getActionRemarksId()), requestBean.getAmount(), requestBean.getUrnNo(),
					requestBean.getMortality(), requestBean.getTimingLogId(), new Date(), requestBean.getIcdFlag(),
					requestBean.getIcdFinalData());
			if (result != null && (result.get("responseId").equals("1") || result.get("responseId").equals("2")
					|| result.get("responseId").equals("3"))) {
				data.put("status", "success");
				data.put("msg", result.get("response"));
			} else {
				data.put("status", "info");
				data.put("msg", result.get("response"));
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in saveCpdClaimActionRequest method of CPDProcessingController : "
					+ e.getMessage());
			data.put("status", "fail");
			data.put("msg", e.getMessage());
		}
		return ResponseEntity.ok().body(data);
	}

	/**
	 * @Author : Sambit Kumar Pradhan
	 * @Date : 01-02-2023
	 * @Description : For Fetching CPD Re-Approval List Count
	 */
	@GetMapping(value = "/getCPDReApprovalClaimListCount")
	public ResponseEntity<?> getCPDReApprovalClaimListCount(@RequestParam("userId") String userId,
			@RequestParam(value = "orderValue", required = false) String orderValue,
			@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "authMode", required = false) Integer authMode,
			@RequestParam(value = "trigger", required = false) Integer trigger,
			@RequestParam(value = "schemeid", required = false) Integer schemeid,
			@RequestParam(value = "schemecategoryid", required = false) String schemecategoryid) {
		String fromDate1 = null;
		String toDate1 = null;
		Map<String, Object> response = new HashMap<>();
		try {
			if (orderValue == null || orderValue.equals("0"))
				orderValue = "URN";
			if (fromDate != null || toDate != null) {
				fromDate1 = new SimpleDateFormat("dd-MM-yy").format(fromDate);
				toDate1 = new SimpleDateFormat("dd-MM-yy").format(toDate);
			}
			response.put("status", "success");
			response.put("data", cpdClaimProcess.getCPDReApprovalClaimListCount(userId, orderValue, fromDate1, toDate1,
					authMode, trigger, schemeid, schemecategoryid));
		} catch (Exception ex) {
			logger.error("Exception Occurred in getCPDApprovalListCount method of CPDProcessingController : "
					+ ex.getMessage());
			response.put("status", "fail");
			response.put("msg", ex.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
