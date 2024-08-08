/**
 * 
 */
package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.OldFloatBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.OldClaimProcessingService;

@Controller
@RequestMapping(value = "/api")
public class OldClaimProcessingController {

	@Autowired
	private OldClaimProcessingService oldClaimProcessing;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/getOldClaimQueryBySNAlist")
	public List<Object> getOldProcessedClaimlist(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		List<Object> SnoclaimList = null;
		try {
			SnoclaimList = oldClaimProcessing.getOldClaimQueryBySNAList(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return SnoclaimList;
	}

	@ResponseBody
	@RequestMapping(value = "/getOldQueriedClaimDetails", method = RequestMethod.GET)
	public String getQueriedClaimDetails(@RequestParam(value = "claimID") String claimID) {
		String SnoClaim = null;
		try {
			SnoClaim = oldClaimProcessing.getOldQueriedClaimDetails(claimID);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return SnoClaim;
	}

	@ResponseBody
	@PostMapping(value = "/takeActionOnOldClaimQuery", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Response> saveQuerysnohospital(
			@RequestParam(value = "claimID", required = false) String claimID,
			@RequestParam(value = "userName", required = false) String hospitalCode,
			@RequestParam(value = "Additionaldoc1", required = false) MultipartFile Additionaldoc1,
			@RequestParam(value = "Additionaldoc2", required = false) MultipartFile Additionaldoc2,
			@RequestParam(value = "UrnNumber", required = false) String UrnNumber,
			@RequestParam(value = "dateofAdmission", required = false) String dateofAdmission,
			@RequestParam(value = "transId", required = false) String transId,
			@RequestParam(value = "ClaimAmount", required = false) String ClaimAmount,
			@RequestParam(value = "actionby", required = false) String actionby) {
		Response response = new Response();
		try {
			response = oldClaimProcessing.takeActionOnOldClaimforsnoQuery(claimID, hospitalCode, Additionaldoc1,
					Additionaldoc2, UrnNumber, dateofAdmission, transId, ClaimAmount, actionby);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage(e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@PostMapping(value = "/getOldClaimResettlementlist")
	public List<Object> getOldClaimResettlelist(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		List<Object> SnoclaimList = null;
		try {
			SnoclaimList = oldClaimProcessing.getOldClaimReApproveList(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return SnoclaimList;

	}

	@GetMapping(value = "/getOldClaimResettlementById")
	@ResponseBody
	public Map<String, String> getOldClaimResettlementById(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = oldClaimProcessing.getOldClaimReAprvById(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@PostMapping(value = "/snoactionofOldReClaim")
	public ResponseEntity<Response> snoactionofOldReClaim(@RequestBody ClaimLogBean logBean) {
		Response response = null;
		try {
			response = oldClaimProcessing.saveClaimSNOOfOldReAprvDetails(logBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/olddoc")
	public void commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		String hCode = json.getString("h");
		String year = json.getString("d");
		try {
			oldClaimProcessing.downLoadOldFile(fileName, year, hCode, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@ResponseBody
	@PostMapping(value = "/getOldClaimActionlist")
	public Map<String, String> getOldClaimActionlist(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = oldClaimProcessing.getOldActionList(requestBean);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error("Exception Occurred in getOldClaimActionList Method of OldClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@GetMapping(value = "/getOldClaimTrackingById")
	@ResponseBody
	public Map<String, String> getOldClaimTrackingById(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = oldClaimProcessing.getOldClaimTrackingDetailsById(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error("Exception Occurred in getOldClaimActionList Method of OldClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@ResponseBody
	@PostMapping(value = "/getSNAProcessedOldClaimlist")
	public Map<String, String> getOldClaimSNAProcessedlist(@RequestBody CPDApproveRequestBean requestBean)
			throws Exception {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = oldClaimProcessing.getOldSNAProcessedList(requestBean);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error("Exception Occurred in getOldClaimActionList Method of OldClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@ResponseBody
	@PostMapping(value = "/getReClaimedAndPendingAtSNAlist")
	public Map<String, String> getReClaimedAndPendingAtSNAlist(@RequestBody CPDApproveRequestBean requestBean)
			throws Exception {
		String snoClaim = null;
		Map<String, String> details = new HashMap<>();
		try {
			snoClaim = oldClaimProcessing.getOldReclaimedPendingAtSNAList(requestBean);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error("Exception Occurred in getOldClaimActionList Method of OldClaimProcessingController : "+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}
	@ResponseBody
	@PostMapping(value = "/getOldClaimCountReport")
	public String getOldClaimCountReport(@RequestBody OldFloatBean requestBean) {
		String claimCount = null;
		try {
			claimCount = oldClaimProcessing.getOldClaimCountProgress(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;
	}
	@ResponseBody
	@GetMapping(value = "/getoldclaimprogressreportdetails")
	public String getoldclaimprogressreportdetails(
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "fromDate", required = false) Date fromdate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "eventName", required = false) String eventName,
			@RequestParam(value = "stateId", required = false) String stateId,
			@RequestParam(value = "districtId", required = false) String districtId,
			@RequestParam(value = "hospitalId", required = false) String hospitalId) {

		String getclaimCountDetails = null;
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
			getclaimCountDetails = oldClaimProcessing.getOldclaimprogressreportdetails(userId, fromdate, toDate, eventName,stateId, districtId, hospitalId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getclaimCountDetails;
	}
	
	@ResponseBody
	@GetMapping(value = "/oldclaimnoncompliance")
	public Map<String,Object> getOldClaimCountReport(@RequestParam(value = "formdate", required = false) Date fromdate,
			@RequestParam(value = "todate", required = false) Date toDate,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "hospital", required = false) String hospital) {
		Map<String,Object> claimCount = new HashedMap<>();
		try {
			claimCount.put("data", oldClaimProcessing.oldclaimnoncompliance(fromdate,toDate,userId,state,dist,hospital));
			claimCount.put("status", HttpStatus.OK.value());
			claimCount.put("msg", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			claimCount.put("status", HttpStatus.BAD_REQUEST.value());
			claimCount.put("msg", "Error");
			claimCount.put("error", e.getMessage());
		}
		return claimCount;
	}
}
