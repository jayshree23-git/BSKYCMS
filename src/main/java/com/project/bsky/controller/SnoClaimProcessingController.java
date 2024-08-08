package com.project.bsky.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.bsky.bean.Bulkapprovalbean;
import com.project.bsky.bean.Bulkrevertbean;
import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.MeTriggerDetailsBean;
import com.project.bsky.bean.PatientRequestBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.ActionRemark;
import com.project.bsky.model.ActionType;
import com.project.bsky.model.EnrollmentRemarks;
import com.project.bsky.model.Mstschemesubcategory;
import com.project.bsky.model.State;
import com.project.bsky.service.SnoClaimProcessingDetails;

@Controller
@RequestMapping(value = "/api")
public class SnoClaimProcessingController {

	@Autowired
	private Logger logger;

	@Autowired
	private SnoClaimProcessingDetails snoClaimProcessing;

	@ResponseBody
	@PostMapping(value = "/getsnoclaimreaprrovelist")
	public List<Object> SnoClaimReApproveList(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		List<Object> SnoclaimList = null;
		try {
			SnoclaimList = snoClaimProcessing.getsnoclaimrasiedata(requestBean);
		} catch (Exception e) {
			logger.error("Exception Occurred in SnoClaimReApproveList Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return SnoclaimList;

	}

	@PostMapping(value = "/getsnoclaimapprovelist")
	@ResponseBody
	public ResponseEntity<?> getSNAClaimApprove(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			Map<String, Object> snoclaimMap = snoClaimProcessing.getSNAClaimApprove(requestBean);
			details.put("status", "success");
			details.put("size", snoclaimMap.get("size"));
			details.put("data", snoclaimMap.get("list"));
		} catch (Exception e) {
			logger.error("Exception Occurred in getSNAClaimApprove Method of SnoClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/cpdapprovalcount")
	@ResponseBody
	public ResponseEntity<?> cpdApprovalCount(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			Map<String, Object> snoclaimMap = snoClaimProcessing.cpdApprovalCount(requestBean);

			details.put("status", "success");
			details.put("count", snoclaimMap.get("count"));
		} catch (Exception e) {
			logger.error("Exception Occurred in getSNAClaimApprove Method of SnoClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/countreportbycpdaprv")
	@ResponseBody
	public ResponseEntity<?> getCountReportBtCPDAprv(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		String countReport = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			countReport = snoClaimProcessing.getCountReportBtCPDAprv(requestBean);
			details.put("status", "success");
			details.put("data", countReport);
		} catch (Exception e) {
			logger.error("Exception Occurred in getCountReportBtCPDAprv Method of SnoClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);

	}

	@GetMapping(value = "/snoapproval")
	@ResponseBody
	public Map<String, String> SnoClaimById(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = snoClaimProcessing.getSnoClaimListById(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in SnoClaimById Method of SnoClaimProcessingController : " + e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@GetMapping(value = "/claimprocesseddetails")
	@ResponseBody
	public Map<String, String> claimProcessedDetails(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = snoClaimProcessing.claimProcessedDetails(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in SnoClaimById Method of SnoClaimProcessingController : " + e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@GetMapping(value = "/urnwisedetails")
	@ResponseBody
	public Map<String, String> getURNWiseDetails(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = snoClaimProcessing.getURNWiseDetails(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in SnoClaimById Method of SnoClaimProcessingController : " + e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@GetMapping(value = "/systemadminrejecteddetails")
	@ResponseBody
	public Map<String, String> getSystemAdminRejectedDetails(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = snoClaimProcessing.getSystemAdminRejectedDetails(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in SnoClaimById Method of SnoClaimProcessingController : " + e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@GetMapping(value = "/dischargeTreatment")
	@ResponseBody
	public Map<String, String> dischargeTreatment(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = snoClaimProcessing.dischargeTreatment(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error("Exception Occurred in dischargeTreatment Method of SnoClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@GetMapping(value = "/multiPackageBlock")
	@ResponseBody
	public Map<String, String> getMultiPackageBlock(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = snoClaimProcessing.getMultiPackageBlock(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error("Exception Occurred in getMultiPackageBlock Method of SnoClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@GetMapping(value = "/downLoadAction")
	public void commonDownloadMethod(HttpServletResponse response, @RequestParam("fileName") String fileName,
			@RequestParam("hCode") String hCode, @RequestParam("dateOfAdm") String dateOfAdm) {
		try {
			String year = dateOfAdm.substring(6);
			snoClaimProcessing.downLoadFile(fileName, year, hCode, response);
		} catch (Exception e) {
			logger.error("Exception Occurred in commonDownloadMethod Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
	}

	@GetMapping(value = "/doc")
	public void commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		String year = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		String hCode = json.getString("h");
		String dateOfAdm = json.getString("d");
		try {
			if (dateOfAdm.length() > 11) {
				String preAuthDate = new SimpleDateFormat("dd MMM yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(dateOfAdm));
				year = preAuthDate.substring(6);
			} else {
				year = dateOfAdm.substring(6);
			}
			snoClaimProcessing.downLoadFile(fileName, year, hCode, response);
		} catch (Exception e) {
			logger.error("Exception Occurred in commonDownloadMethod Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
	}

	@GetMapping(value = "/multipledoc")
	public void commonMultipleDownloadMethod(HttpServletResponse response,
			@RequestParam("data") String enCodedJsonString) throws JSONException, IOException {
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONArray jsonArray = new JSONArray(jsonString);
		snoClaimProcessing.filteredFile(jsonArray, response);
	}

	@PostMapping(value = "/snoaction")
	public ResponseEntity<Response> saveClaimRaiseHospita(@RequestBody ClaimLogBean logBean) {
		Response response = null;
		try {
			response = snoClaimProcessing.saveClaimSNODetails(logBean);
		} catch (Exception e) {
			logger.error("Exception Occurred in saveClaimRaiseHospita Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/snoreaction")
	public ResponseEntity<Response> saveClaimReApprovalAction(@RequestBody ClaimLogBean logBean) {
		Response response = null;
		try {
			response = snoClaimProcessing.saveClaimReApprovalAction(logBean);
		} catch (Exception e) {
			logger.error("Exception Occurred in saveClaimRaiseHospita Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/saveclaimprocesseddetails")
	public ResponseEntity<Response> saveClaimProcessedDetails(@RequestBody ClaimLogBean logBean) {
		Response response = null;
		try {
			response = snoClaimProcessing.saveClaimProcessedDetails(logBean);
		} catch (Exception e) {
			logger.error("Exception Occurred in saveClaimRaiseHospita Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/saveurnwisedetails")
	public ResponseEntity<Response> saveUrnWiseDetails(@RequestBody ClaimLogBean logBean) {
		Response response = null;
		try {
			response = snoClaimProcessing.saveUrnWiseDetails(logBean);
		} catch (Exception e) {
			logger.error("Exception Occurred in saveClaimRaiseHospita Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/saveSyatemAdminSnaRejectedDetails")
	public ResponseEntity<Response> saveSyatemAdminSnaRejectedDetails(@RequestBody ClaimLogBean logBean) {
		Response response = null;
		try {
			response = snoClaimProcessing.saveSyatemAdminSnaRejectedDetails(logBean);
		} catch (Exception e) {
			logger.error("Exception Occurred in saveClaimRaiseHospita Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/getAllRemarks")
	@ResponseBody
	public List<ActionRemark> GetRemarks() {
		List<ActionRemark> allActionRemark = snoClaimProcessing.getAllActionRemark();
		return allActionRemark;
	}

	// this remark section for enrollment section
	@GetMapping("/getEnrollmentAllRemarks")
	@ResponseBody
	public List<EnrollmentRemarks> GetRemarksenrollment() {
		List<EnrollmentRemarks> enrollmentallActionRemark = snoClaimProcessing.getEnrollmentAllActionRemark();
		return enrollmentallActionRemark;
	}

	@GetMapping("/getRemarksById")
	@ResponseBody
	public ActionRemark GetRemarksById(@RequestParam(value = "remarkId") Long remarkId) {
		ActionRemark allActionRemark = snoClaimProcessing.getActionRemarkById(remarkId);
		return allActionRemark;
	}

	@ResponseBody
	@GetMapping(value = "/getPreAuthData")
	public String PreAuthData(@RequestParam("urn") String urn) {
		String preAuth = null;
		try {
			preAuth = snoClaimProcessing.getPreAuthData(urn);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in PreAuthData Method of SnoClaimProcessingController : " + e.getMessage());
		}
		return preAuth;
	}

	@GetMapping("/getState")
	@ResponseBody
	public List<State> getState() {
		List<State> statedetails = snoClaimProcessing.getAllState();
		return statedetails;
	}

	@ResponseBody
	@GetMapping(value = "/getDistrictByStateSno")
	public String getDistrict(@RequestParam("stateCode") String stateCode, @RequestParam("userId") Long userId) {
		String districtList = null;
		try {
			districtList = snoClaimProcessing.getDistrictList(stateCode, userId);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getDistrict Method of SnoClaimProcessingController : " + e.getMessage());
		}
		return districtList;
	}

	@ResponseBody
	@GetMapping(value = "/getHospitalListBySno")
	public String getHospital(@RequestParam("stateCode") String stateCode, @RequestParam("distCode") String distCode,
			@RequestParam("userId") Long userId) {
		String hospitalList = null;
		try {
			hospitalList = snoClaimProcessing.getHospital(stateCode, distCode, userId);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getHospital Method of SnoClaimProcessingController : " + e.getMessage());
		}
		return hospitalList;
	}

	@GetMapping(value = "/getActionType")
	@ResponseBody
	public List<ActionType> getActionType() {
		List<ActionType> actionType = null;
		actionType = snoClaimProcessing.getActionType();
		return actionType;
	}

	@PostMapping(value = "/bulksearccountdetails")
	@ResponseBody
	public String getBulkcount(@RequestBody Bulkapprovalbean requestBean) throws Exception {
		JSONObject claimCount = null;
		try {
			claimCount = snoClaimProcessing.getcountdetails(requestBean);

		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getBulkcount Method of SnoClaimProcessingController : " + e.getMessage());
		}
		return claimCount.toString();
	}

	@GetMapping(value = "/savebulkapprovedforsna")
	@ResponseBody
	public ResponseEntity<Response> getsavebulkapproved(@RequestParam(value = "user", required = false) Long user,
			@RequestParam(value = "group", required = false) Long group,
			@RequestParam(value = "flags", required = false) String flags,
			@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "stateid", required = false) String stateid,
			@RequestParam(value = "districtid", required = false) String districtid,
			@RequestParam(value = "hospitalid", required = false) String hospitalid) {
		Response response = new Response();
		try {
			response = snoClaimProcessing.getsavebulkapproveds(user, group, flags, fromDate, toDate, stateid,
					districtid, hospitalid);
		} catch (Exception e) {
			logger.error("Exception Occurred in getsavebulkapproved Method of SnoClaimProcessingController : "
					+ e.getMessage());
			response.setStatus("Failed");
			response.setMessage(e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	/**
	 * @Author : Sambit Kumar Pradhan
	 * @Date : 17-01-2023
	 * @Description : For Downloading All Documents in a Merged PDF File
	 */
	@GetMapping(value = "/downloadAllDocuments")
	public void downloadAllDocuments(HttpServletResponse httpServletResponse,
			@RequestParam("documentData") String enCodedJsonString) throws JSONException, IOException {
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONArray jsonArray = new JSONArray(jsonString);
		snoClaimProcessing.downloadDocuments(jsonArray, httpServletResponse);
	}

	@ResponseBody
	@PostMapping(value = "/getdcClaimApprovedlist")
	public List<Object> getdcClaimApprovedlist(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		List<Object> SnoclaimList = null;
		try {
			SnoclaimList = snoClaimProcessing.getdcClaimApproveddata(requestBean);
		} catch (Exception e) {
			logger.error("Exception Occurred in getdcClaimApprovedlist Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return SnoclaimList;

	}

	@GetMapping(value = "/getDcAprvListById")
	@ResponseBody
	public Map<String, String> getDcAprvListById(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = snoClaimProcessing.getDCClaimAprvById(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error("Exception Occurred in getDcAprvListById Method of SnoClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@PostMapping(value = "/snoactionofDCApproved")
	public ResponseEntity<Response> snoactionofDCApproved(@RequestBody ClaimLogBean logBean) {
		Response response = null;
		try {
			response = snoClaimProcessing.saveClaimSNOOfDCAprvDetails(logBean);
		} catch (Exception e) {
			logger.error("Exception Occurred in snoactionofDCApproved Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@PostMapping(value = "/getOldProcessedClaimlist")
	public List<Object> getOldProcessedClaimlist(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		List<Object> SnoclaimList = null;
		try {
			SnoclaimList = snoClaimProcessing.getOldClaimApprovedList(requestBean);
		} catch (Exception e) {
			logger.error("Exception Occurred in getOldProcessedClaimlist Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return SnoclaimList;
	}

	@GetMapping(value = "/getOldProcessedClaimById")
	@ResponseBody
	public Map<String, String> getOldProcessedClaimById(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = snoClaimProcessing.getOldClaimById(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error("Exception Occurred in getOldProcessedClaimById Method of SnoClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@PostMapping(value = "/SaveOldProcessedClaim")
	public ResponseEntity<Response> SaveOldProcessedClaim(@RequestBody ClaimLogBean logBean) {
		Response response = null;
		try {
			response = snoClaimProcessing.saveoldClaimDetails(logBean);
		} catch (Exception e) {
			logger.error("Exception Occurred in SaveOldProcessedClaim Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/getClaimsOnHoldList")
	@ResponseBody
	public ResponseEntity<?> getClaimsOnHoldList(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			List<Object> snoclaimList = snoClaimProcessing.getClaimsOnHoldList(requestBean);
			details.put("status", "success");
			details.put("data", snoclaimList);
		} catch (Exception e) {
			logger.error("Exception Occurred in getClaimsOnHoldList Method of SnoClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/getTreatmentHistoryoverpackgae")
	@ResponseBody
	public String getTreatmentHistoryoverpackgae(@RequestParam(required = false, value = "txnId") Long txnId,
			@RequestParam(required = false, value = "urnnumber") String urnnumber,
			@RequestParam(required = false, value = "hospitalcode") String hospitalcode,
			@RequestParam(required = false, value = "caseno") String caseno,
			@RequestParam(required = false, value = "uidreferencenumber") String uidreferencenumber,
			@RequestParam(required = false, value = "userid") Long userid) {
		String treatmetlist = null;
		try {
			treatmetlist = snoClaimProcessing.getTreatmentHistoryoverpackgae(txnId, urnnumber, hospitalcode, caseno,
					uidreferencenumber, userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return treatmetlist;
	}

	@GetMapping(value = "/getremarkdetails")
	@ResponseBody
	public String getremarkdetails(@RequestParam(required = false, value = "snaid") Long snaid,
			@RequestParam(required = false, value = "fromdate") Date fromdate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "hospitalcode") String hospitalcode,
			@RequestParam(required = false, value = "stateode") String stateode,
			@RequestParam(required = false, value = "distcode") String distcode) {
		String rejectionlistdetails = null;
		try {
			rejectionlistdetails = snoClaimProcessing.getremarkdetailsforsna(snaid, fromdate, todate, hospitalcode,
					stateode, distcode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return rejectionlistdetails;
	}

	@GetMapping(value = "/getcountremarkdetails")
	@ResponseBody
	public String getcountremarkdetails(@RequestParam(required = false, value = "userid") Long userid,
			@RequestParam(required = false, value = "fromdate") Date fromdate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "statecode") String statecode,
			@RequestParam(required = false, value = "districtcode") String districtcode,
			@RequestParam(required = false, value = "hospitalcode") String hospitalcode,
			@RequestParam(required = false, value = "remarkid") Long remarkid,
			@RequestParam(required = false, value = "hospitalcodeforremark") String hospitalcodeforremark) {
		String countdetails = null;
		try {
			countdetails = snoClaimProcessing.getcountremarkdetailsforsna(userid, fromdate, todate, statecode,
					districtcode, hospitalcode, remarkid, hospitalcodeforremark);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return countdetails;
	}

	@GetMapping(value = "/getdetailsonfloatclaimdetails")
	@ResponseBody
	public String getcountremarkdetails(@RequestParam(required = false, value = "urn") String urn,
			@RequestParam(required = false, value = "claimid") Long claimid,
			@RequestParam(required = false, value = "floatno") String floatno) {
		String floatdetails = null;
		try {
			floatdetails = snoClaimProcessing.getcountremarkdetailspayment(urn, claimid, floatno);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return floatdetails;
	}

	@GetMapping(value = "/getActiondetails")
	@ResponseBody
	public String getActiondetails(@RequestParam(required = false, value = "groupId") Long groupId,
			@RequestParam(required = false, value = "statecode") String statecode,
			@RequestParam(required = false, value = "districtcode") String districtcode,
			@RequestParam(required = false, value = "hospitalcode") String hospitalcode,
			@RequestParam(required = false, value = "fromDate") Date fromDate,
			@RequestParam(required = false, value = "toDate") Date toDate,
			@RequestParam(required = false, value = "userId") Long userId) {
		String actionlist = null;
		try {
			actionlist = snoClaimProcessing.actiondetailsforsna(groupId, statecode, districtcode, hospitalcode,
					fromDate, toDate, userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return actionlist;
	}

	@GetMapping(value = "/getactionremarkdetails")
	@ResponseBody
	public String getactionremarkdetails(@RequestParam(required = false, value = "snaid") Long snaid,
			@RequestParam(required = false, value = "fromdate") Date fromdate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "hospitalcode") String hospitalcode,
			@RequestParam(required = false, value = "stateode") String stateode,
			@RequestParam(required = false, value = "distcode") String distcode) {
		String actionremark = null;
		try {
			actionremark = snoClaimProcessing.getactionremarkdetailssforsna(snaid, fromdate, todate, hospitalcode,
					stateode, distcode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return actionremark;
	}

	@GetMapping(value = "/getcountremarkforsnactiondetails")
	@ResponseBody
	public String getcountremarkforsnactiondetails(@RequestParam(required = false, value = "userid") Long userid,
			@RequestParam(required = false, value = "fromdate") Date fromdate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "statecode") String statecode,
			@RequestParam(required = false, value = "districtcode") String districtcode,
			@RequestParam(required = false, value = "hospitalcode") String hospitalcode,
			@RequestParam(required = false, value = "remarkid") Long remarkid,
			@RequestParam(required = false, value = "hospitalcodeforremark") String hospitalcodeforremark) {
		String countdetailsforsnaction = null;
		try {
			countdetailsforsnaction = snoClaimProcessing.getcountremarkdetailsforsnaaction(userid, fromdate, todate,
					statecode, districtcode, hospitalcode, remarkid, hospitalcodeforremark);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return countdetailsforsnaction;
	}

	@GetMapping(value = "/getHistoryofclaimno")
	@ResponseBody
	public Map<String, String> getHistoryofclaimno(@RequestParam("claimno") String claimno) {
		String claimhistory = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			claimhistory = snoClaimProcessing.getAllHistoryAgainstClaimnumber(claimno);
			details.put("status", "success");
			details.put("details", claimhistory);
		} catch (Exception e) {
			logger.error("Exception Occurred in getHistoryofclaimno Method of SnoClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@ResponseBody
	@PostMapping(value = "/getTriggerDetails")
	public Map<String, Object> getTriggerDetails(@RequestBody MeTriggerDetailsBean triggerDetails) {
		logger.info("Inside getHistoryofclaimno Method ofSnoClaimProcessingController");
		Map<String, Object> list = new HashMap<>();
		try {
			list = snoClaimProcessing.getTriggerDetails(triggerDetails);
		} catch (Exception e) {
			logger.error("Exception Occurred in getHistoryofclaimno Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return list;
	}

	// SNA BULK APPROVAL REVERT LIST
	@ResponseBody
	@GetMapping(value = "/getbulkapprovalrevertlist")
	public Map<String, Object> getbulkapprovalrevertlist(
			@RequestParam(required = false, value = "fromDate") String fromDate,
			@RequestParam(required = false, value = "toDate") String toDate,
			@RequestParam(required = false, value = "userId") Long userId) {
		List<Object> bulklist = null;
		Map<String, Object> records = new HashMap<String, Object>();
		try {
			bulklist = snoClaimProcessing.getbulkapprovallistofdata(fromDate, toDate, userId);
			records.put("status", "success");
			records.put("details", bulklist);
		} catch (Exception e) {
			logger.error("Exception Occurred in getbulkapprovalrevertlist Method of SnoClaimProcessingController : "
					+ e.getMessage());
			records.put("status", "fails");
			records.put("status", e.getMessage());
		}
		return records;
	}

	// FOR SNA BULK APPROVAL REVERT SUBMIT METHOD
	@PostMapping(value = "/getbulkapprovalrevertSubmit")
	public ResponseEntity<Response> getsubmitbulkapprovalrevert(@RequestBody Bulkrevertbean logBean) {
		Response response = null;
		try {
			response = snoClaimProcessing.getsubmitbulkapprovalrevertRecord(logBean);
		} catch (Exception e) {
			logger.error("Exception Occurred in getsubmitbulkapprovalrevert Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/getspecialtyreuquestlist")
	@ResponseBody
	public ResponseEntity<?> getSpecialtyReuquest(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			String snoRequestMap = snoClaimProcessing.getSpecialtyReuquest(requestBean);
			details.put("status", "success");
			details.put("data", snoRequestMap);
		} catch (Exception e) {
			logger.error("Exception Occurred in getSpecialtyReuquest Method of SnoClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/getspecialtyreuquestdetails")
	@ResponseBody
	public ResponseEntity<?> getSpecialtyReuquestDetails(
			@RequestParam(required = false, value = "requestId") Long requestId) throws Exception {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			String snoRequestDetails = snoClaimProcessing.getSpecialtyReuquestDetails(requestId);
			details.put("status", "success");
			details.put("data", snoRequestDetails);
		} catch (Exception e) {
			logger.error("Exception Occurred in getSpecialtyReuquest Method of SnoClaimProcessingController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/getsnafloatclaimdetails")
	@ResponseBody
	public Map<String, String> getSnaFloatClaimDetails(@RequestParam(required = false, value = "urn") String urn,
			@RequestParam(required = false, value = "claimid") Long claimid,
			@RequestParam(required = false, value = "floatno") String floatno) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = snoClaimProcessing.getSnaFloatClaimDetails(urn, claimid, floatno);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in SnoClaimById Method of SnoClaimProcessingController : " + e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}
	
	@GetMapping(value = "/getsnamortalitystatus")
	@ResponseBody
	public Map<String,String> getsnamortalitystatus(@RequestParam(required = false, value = "claimid") Long claimid) {
		Map<String, String> details = new HashMap<String, String>();
		try {
			String snoClaim = snoClaimProcessing.getsnamortalitystatus(claimid);
			details.put("status", "200");
			details.put("mortality", snoClaim==null?"":snoClaim);
		} catch (Exception e) {
			details.put("status","400");
			details.put("error", e.getMessage());
			details.put("mortality", "");
		}
		return details;
	}
	//for subcategory drop down in sna page
	@GetMapping("/getMstschemesubcategory")
	@ResponseBody
	public List<Mstschemesubcategory> getMstschemesubcategory() {
		List<Mstschemesubcategory> schemesubcategoryetails = snoClaimProcessing.getSchemesubcategory();
		return schemesubcategoryetails;
	}
}
