/**
 * 
 */
package com.project.bsky.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.BtnVisibilityBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.SNARequestApproval;

/**
 * @author arabinda.guin
 *
 */
@CrossOrigin
@Controller
@RequestMapping(value = "/api")
public class SNARequestApproveController {
	@Autowired
	private SNARequestApproval snarequest;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getRequestApproval")
	public List<Object> SnoRequestApproval(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "fromDate", required = false) Date fromdate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "stateCode", required = false) String stateCode,
			@RequestParam(value = "distCode", required = false) String distCode,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "schemeid", required = false) String schemeid,
			@RequestParam(value = "schemecategoryid", required = false) String schemecategoryid) throws Exception {
		List<Object> requestList = null;
		try {
			requestList = snarequest.getRequestList(userId, fromdate, toDate, stateCode, distCode, hospitalCode,
					schemeid, schemecategoryid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return requestList;

	}

	@GetMapping(value = "/getRequestById")
	@ResponseBody
	public Map<String, String> RequestById(@RequestParam("txnId") Integer txnId) {
		String requestList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			requestList = snarequest.getRequestByDetailId(txnId);
			details.put("status", "success");
			details.put("details", requestList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@ResponseBody
	@PostMapping(value = "/snaSystemRejectedAction", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Response> UpdateReject(
			@RequestParam(value = "rejectionId", required = false) Long rejectionId,
			@RequestParam(value = "transactionDetailsId", required = false) Long transactionDetailsId,
			@RequestParam(value = "statusflag", required = false) Integer statusflag,
			@RequestParam(value = "claimBy", required = false) String claimBy,
			@RequestParam(value = "snaRemark", required = false) String snaRemark,
			@RequestParam(value = "sysRejStatus", required = false) Integer sysRejStatus,
			@RequestParam(value = "ApproveDoc", required = false) MultipartFile ApproveDoc,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "dateOfAdm", required = false) String dateOfAdm,
			@RequestParam(value = "urn", required = false) String urn) {
		Response response = null;
		try {
			response = snarequest.saveRejectDetails(rejectionId, transactionDetailsId, statusflag, claimBy, snaRemark,
					sysRejStatus, ApproveDoc, hospitalCode, dateOfAdm, urn);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@GetMapping(value = "/getNonComplianceRequestApproval")
	public List<Object> SnoRequestApproval(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "fromDate", required = false) Date fromdate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "stateCode", required = false) String stateCode,
			@RequestParam(value = "distCode", required = false) String distCode,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "schemeid", required = false) String schemeid,
			@RequestParam(value = "schemecategoryid", required = false) String schemecategoryid) throws Exception {

		List<Object> requestList = null;
		try {
			requestList = snarequest.getNonComplianceRequestList(userId, fromdate, toDate, stateCode, distCode,
					hospitalCode, flag,schemeid,schemecategoryid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return requestList;

	}

	@GetMapping(value = "/getNonComplianceRequestById")
	@ResponseBody
	public Map<String, String> NonComplianceRequestById(@RequestParam("txnId") Integer txnId) {
		String requestList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			requestList = snarequest.getNonComplianceRequestByDetailId(txnId);
			details.put("status", "success");
			details.put("details", requestList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@ResponseBody
	@PostMapping(value = "/snaNonComplianceRequestAction", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Response> UpdateNonCompliance(
			@RequestParam(value = "rejectionId", required = false) Long rejectionId,
			@RequestParam(value = "transactionDetailsId", required = false) Long transactionDetailsId,
			@RequestParam(value = "statusflag", required = false) Integer statusflag,
			@RequestParam(value = "claimBy", required = false) String claimBy,
			@RequestParam(value = "snaRemark", required = false) String snaRemark,
			@RequestParam(value = "sysRejStatus", required = false) Integer sysRejStatus,
			@RequestParam(value = "ApproveDoc", required = false) MultipartFile ApproveDoc,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "dateOfAdm", required = false) String dateOfAdm,
			@RequestParam(value = "urn", required = false) String urn) {
		Response response = null;
		try {
			response = snarequest.saveNonComplianceDetails(rejectionId, transactionDetailsId, statusflag, claimBy,
					snaRemark, sysRejStatus, ApproveDoc, hospitalCode, dateOfAdm, urn);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@PostMapping(value = "/getSysRejListSna")
	public List<Object> SysRejBtnEnable(@RequestBody BtnVisibilityBean requestBean) throws Exception {
		List<Object> requestList = null;
		try {
			requestList = snarequest.getNonUploadingListToSna(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return requestList;

	}

	@ResponseBody
	@PostMapping(value = "/snaNonCompliancePermission")
	public ResponseEntity<Response> SnaNonCompliancePermission(
			@RequestParam(value = "snaUserId", required = false) Long snaUserId,
			@RequestParam(value = "claimBy", required = false) String claimBy,
			@RequestParam(value = "hosUserId", required = false) Long hosUserId,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "nonUploadingStatus", required = false) Integer nonUploadingStatus,
			@RequestParam(value = "nonComplianceStatus", required = false) Integer nonComplianceStatus) {
		Response response = null;
		try {
			response = snarequest.savePermissionDetails(snaUserId, hosUserId, hospitalCode, claimBy, nonUploadingStatus,
					nonComplianceStatus);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

}
