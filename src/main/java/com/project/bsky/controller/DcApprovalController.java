/**
 * 
 */
package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.DCDashboardBean;
import com.project.bsky.bean.Enrollmentapprovalbean;
import com.project.bsky.bean.GODashboardBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.DcClaimApprovalDetailsService;

/**
 * @author arabinda.guin
 *
 */
@Controller
@RequestMapping(value = "/api")
public class DcApprovalController {

	@Autowired
	private DcClaimApprovalDetailsService dcService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getDcClaimList")
	public String SnoClaimDetails(@RequestParam(value = "userId") Long userId) throws Exception {
		String dcClaimList = null;
		try {
			dcClaimList = dcService.getDcClaimList(userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return dcClaimList;
	}

	@GetMapping(value = "/getDcDetailsById")
	@ResponseBody
	public Map<String, String> getMultiPackageBlock(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = dcService.getDcClaimListById(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@PostMapping(value = "/dcClaimSubmit")
	@ResponseBody
	public ResponseEntity<?> dcClaimSubmit(@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "txnId") Long txnId, @RequestParam(value = "URN") String URN,
			@RequestParam(value = "hospitalCode") String hospitalCode, @RequestParam(value = "claimId") Long claimId,
			@RequestParam(value = "claimAmount", required = false) Double claimAmount,
			@RequestParam(value = "additionalDoc", required = false) String additionalDoc,
			@RequestParam(value = "additionalDoc1", required = false) String additionalDoc1,
			@RequestParam(value = "additionalDoc2", required = false) String additionalDoc2,
			@RequestParam(value = "dischargeSlip") String dischargeSlip,
			@RequestParam(value = "preSurgery", required = false) String preSurgery,
			@RequestParam(value = "postSurgery", required = false) String postSurgery,
			@RequestParam(value = "intraSurgery", required = false) String intraSurgery,
			@RequestParam(value = "specimenRemoval", required = false) String specimenRemoval,
			@RequestParam(value = "patientPhoto", required = false) String patientPhoto,
			@RequestParam(value = "investigation1") MultipartFile investigation1Doc,
			@RequestParam(value = "investigation2", required = false) MultipartFile investigation2Doc,
			@RequestParam(value = "dateOfAdmission", required = false) String dateOfAdmission,
			@RequestParam(value = "remarks") String remarks) {
		Map<String, Object> response = new HashMap<>();
		Integer msgOut;
		try {
			msgOut = dcService.dcClaimSubmit(userId, txnId, URN, hospitalCode, claimId, claimAmount, additionalDoc,
					additionalDoc1, additionalDoc2, dischargeSlip, preSurgery, postSurgery, intraSurgery,
					specimenRemoval, patientPhoto, investigation1Doc, investigation2Doc, dateOfAdmission, remarks);
			if (msgOut == 1) {
				response.put("statusCode", HttpStatus.OK.value());
				response.put("status", "Success");
				response.put("message", "DC Claim Submitted Successfully.");
			} else {
				response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.put("status", "Failed");
				response.put("message", "DC Claim Submission Failed.");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			response.put("message", e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/getDcInvestigationCount")
	public String DCInvstigationReport(@RequestBody DCDashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getDCInvestigationCount(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;
	}

	@ResponseBody
	@PostMapping(value = "/getDcOverrideCount")
	public String DCOverrideReport(@RequestBody DCDashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getDCOverRideCount(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;

	}

	@ResponseBody
	@PostMapping(value = "/getDcGrievanceCount")
	public String DCGrievanceReport(@RequestBody DCDashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getDCGrievanceCount(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;

	}

	@ResponseBody
	@PostMapping(value = "/getDcGrievanceResolveCount")
	public String DCGrievanceResolveReport(@RequestBody DCDashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getDCGrievanceResolveCount(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;

	}

	@ResponseBody
	@PostMapping(value = "/getDcGrievanceModeCount")
	public String DCGrievanceModeReport(@RequestBody DCDashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getDCGrievanceModeCount(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;

	}

	@ResponseBody
	@PostMapping(value = "/getCCECountReport")
	public String DCCCECountReport(@RequestBody DCDashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getCCECountReport(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;

	}

	@ResponseBody
	@PostMapping(value = "/getEmpCountReport")
	public String DCEmpCountReport(@RequestBody DCDashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getEmpCountReport(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;

	}

	@ResponseBody
	@PostMapping(value = "/getGOGrievanceSecCount")
	public String GOGrievanceReport(@RequestBody GODashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getGOCountReport(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;

	}

	@ResponseBody
	@PostMapping(value = "/getGOGrievanceMedCount")
	public String GOGrievanceMediunCount(@RequestBody GODashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getGOMediumCount(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;

	}

	@ResponseBody
	@PostMapping(value = "/getGODistrictSecCount")
	public String GODistrictWiseReport(@RequestBody GODashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getGODistrictWiseCount(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;

	}

	@ResponseBody
	@PostMapping(value = "/getGOCCESecCount")
	public String GOCCEReport(@RequestBody GODashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getGOCCEWiseCount(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;

	}

	@ResponseBody
	@PostMapping(value = "/getGOCCETBLSecCount")
	public String GOCCETBLReport(@RequestBody GODashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getGOCCEDistrictWiseCount(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;

	}

	@ResponseBody
	@GetMapping(value = "/getenrollmentlist")
	public List<Object> getenrollmentlist(@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "urn", required = false) String urn) {
		List<Object> claiList = null;
		try {
			claiList = dcService.getlist(fromDate, toDate, userId, urn);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claiList;
	}

	@GetMapping(value = "/getenrollmentthroughid")
	@ResponseBody
	public Map<String, String> getenrollmentthroughid(@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "depregid", required = false) Long depregid,
			@RequestParam(value = "acknowledgementnumber", required = false) String acknowledgementnumber) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = dcService.getenrollmentdetailsthroughid(fromDate, toDate, userId, depregid,
					acknowledgementnumber);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@ResponseBody()
	@PostMapping(value = "/saveEnrollmentaction")
	public ResponseEntity<Response> saveEnrollmentaction(@RequestBody Enrollmentapprovalbean requestBean) {
		Response response = new Response();
		try {
			response = dcService.getaction(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("Failed");
			response.setMessage(e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/getactiontakenhistory")
	@ResponseBody
	public Map<String, String> getactiontakenhistory(@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "depregid", required = false) Long depregid,
			@RequestParam(value = "acknowledgementnumber", required = false) String acknowledgementnumber) {
		String history = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			history = dcService.getactiontakenhistorydetails(fromDate, toDate, userId, depregid, acknowledgementnumber);
			details.put("status", "success");
			details.put("details", history);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@ResponseBody
	@PostMapping(value = "/getGrievancePendingCountDetails")
	public String getGrievancePendingCountDetails(@RequestBody GODashboardBean requestBean) throws Exception {
		String claimCount = null;
		try {
			claimCount = dcService.getGOCountReportDetails(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;
	}

	@ResponseBody
	@GetMapping(value = "/getrecomplylist")
	public List<Object> getrecomplylist(@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "urn", required = false) String urn,
			@RequestParam(value = "userId", required = false) Long userId) {
		List<Object> claiList = null;
		try {
			claiList = dcService.getcomplylist(fromDate, toDate, urn, userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claiList;
	}

	@GetMapping(value = "/docenrollment")
	public void commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		String year = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		String hCode = json.getString("h");
		String dateOfAdm = json.getString("d");
		String Statecode = json.getString("s");
		String districtcode = json.getString("dis");
		String blockcode = json.getString("b");
		String enrollmentfolder = "Enrollment";
		try {
			if (dateOfAdm.length() > 11) {
				String preAuthDate = new SimpleDateFormat("dd MMM yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(dateOfAdm));
				year = preAuthDate.substring(6);
			} else {
				year = dateOfAdm.substring(6);
			}
			dcService.downLoadFile(fileName, year, hCode, Statecode, districtcode, blockcode, enrollmentfolder,
					response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	@ResponseBody
	@GetMapping(value = "/getactiontakenhistoryen")
	public List<Object> getactiontakenhistory(@RequestParam(value = "enggid", required = false) Long enggid,
			@RequestParam(value = "acknowledgementno", required = false) String acknowledgementno) {
		List<Object> claiList = null;
		try {
			claiList = dcService.getactiontakenhistorylist(enggid, acknowledgementno);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claiList;
	}

	@ResponseBody
	@GetMapping(value = "/gethospitalenrollmentlistactiontakenlist")
	public List<Object> gethospitalenrollmentlistactiontakenlist(
			@RequestParam(value = "urn", required = false) String urn,
			@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "searchdata", required = false) Long searchdata,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "hospital", required = false) String hospital) {
		List<Object> actionlist = null;
		try {
			actionlist = dcService.gethospitalenrollmentlistactiontakenDetails(urn, fromDate, toDate, userId, username,
					searchdata,state,dist,hospital);
		} catch (Exception e) {
			logger.error("Exception occured in gethospitalenrollmentlistactiontakenlist method of getapplicationstatus"
					+ e.getMessage());
		}
		return actionlist;
	}
}
