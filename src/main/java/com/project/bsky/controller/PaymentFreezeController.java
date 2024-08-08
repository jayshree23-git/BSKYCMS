package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.FloatReportBean;
import com.project.bsky.bean.FloatRequest;
import com.project.bsky.bean.FloatRequestBean;
import com.project.bsky.bean.HospitalwisefloatdetailsModaldata;
import com.project.bsky.bean.OldClaimPymntBean;
import com.project.bsky.bean.OldFloatBean;
import com.project.bsky.bean.PaymentActionBean;
import com.project.bsky.bean.PostPaymentRequest;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.ReversePaymentBean;
import com.project.bsky.bean.SnoClaimDetails;
import com.project.bsky.bean.TxnclamFloateDetailsbean;
import com.project.bsky.model.TxnclaimFloatActionLog;
import com.project.bsky.model.TxnclamFloateDetails;
import com.project.bsky.service.OldClaimFloatService;
import com.project.bsky.service.PaymentFreezeService;

@RestController
@RequestMapping(value = "/api")
public class PaymentFreezeController {

	@Autowired
	private PaymentFreezeService paymentFreezeService;

	@Autowired
	private OldClaimFloatService oldFloatService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/getpaymentfreezelist")
	public ResponseEntity<?> PaymentfreezeList(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		List<Object> paymentList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			paymentList = paymentFreezeService.getpaymentfreezedata(requestBean);
			details.put("status", "success");
			details.put("data", paymentList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@PostMapping(value = "/getrefundlist")
	public ResponseEntity<?> getRefundList(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		List<Object> paymentList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			paymentList = paymentFreezeService.getRefundList(requestBean);
			details.put("status", "success");
			details.put("data", paymentList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@PostMapping(value = "/getSnaApprovedList")
	public ResponseEntity<?> getSnaApprovedList(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		List<Object> paymentList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			paymentList = paymentFreezeService.getSnaApprovedList(requestBean);
			details.put("status", "success");
			details.put("data", paymentList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@PostMapping(value = "/getCountDetails")
	public String getCountDetails(@RequestBody FloatRequestBean requestBean) throws Exception {
		String summary = null;
		try {
			summary = paymentFreezeService.getSummary(requestBean).toString();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return summary;
	}

	@ResponseBody
	@GetMapping(value = "/getCountDetailsByFloatNo")
	public String getCountDetailsByFloatNo(@RequestParam("floatNumber") String floatNumber,
			@RequestParam("levelId") Long levelId) throws Exception {
		String summary = null;
		try {
			summary = paymentFreezeService.getCountDetails(floatNumber, levelId).toString();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return summary;
	}

	@PostMapping(value = "/gereratefloat", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> savePaymentfreeze(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "stateCodeList", required = false) String stateCodeList,
			@RequestParam(value = "distCodeList", required = false) String distCodeList,
			@RequestParam(value = "hospitalCodeList", required = false) String hospitalCodeList,
			@RequestParam(value = "snoUserId", required = false) Long snoUserId,
			@RequestParam(value = "snaAmount", required = false) Double snaAmount,
			@RequestParam(value = "floatFile", required = false) MultipartFile floatFile,
			@RequestParam(value = "searchtype", required = false) Integer searchtype,
			@RequestParam(value = "schemecategoryid", required = false) String schemecategoryid,
			@RequestParam(value = "floatList", required = false) List<String> floatList) {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = paymentFreezeService.savePaymentfreeze(userId, fromDate, toDate, stateCodeList, distCodeList,
					hospitalCodeList, snoUserId, snaAmount, floatFile, searchtype, schemecategoryid, floatList);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping("/getPaymentFrzDtlsList")
	public String getPaymentFrzDtlsList(@RequestParam("userId") Long userId, @RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate, @RequestParam("pendingAt") Long pendingAt) {
		try {
			Date fromDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate);
			Date toDate1 = new SimpleDateFormat("dd-MMM-yyyy").parse(toDate);
			return paymentFreezeService.getPaymentFreezeList(userId, fromDate1, toDate1, pendingAt).toString();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	@GetMapping("/getFloatClaimDetails")
	public List<Object> getFloatClaimDetails(@RequestParam("floatNo") String floatNo) {
		try {
			return paymentFreezeService.getFloatClaimDetails(floatNo);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	@GetMapping("/updateSnaApprvdAmnt")
	public ResponseEntity<Response> updateSnaApprvdAmnt(@RequestParam("claimId") Integer claimId,
			@RequestParam("amount") String amount, @RequestParam("userId") Long userId,
			@RequestParam("remark") String remark) {
		Response response = paymentFreezeService.updateSnaApprvdAmnt(claimId, amount, userId, remark);
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/floatlist")
	public ResponseEntity<?> getFLoatList(@RequestParam(value = "groupId", required = false) Integer groupId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "snoid", required = false) String snoid,
			@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "authMode", required = false) Integer authMode) {
		List<TxnclamFloateDetailsbean> list = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			list = paymentFreezeService.getfloatdata(groupId, fromdate, todate, snoid, userid, authMode);
			details.put("status", "success");
			details.put("data", list);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/floatdetails")
	public ResponseEntity<?> getFLoatDetails(@RequestParam(value = "floatNumber") String floatNumber) {
		List<Object> floatDetails = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			floatDetails = paymentFreezeService.getFLoatDetails(floatNumber);
			details.put("status", "success");
			details.put("data", floatDetails);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/floatdetailsbyhospital")
	public ResponseEntity<?> getFLoatDetailsByHospital(@RequestParam(value = "floatNumber") String floatNumber,
			@RequestParam(value = "hospitalCode") String hospitalCode) {
		List<Object> floatDetails = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			floatDetails = paymentFreezeService.getFLoatDetailsByHospital(floatNumber, hospitalCode);
			details.put("status", "success");
			details.put("data", floatDetails);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/updateremarks")
	public ResponseEntity<?> remarkUpdate(@RequestBody ClaimLogBean bean) {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = paymentFreezeService.remarkUpdate(bean);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/verifyfloat", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> verifyFloat(@RequestParam(value = "floatNumber") String floatNumber,
			@RequestParam(value = "actionBy") Long actionBy, @RequestParam(value = "remark") String remark,
			@RequestParam(value = "floatFile", required = false) MultipartFile file) {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = paymentFreezeService.verifyFloat(floatNumber, actionBy, remark, file);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping("/paymentFreeze")
	public ResponseEntity<Response> paymentFreeze(@RequestParam("floatNo") String floatNo,
			@RequestParam("userId") Long userId) {
		Response response = paymentFreezeService.paymentFreeze(floatNo, userId);
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/floatlistview")
	public ResponseEntity<?> getNonVerifiedFLoatList(@RequestParam(value = "groupId") Integer groupId) {
		List<TxnclamFloateDetails> list = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			if (groupId == 8) {
				list = paymentFreezeService.getFLoatList();
				details.put("status", "success");
				details.put("data", list);
			} else {
				details.put("status", "failed");
				details.put("msg", "Page for valid user");
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/getpostpaymentlist")
	public ResponseEntity<?> getPostPaymentList(@RequestBody CPDApproveRequestBean bean) {
		List<SnoClaimDetails> snoclaimList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			snoclaimList = paymentFreezeService.getPostPaymentList(bean);
			details.put("status", "success");
			details.put("data", snoclaimList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/postpaymentupdation")
	public ResponseEntity<?> updatePostPayment(@RequestBody PostPaymentRequest paymentRequest) {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = paymentFreezeService.updatePostPayment(paymentRequest);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@PostMapping(value = "/paymentfreeze")
	public ResponseEntity<?> freezePayment(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		Response paymentList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			paymentList = paymentFreezeService.freezePayment(userId, file);
			details.put("status", "success");
			details.put("data", paymentList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);

	}

	@ResponseBody
	@PostMapping(value = "/getPaymentFreezeReport")
	public String getPaymentFreezeReport(@RequestBody FloatReportBean requestBean) {
		JSONObject claimCount = null;
		try {
			claimCount = paymentFreezeService.getPaymentFreezeReport(requestBean.getUserId(), requestBean.getFromDate(),
					requestBean.getToDate(), requestBean.getStateId(), requestBean.getDistrictId(),
					requestBean.getHospitalId(), requestBean.getMortality(), requestBean.getSearchtype(),
					requestBean.getSchemecategoryid());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount.toString();
	}

	@PostMapping("/paymentFreezeAction")
	public ResponseEntity<Response> paymentFreezeAction(@RequestParam(required = false, value = "userId") String userId,
			@RequestParam(required = false, value = "file") MultipartFile file,
			@RequestParam(required = false, value = "fromDate") String fromDate,
			@RequestParam(required = false, value = "toDate") String toDate,
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId,
			@RequestParam(required = false, value = "hospitalId") String hospitalId,
			@RequestParam(required = false, value = "mortality") String mortality,
			@RequestParam(required = false, value = "amount") String amount,
			@RequestParam(required = false, value = "searchtype") String searchtype,
			@RequestParam(required = false, value = "schemecategoryid") String schemecategoryid) {
		Response response = new Response();
		PaymentActionBean bean = new PaymentActionBean();
		try {
			bean.setUserId(Long.parseLong(userId));
			bean.setFromDate(new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate));
			bean.setToDate(new SimpleDateFormat("dd-MMM-yyyy").parse(toDate));
			bean.setStateId(stateId);
			bean.setDistrictId(districtId);
			bean.setHospitalId(hospitalId);
			bean.setMortality(mortality);
			bean.setAmount(amount);
			bean.setSearchtype(Long.parseLong(searchtype));
			bean.setSchemecategoryid(schemecategoryid);
			Integer resp = paymentFreezeService.paymentFreezeAction(bean);
			if (resp == 1) {
				Integer res = paymentFreezeService.savePaymentFreezeRecord(file, bean);
				if (res == 1) {
					response.setStatus("success");
				} else {
					response.setStatus("failed");
				}
			} else {
				response.setStatus("failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@PostMapping(value = "/paymentFreezeView")
	public String paymentFreezeView(@RequestBody PaymentActionBean requestBean) {
		String floatList = null;
		try {
			floatList = paymentFreezeService.paymentFreezeView(requestBean).toString();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return floatList;
	}

	@GetMapping("/downloadPfzFile")
	public String downloadPfzFile(HttpServletResponse response, @RequestParam("file") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		try {
			byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
			String jsonString = new String(bytes, StandardCharsets.UTF_8);
			JSONObject json = new JSONObject(jsonString);
			String file = json.getString("f");
			String userId = json.getString("u");
			paymentFreezeService.downloadPfzFile(file, userId, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}

	@PostMapping(value = "/forwardtosna", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> forwardtosna(@RequestParam("floatNumber") String floatNumber,
			@RequestParam(value = "actionBy") Long actionBy, @RequestParam(value = "remark") String remark,
			@RequestParam(value = "floatFile", required = false) MultipartFile file) throws JSONException {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = paymentFreezeService.forwardToSNA(floatNumber, actionBy, remark, file);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	// FO REMARKlIST
	@GetMapping("/getforemarkList")
	public String getFORemarklist(@RequestParam("userId") Long userId, @RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate, @RequestParam("pendingAt") Long pendingAt) {
		return paymentFreezeService.getforremarkslistdata(userId, fromDate, toDate, pendingAt).toString();
	}

	// Fo Forward
	@GetMapping(value = "/paymentforward")
	public ResponseEntity<?> paymentforward(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "header", required = false) String header,
			@RequestParam(value = "remarks", required = false) String remarks,
			@RequestParam(value = "pendingAt", required = false) Long pendingAt) throws Exception {
		Response paymentList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			paymentList = paymentFreezeService.paymentForwardsubmit(userId, header, remarks, pendingAt);
			details.put("status", "success");
			details.put("data", paymentList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);

	}

	// Summarydetails
	@GetMapping("/summarydetails")
	public String getsummaryDtls(@RequestParam("userid") Long userid, @RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate, @RequestParam("verify") String verify,
			@RequestParam("schemecategoryid") String schemecategoryid) {
		return paymentFreezeService.getsummaryDetails(userid, fromDate, toDate, verify, schemecategoryid).toString();
	}

	// modal details
	@GetMapping("/viewmodaldata")
	public String getoldforemarkmodaldata(@RequestParam("claimid") Long claimid) {
		return paymentFreezeService.getoldforemarkmodaldata(claimid).toString();
	}

	// hospitalwiseFloatdetails
	@ResponseBody
	@GetMapping(value = "/getHospitalwisefloatdetails")
	public String getHospitalwisefloatdetails(
			@RequestParam("floatnumberhospitawisedetails") String floatnumberhospitawisedetails,
			@RequestParam("fromdate") String fromdate) {
		return paymentFreezeService.getfloatdetailshospitalwise(floatnumberhospitawisedetails, fromdate).toString();
	}

	// modal data
	@ResponseBody
	@PostMapping(value = "/getHospitalwisefloatdetailsmodaldata")
	public List<Object> getActionWiseFloatReport(@RequestBody HospitalwisefloatdetailsModaldata requestBean) {
		List<Object> Hospitalwisefloatdetails = new ArrayList<Object>();
		try {
			Hospitalwisefloatdetails = paymentFreezeService.getHospitalwisefloatdetailsReport(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return Hospitalwisefloatdetails;
	}

	// Fo Forward
	@GetMapping(value = "/pendingFloatFo")
	public ResponseEntity<?> getPendingFloatFo(@RequestParam(value = "userId", required = false) Integer userId)
			throws Exception {
		List<TxnclamFloateDetails> pendingFLoat = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			pendingFLoat = paymentFreezeService.getAssignedFO(userId);
			details.put("status", "success");
			details.put("data", pendingFLoat);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);

	}

	// Fo Forward
	@ResponseBody
	@PostMapping(value = "/assignToFo")
	public ResponseEntity<?> assignToFo(@RequestBody FloatRequest floatRequest) throws Exception {
		Response response = new Response();
		List<TxnclamFloateDetails> pendingFLoat = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = paymentFreezeService.assignToFo(floatRequest);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);

	}

	@ResponseBody
	@PostMapping(value = "/forwardFloat", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> forwardFloat(@RequestParam(value = "floatList", required = false) List<Long> floatList,
			@RequestParam(value = "remark", required = false) String remark,
			@RequestParam(value = "pendingAt", required = false) Integer pendingAt,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "floatFile", required = false) MultipartFile file) throws Exception {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			Response res = paymentFreezeService.forwardFloat(floatList, remark, pendingAt, userId, file);
			details.put("status", "success");
			details.put("data", res.getMessage());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@PostMapping(value = "/getPaymentFreezeOldReport")
	public String getPaymentFreezeOldReport(@RequestBody OldFloatBean requestBean) {
		String claimCount = null;
		try {
			claimCount = oldFloatService.getPaymentFreezeReport(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimCount;
	}

	@PostMapping("/paymentFreezeOldAction")
	public ResponseEntity<Response> paymentFreezeOldAction(
			@RequestParam(required = false, value = "userId") String userId,
			@RequestParam(required = false, value = "file") MultipartFile file,
			@RequestParam(required = false, value = "fromDate") String fromDate,
			@RequestParam(required = false, value = "toDate") String toDate,
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId,
			@RequestParam(required = false, value = "hospitalId") String hospitalId) {
		Response response = new Response();
		try {
			Long id = Long.parseLong(userId);
			Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate);
			Date tdate = new SimpleDateFormat("dd-MMM-yyyy").parse(toDate);
			Integer resp = oldFloatService.paymentFreezeAction(id, fdate, tdate, stateId, districtId, hospitalId);
			if (resp == 1) {
				Integer res = oldFloatService.savePaymentFreezeRecord(file, id, fdate, tdate, stateId, districtId,
						hospitalId);
				if (res == 1) {
					response.setStatus("success");
				} else {
					response.setStatus("failed");
				}
			} else {
				response.setStatus("failed");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
		}
		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@PostMapping(value = "/paymentFreezeViewOld")
	public String paymentFreezeViewOld(@RequestBody OldFloatBean requestBean) {
		String floatList = null;
		try {
			floatList = oldFloatService.paymentFreezeView(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return floatList;
	}

	@GetMapping("/downloadOldPfzFile")
	public String downloadOldPfzFile(HttpServletResponse response, @RequestParam("file") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		try {
			byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
			String jsonString = new String(bytes, StandardCharsets.UTF_8);
			JSONObject json = new JSONObject(jsonString);
			String file = json.getString("f");
			String userId = json.getString("u");
			oldFloatService.downloadPfzFile(file, userId, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}

	@PostMapping(value = "/getOldPostPaymentList")
	public List<OldClaimPymntBean> getOldPostPaymentList(@RequestBody OldFloatBean bean) {
		List<OldClaimPymntBean> snoclaimList = new ArrayList<OldClaimPymntBean>();
		try {
			snoclaimList = oldFloatService.getPostPaymentList(bean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return snoclaimList;
	}

	@PostMapping(value = "/oldpostpaymentupdation")
	public ResponseEntity<?> updateOldPostPayment(@RequestBody PostPaymentRequest paymentRequest) {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = oldFloatService.updatePostPayment(paymentRequest);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/paymentFreezeDetails")
	public String paymentFreezeClaimDetails(@RequestParam(required = false, value = "fromDate") String fromDate,
			@RequestParam(required = false, value = "toDate") String toDate,
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId,
			@RequestParam(required = false, value = "hospitalId") String hospitalId,
			@RequestParam(required = false, value = "mortality") String mortality) {
		String claimList = null;
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
			Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate);
			Date tdate = new SimpleDateFormat("dd-MMM-yyyy").parse(toDate);
			claimList = paymentFreezeService.paymentFreezeClaimDetails(fdate, tdate, stateId, districtId, hospitalId,
					mortality);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimList;
	}

	@GetMapping(value = "/paymentFreezeOldDetails")
	public String paymentFreezeOldDetails(@RequestParam(required = false, value = "fromDate") String fromDate,
			@RequestParam(required = false, value = "toDate") String toDate,
			@RequestParam(required = false, value = "stateId") String stateId,
			@RequestParam(required = false, value = "districtId") String districtId,
			@RequestParam(required = false, value = "hospitalId") String hospitalId) {
		String claimList = null;
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
			Date fdate = new SimpleDateFormat("dd-MMM-yyyy").parse(fromDate);
			Date tdate = new SimpleDateFormat("dd-MMM-yyyy").parse(toDate);
			claimList = oldFloatService.paymentFreezeClaimDetails(fdate, tdate, stateId, districtId, hospitalId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimList;
	}

	@PostMapping(value = "/getPaymentList")
	public ResponseEntity<?> getPaymentList(@RequestBody ReversePaymentBean bean) {
		String snoclaimList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			snoclaimList = paymentFreezeService.getPaymentList(bean);
			details.put("status", "success");
			details.put("data", snoclaimList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/getPaidClaimList")
	public ResponseEntity<?> getPaidClaimList(@RequestBody ReversePaymentBean bean) {
		List<Object> snoclaimList = new ArrayList<Object>();
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			snoclaimList = paymentFreezeService.getPaidClaimList(bean);
			details.put("status", "success");
			details.put("data", snoclaimList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/reversepayment")
	public ResponseEntity<?> reversePayment(@RequestBody ReversePaymentBean paymentRequest) {
		Response response = new Response();
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = paymentFreezeService.reversePayment(paymentRequest);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/getpostpaymenView")
	public ResponseEntity<?> getpostpaymenView(@RequestBody CPDApproveRequestBean requestBean) {
		List<SnoClaimDetails> postpaymnetview = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			postpaymnetview = paymentFreezeService.getPostPaymenView(requestBean);
			details.put("status", "success");
			details.put("data", postpaymnetview);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/pendingmoratlityStatus")
	public String pendingMortalitystatus(@RequestParam(required = false, value = "userid") String userid,
			@RequestParam(required = false, value = "fromdate") Date fromdate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "statecode") String statecode,
			@RequestParam(required = false, value = "districtcode") String districtcode,
			@RequestParam(required = false, value = "hospitalcode") String hospitalcode) {
		String claimList = null;
		try {
			claimList = oldFloatService.pendingmortality(userid, fromdate, todate, statecode, districtcode,
					hospitalcode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimList;
	}

	@GetMapping(value = "/floatLogHistory")
	public ResponseEntity<?> getFloatLogHistory(@RequestParam(required = false, value = "floatId") Long floatId) {
		List<TxnclaimFloatActionLog> logList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			logList = paymentFreezeService.getLogHistory(floatId);
			details.put("status", "success");
			details.put("data", logList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/floatdocdownload")
	public void floatDocDownload(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		String floatNumber = json.getString("h");
		try {
			paymentFreezeService.floatDocDownload(fileName, floatNumber, response);
		} catch (Exception e) {
			logger.error("Exception Occurred in commonDownloadMethod Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
	}

	@ResponseBody
	@PostMapping(value = "/getDistrictByMultiState")
	public ResponseEntity<?> getDistrict(@RequestBody Map<String, Object> mapObj) {
		String districtList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			districtList = paymentFreezeService.getDistrictList(mapObj);
			details.put("status", "success");
			details.put("data", districtList);
		} catch (Exception e) {
			details.put("status", "fail");
			details.put("msg", e.getMessage());
			logger.error(
					"Exception Occurred in getDistrict Method of SnoClaimProcessingController : " + e.getMessage());
		}
		return ResponseEntity.ok(details);

	}

	@ResponseBody
	@PostMapping(value = "/getHospitalByMultiDistrict")
	public ResponseEntity<?> getHospitalByMultiDistrict(@RequestBody Map<String, Object> mapObj) {
		String hospitalList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			hospitalList = paymentFreezeService.getHospitalByMultiDistrict(mapObj);
			details.put("status", "success");
			details.put("data", hospitalList);
		} catch (Exception e) {
			details.put("status", "fail");
			details.put("msg", e.getMessage());
			logger.error(
					"Exception Occurred in getHospital Method of SnoClaimProcessingController : " + e.getMessage());
		}
		return ResponseEntity.ok(details);

	}

	@GetMapping(value = "/docFloat")
	public void floatDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		String floatNumber = json.getString("h");
		String currentYear = json.getString("c");
		try {
			paymentFreezeService.downLoadFilefloat(fileName, floatNumber, currentYear, response);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in floatDownloadMethod Method of PaymentFreezeController : " + e.getMessage());
		}
	}

	// get float Draft List
	@GetMapping(value = "/floatDraftList")
	public ResponseEntity<?> getfloatDraftList(@RequestParam(value = "snoid", required = false) Long snoid,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate) {
		List<TxnclamFloateDetailsbean> list = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			list = paymentFreezeService.getDraftList(snoid,fromdate,todate);
			details.put("status", "success");
			details.put("data", list);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/forwarddraftfloat", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> forwardDraftFloat(@RequestParam(value = "floatId", required = false) String floatNumber,
			@RequestParam(value = "snoUserId", required = false) Long snoUserId,
			@RequestParam(value = "snacertification", required = false) MultipartFile snacertification,
			@RequestParam(value = "mecertification", required = false) MultipartFile mecertification,
			@RequestParam(value = "otherfile", required = false) MultipartFile otherfile,
			@RequestParam(value = "description", required = false) String description) {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = paymentFreezeService.forwardDraftFloat(floatNumber, snoUserId, snacertification, mecertification,
					otherfile, description);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/floatView")
	public ResponseEntity<?> getFLoatView(@RequestParam(value = "groupId", required = false) Integer groupId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "snoid", required = false) String snoid,
			@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "authMode", required = false) Integer authMode) {
		List<TxnclamFloateDetailsbean> listview = null;
		Map<String, Object> detailsview = new HashMap<String, Object>();
		try {
			listview = paymentFreezeService.getfloatViewdata(groupId, fromdate, todate, snoid, userid, authMode);
			detailsview.put("status", "success");
			detailsview.put("data", listview);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			detailsview.put("status", "fail");
			detailsview.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(detailsview);
	}

	@PostMapping(value = "/floatclaimaction")
	public ResponseEntity<Response> saveFloatClaimAction(@RequestBody ClaimLogBean logBean) {
		Response response = null;
		try {
			response = paymentFreezeService.saveFloatClaimAction(logBean);
		} catch (Exception e) {
			logger.error("Exception Occurred in saveFloatClaimAction Method of SnoClaimProcessingController : "
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}
	
	//post -payment new details page
	@GetMapping("/getFloatClaimDetailsList")
	public List<Object> getFloatClaimDetailsList(@RequestParam("floatNo") String floatNo) {
		try {
			return paymentFreezeService.getFloatClaimDetailsList(floatNo);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

}
