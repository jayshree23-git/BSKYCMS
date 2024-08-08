package com.project.bsky.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.project.bsky.bean.Cpdapprovalbean;
import com.project.bsky.bean.DocumentclickStatus;
import com.project.bsky.bean.Response;
import com.project.bsky.exception.CPDClaimProcessingException;
import com.project.bsky.model.PackageDetails;
import com.project.bsky.service.CPDClaimProcessingService;

/**
 * @author ipsita.shaw
 * @author : Sambit Kumar Pradhan
 *
 */
@RestController
@RequestMapping(value = "/api")
@PropertySource("classpath:error-message.properties")
public class CPDProcessingController {
	private final Logger logger;

	@Autowired
	public CPDProcessingController(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private CPDClaimProcessingService cpdClaimProcess;

	@Value("${cpdclaim.fetchrecord.error.message}")
	private String fetchMessage;

	/**
	 * @author ipsita.shaw
	 * @date 26-08-2022 desc- This method is used to get the list of claim raised by
	 *       hospital
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/getCPDClaimDetails")
	public String getAllClaimRaised(@RequestParam("userId") String userId,
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
		if (orderValue == null || orderValue.equals("0"))
			orderValue = "URN";
		if (fromDate != null || toDate != null) {
			fromDate1 = new SimpleDateFormat("dd-MM-yy").format(fromDate);
			toDate1 = new SimpleDateFormat("dd-MM-yy").format(toDate);
		} else {
			fromDate1 = null;
			toDate1 = null;
		}
		try {
			claimList = cpdClaimProcess.getAllClaimRaised(Integer.parseInt(userId), orderValue, fromDate1, toDate1,
					authMode, trigger,schemeid,schemecategoryid);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getAllClaimRaised method of CPDProcessingController : " + e.getMessage());
		}
		return claimList;
	}

	@GetMapping(value = "/getCPDDraftCliamDetails")
	public Map<String, Object> getCPDDraftCliamDetails(
			@RequestParam("userId") String userId,
			@RequestParam("schemeid") Integer schemeid,
			@RequestParam(value = "schemecategoryid", required = false) String schemecategoryid) {
		String claimList = null;
		Map<String, Object> details = new HashMap<>();
		try {
			claimList = cpdClaimProcess.getCPDDraftCliamDetails(Integer.parseInt(userId),schemeid,schemecategoryid);
			details.put("status", "success");
			details.put("data", claimList);
		} catch (Exception e) {
			details.put("status", "fail");
			details.put("data", e.getMessage());
			logger.error(
					"Exception Occurred in getAllClaimRaised method of CPDProcessingController : " + e.getMessage());
		}
		return details;
	}

	/**
	 * @author ipsita.shaw
	 * @date 27-08-2022
	 * @param transaction_id
	 * @param urn
	 * @param claimId
	 * @return
	 */
	@GetMapping(value = "/getClaimDetails")
	public String getClaimDetails(@RequestParam("transaction_id") String transactionid, @RequestParam("urn") String urn,
			@RequestParam("claimId") String claimId, @RequestParam("authorizedCode") String authorizedCode,
			@RequestParam("hospitalCode") String hospitalCode, @RequestParam("actualDate") String actualDate,
			@RequestParam(value = "caseNo") String caseNo, @RequestParam(value = "userId") Long userId,
			@RequestParam(value = "claimNo") String claimNo) throws SQLException {
		String mod = cpdClaimProcess.getClaimDetails(transactionid, urn, claimId, authorizedCode, hospitalCode,
				actualDate, caseNo, userId, new Date(), claimNo);
		if (mod == null) {
			throw new CPDClaimProcessingException(String.format(fetchMessage, urn), new Throwable("DNF"));
		}
		return mod;
	}

	@GetMapping(value = "/getIndividualDraftClaimDetails")
	public String getIndividualDraftClaimDetails(@RequestParam("transaction_id") String transactionid,
			@RequestParam("urn") String urn, @RequestParam("claimId") String claimId,
			@RequestParam("authorizedCode") String authorizedCode, @RequestParam("hospitalCode") String hospitalCode,
			@RequestParam("actualDate") String actualDate, @RequestParam(value = "caseNo") String caseNo,
			@RequestParam(value = "userId") Long userId, @RequestParam(value = "claimNo") String claimNo)
			throws SQLException {
		String mod = cpdClaimProcess.getIndividualDraftClaimDetails(transactionid, urn, claimId, authorizedCode,
				hospitalCode, actualDate, caseNo, userId, new Date(), claimNo);
		if (mod == null) {
			throw new CPDClaimProcessingException(String.format(fetchMessage, urn), new Throwable("DNF"));
		}
		return mod;
	}

	@ResponseBody
	@PostMapping(value = "/saveCpdClaimActionRequest")
	public ResponseEntity<Map<String, Object>> saveCpdClaimActionRequest(@RequestBody ClaimLogBean requestBean) {

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
	 * @author ipsita.shaw
	 * @date 27-08-2022
	 * @param transaction_id
	 * @param urn
	 * @param claimId
	 * @return
	 */
	@GetMapping(value = "/getPreAuthHistory")
	public String getPreAuthHistory(@RequestParam("urn") String urn,
			@RequestParam("authorizedCode") String authorizedCode, @RequestParam("hospitalCode") String hospitalCode) {
		String mod = cpdClaimProcess.getPreAuthHistoryDetails(urn, authorizedCode, hospitalCode);
		if (mod == null) {
			throw new CPDClaimProcessingException(String.format(fetchMessage, urn), new Throwable("DNF"));
		}
		return mod;
	}

	@GetMapping(value = "/getMultiPackDtls")
	public String getMultiPackDtls(@RequestParam("transactionID") String transaction_id,
			@RequestParam("urn") String urn, @RequestParam("authorizedCode") String authorizedCode,
			@RequestParam("hospitalCode") String hospitalCode) {
		String mod = cpdClaimProcess.getMultiPackDtls(transaction_id, urn, authorizedCode, hospitalCode);
		if (mod == null) {
			throw new CPDClaimProcessingException(String.format(fetchMessage, urn), new Throwable("DNF"));
		}
		return mod;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping(value = "/getPackageDetails")
	public ResponseEntity<?> getPackageDetails(@RequestParam("packageId") String packageId,
			@RequestParam("procedureCode") String procedureCode) {
		Map<String, Object> details = new HashMap<String, Object>();
		PackageDetails response = new PackageDetails();
		try {
			response = cpdClaimProcess.getPackageDetails(packageId.trim(), procedureCode.trim());
			if (response != null) {
				details.put("status", "success");
				details.put("packageDetails", response);
			} else {
				details.put("status", "fail");
				details.put("packageDetails", null);
			}
		} catch (Exception ex) {
			logger.error(
					"Exception Occurred in getPackageDetails method of CPDProcessingController : " + ex.getMessage());
			details.put("status", "fail");
			details.put("msg", ex.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	/**
	 * @Author : Sambit Kumar Pradhan
	 * @Date : 01-01-2023
	 * @Description : For Generating PDF From Given List of Data
	 */
	@GetMapping(value = "/generatePDF")
	public void generatePDF(@RequestParam("data") String data, HttpServletResponse httpServletResponse)
			throws JSONException {
		try {
			byte[] bytes = Base64.getDecoder().decode(data);
			JSONArray reports = new JSONObject(new String(bytes)).getJSONArray("report");
			JSONArray headers = (JSONArray) new JSONObject(new String(bytes)).getJSONArray("heading").get(0);
			cpdClaimProcess.generatePDF(reports, headers, httpServletResponse);
		} catch (Exception e) {
			logger.error("Exception Occurred in generatePDF method of CPDProcessingController : " + e.getMessage());
		}
	}

	/**
	 * @Author : Sambit Kumar Pradhan
	 * @Date : 01-02-2023
	 * @Description : For Fetching CPD Approval List Count
	 */
	@GetMapping(value = "/getCPDApprovalListCount")
	public ResponseEntity<?> getCPDApprovalListCount(@RequestParam("userId") String userId,
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
			response.put("data",
					cpdClaimProcess.getCPDApprovalListCount(userId, orderValue, fromDate1, toDate1, authMode, trigger,schemeid,schemecategoryid));
		} catch (Exception ex) {
			logger.error("Exception Occurred in getCPDApprovalListCount method of CPDProcessingController : "
					+ ex.getMessage());
			response.put("status", "fail");
			response.put("msg", ex.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * @Author : Sambit Kumar Pradhan
	 * @Date : 01-02-2023
	 * @Description : For Fetching CPD Approval List
	 */
	@GetMapping(value = "/getOldTreatmentHistoryCPD")
	private ResponseEntity<?> getOldTreatmentHistoryCPD(@RequestParam(required = false, value = "urn") String urn) {
		Map<String, Object> response = new HashMap<>();
		try {
			response.put("status", "success");
			response.put("data", cpdClaimProcess.getOldTreatmentHistoryCPD(urn));
		} catch (Exception e) {
			logger.error("Exception Occurred in getOldTreatmentHistoryCPD method of CPDProcessingController : "
					+ e.getMessage());
			response.put("status", "fail");
			response.put("msg", e.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// Documnet _click_status
	@PostMapping(value = "/documnetinsertstatus")
	public ResponseEntity<?> getdocumnetinserstatus(@RequestBody DocumentclickStatus documnetstatus) {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = cpdClaimProcess.getDocumnetinsert(documnetstatus);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			e.printStackTrace();
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@PostMapping(value = "/cpdapprovalnew")
	public ResponseEntity<Response> saveCpdClaimActionRequestnew(@RequestBody Cpdapprovalbean requestBean) {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = cpdClaimProcess.saveCpdClaimActionnew(requestBean);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage(e.getMessage());
			logger.error("Exception occured in saveCpdClaimActionRequestnew method of saveCpdClaimActionnew"
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}
	
	@ResponseBody
	@PostMapping(value = "/cpddraftaction")
	public ResponseEntity<Response> saveCpdClaimDraftAction(@RequestBody Cpdapprovalbean requestBean) {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = cpdClaimProcess.saveCpdClaimDraftAction(requestBean);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage(e.getMessage());
			logger.error("Exception occured in saveCpdClaimActionRequestnew method of saveCpdClaimActionnew"
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}
}
