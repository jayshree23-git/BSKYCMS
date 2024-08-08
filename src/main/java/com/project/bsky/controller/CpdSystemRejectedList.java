/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.BulkDateExtensionBean;
import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.CpdQueryToHospitalBean;
import com.project.bsky.bean.DateExtensionBean;
import com.project.bsky.bean.NonComplianceBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.Snawiserununprocessedupdate;
import com.project.bsky.bean.Snawiseunprocessedbean;
import com.project.bsky.bean.SystemRejQueryCpdBean;
import com.project.bsky.service.CpdSystemRejectedListService;

/**
 * @author hrusikesh.mohanty
 *
 */

@RestController
@RequestMapping(value = "/api")
public class CpdSystemRejectedList {

	private final Logger logger;

	@Autowired
	public CpdSystemRejectedList(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private CpdSystemRejectedListService cpdsystemrejeted;

	@ResponseBody
	@GetMapping(value = "/getCpdSystemRejectedList")
	public List<CpdQueryToHospitalBean> getCpdSystemRejectedList(
			@RequestParam(value = "hospitalcoderejected", required = false) String hospitalcoderejected,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "Package", required = false) String Package,
			@RequestParam(value = "packageCodedata", required = false) String packageCodedata,
			@RequestParam(value = "URN", required = false) String URN,
			@RequestParam(value = "schemeid", required = false) String schemeid,
			@RequestParam(value = "schemecategoryid", required = false) String schemecategoryid) {
		List<CpdQueryToHospitalBean> cpdList = null;
		try {
			if (Objects.equals(fromDate, "") && Objects.equals(toDate, "")) {
				cpdList = cpdsystemrejeted.getRejetedData(hospitalcoderejected, null, null, Package, packageCodedata,
						URN, schemeid, schemecategoryid);
			} else {
				cpdList = cpdsystemrejeted.getRejetedData(hospitalcoderejected, fromDate, toDate, Package,
						packageCodedata, URN, schemeid, schemecategoryid);
			}
		} catch (Exception e) {
			logger.error(
					"Exception occured in getCpdSystemRejectedList method of CpdSystemRejectedList" + e.getMessage());
		}
		return cpdList;
	}

	@GetMapping(value = "/getRequestedByTransactionId")
	@ResponseBody
	public Map<String, String> RequestsById(@RequestParam("txnId") Integer txnId) {
		String requestList = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			requestList = cpdsystemrejeted.getRequestByDetailId(txnId);
			details.put("status", "success");
			details.put("details", requestList);
		} catch (Exception e) {
			details.put("status", "fails");
			details.put("status", e.getMessage());
			logger.error("Exception occured in RequestsById method of CpdSystemRejectedList" + e.getMessage());
		}
		return details;
	}

	@PostMapping(value = "/rejectRequestCpd")
	public ResponseEntity<Response> saveRejectedRequest(@RequestBody SystemRejQueryCpdBean rejBean) {
		Response response = null;
		try {
			response = cpdsystemrejeted.saveRejectRequestCPD(rejBean);
		} catch (Exception e) {
			logger.error("Exception occured in saveRejectedRequest method of CpdSystemRejectedList" + e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/getCpdSystemRejectedListToSNA")
	@ResponseBody
	public ResponseEntity<?> getSNAClaimApprove(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		List<Object> snoclaimList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			snoclaimList = cpdsystemrejeted.getRejetedDataCPDToSNA(requestBean);
			details.put("status", "success");
			details.put("data", snoclaimList);
		} catch (Exception e) {
			details.put("status", "fail");
			details.put("msg", e.getMessage());
			logger.error("Exception occured in getSNAClaimApprove method of CpdSystemRejectedList" + e.getMessage());
		}
		return ResponseEntity.ok(details);

	}

	@GetMapping(value = "/getNonComplianceById")
	@ResponseBody
	public Map<String, String> SnoClaimById(@RequestParam("txnId") Integer txnId) {
		String snoClaim = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			snoClaim = cpdsystemrejeted.getNonComplianceClaimListById(txnId);
			details.put("status", "success");
			details.put("details", snoClaim);
		} catch (Exception e) {
			details.put("status", "fails");
			details.put("status", e.getMessage());
			logger.error("Exception occured in SnoClaimById method of CpdSystemRejectedList" + e.getMessage());
		}
		return details;
	}

	@PostMapping(value = "/snoNonComplianceaction")
	public ResponseEntity<Response> saveClaimRaiseHospita(@RequestBody ClaimLogBean logBean) {
		Response response = null;
		try {
			response = cpdsystemrejeted.saveClaimSNANonComplianceDetails(logBean);
		} catch (Exception e) {
			logger.error("Exception occured in saveClaimRaiseHospita method of CpdSystemRejectedList" + e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/getNonComplianceExtension")
	@ResponseBody
	public ResponseEntity<?> getNonComplianceExtension(@RequestBody NonComplianceBean requestBean) throws Exception {
		List<Object> snoclaimList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			snoclaimList = cpdsystemrejeted.getNonComplianceExtn(requestBean);
			details.put("status", "success");
			details.put("data", snoclaimList);
		} catch (Exception e) {
			details.put("status", "fail");
			details.put("msg", e.getMessage());
			logger.error(
					"Exception occured in getNonComplianceExtension method of CpdSystemRejectedList" + e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/getNonComplianceExtensionview")
	@ResponseBody
	public List<Object> getNonComplianceExtensionview(@RequestParam(value = "action", required = false) Integer action,
			@RequestParam(value = "userid", required = false) Long userid) {
		List<Object> snoclaimList = new ArrayList<Object>();
		try {
			snoclaimList = cpdsystemrejeted.getNonComplianceExtensionview(action, userid);
		} catch (Exception e) {
			logger.error("Exception occured in getNonComplianceExtensionview method of CpdSystemRejectedList" + e);
		}
		return snoclaimList;
	}

	@PostMapping(value = "/saveDateExtension")
	public ResponseEntity<Response> saveDateExtension(@RequestBody DateExtensionBean logBean) {
		Response response = null;
		try {
			response = cpdsystemrejeted.saveNonComplianceDateExtension(logBean);
		} catch (Exception e) {
			logger.error("Exception occured in saveDateExtension method of CpdSystemRejectedList" + e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/getSNASystemRejectedListToSNA")
	@ResponseBody
	public ResponseEntity<?> getSNASystemRejectedListToSNA(@RequestBody CPDApproveRequestBean requestBean)
			throws Exception {
		List<Object> snoclaimList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			snoclaimList = cpdsystemrejeted.getRejetedDataSNAToSNA(requestBean);
			details.put("status", "success");
			details.put("data", snoclaimList);
		} catch (Exception e) {
			details.put("status", "fail");
			details.put("msg", e.getMessage());
			logger.error("Exception occured in getSNASystemRejectedListToSNA method of CpdSystemRejectedList"
					+ e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/getUnprocessedShedule")
	@ResponseBody
	public ResponseEntity<?> getUnprocessedShedule(@RequestBody NonComplianceBean requestBean) throws Exception {
		List<Object> snoclaimList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			snoclaimList = cpdsystemrejeted.getUnprocessedCountData(requestBean);
			details.put("status", "success");
			details.put("data", snoclaimList);
		} catch (Exception e) {
			details.put("status", "fail");
			details.put("msg", e.getMessage());
			logger.error("Exception occured in getUnprocessedShedule method of CpdSystemRejectedList" + e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/runUnprocessedClaim")
	@ResponseBody
	public ResponseEntity<Response> runUnprocessedClaim(@RequestBody NonComplianceBean requestBean) throws Exception {
		Response response = new Response();
		try {
			response = cpdsystemrejeted.RunUnprocessed(requestBean);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			logger.error("Exception occured in runUnprocessedClaim method of CpdSystemRejectedList" + e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/getBulkNonComplianceExtension")
	@ResponseBody
	public ResponseEntity<?> getBulkNonComplianceExtension(@RequestBody NonComplianceBean requestBean)
			throws Exception {
		List<Object> snoclaimList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			snoclaimList = cpdsystemrejeted.getBulkNonComplianceExtn(requestBean);
			details.put("status", "success");
			details.put("data", snoclaimList);
		} catch (Exception e) {
			details.put("status", "fail");
			details.put("msg", e.getMessage());
			logger.error("Exception occured in getBulkNonComplianceExtension method of CpdSystemRejectedList"
					+ e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@PostMapping(value = "/saveBulkDateExtension")
	public ResponseEntity<Response> saveBulkDateNonComplExtension(@RequestBody BulkDateExtensionBean logBean) {
		Response response = null;
		try {
			response = cpdsystemrejeted.saveBulkNonComplianceDateExtension(logBean);
		} catch (Exception e) {
			logger.error("Exception occured in saveBulkDateNonComplExtension method of CpdSystemRejectedList"
					+ e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/getSnawiseunprocessedclaimlist")
	@ResponseBody
	public ResponseEntity<?> getSnawiseunprocessedclaimlist(@RequestBody Snawiseunprocessedbean requestBean)
			throws Exception {
		List<Object> snoclaimList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			snoclaimList = cpdsystemrejeted.getSnawiseunprocessedcountdetails(requestBean);
			details.put("status", "success");
			details.put("data", snoclaimList);
		} catch (Exception e) {
			details.put("status", "fail");
			details.put("msg", e.getMessage());
			logger.error("Exception occured in getSnawiseunprocessedclaimlist method of getSnawiseunprocessedclaimlist"
					+ e.getMessage());
		}
		return ResponseEntity.ok(details);

	}

	@PostMapping(value = "/getSnawiseunprocessedclaimupdatet")
	public ResponseEntity<?> getSnawiseunprocessedclaimupdatet(
			@RequestBody Snawiserununprocessedupdate snawiseunprocessed) {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = cpdsystemrejeted.getSnawiseunprocesseupdatet(snawiseunprocessed);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/getunprocessedsummarydetails")
	@ResponseBody
	public List<Object> getunprocessedsummarydetails(@RequestParam(value = "fromdate", required = false) Date fromdate,
			@RequestParam(value = "todate", required = false) Date todate,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "hospital", required = false) String hospital,
			@RequestParam(value = "flag", required = false) Integer flag) {
		List<Object> snoclaimList = new ArrayList<Object>();
		try {
			snoclaimList = cpdsystemrejeted.getunprocessedsummarydetails(fromdate, todate, state, state, dist, hospital,
					flag);
		} catch (Exception e) {
			logger.error("Exception occured in getUnprocessedShedule method of CpdSystemRejectedList" + e.getMessage());
		}
		return snoclaimList;
	}
}
