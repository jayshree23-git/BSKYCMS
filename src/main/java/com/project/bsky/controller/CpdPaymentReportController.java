package com.project.bsky.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.CpdPaymentReportBean;
import com.project.bsky.bean.PostPaymentRequest;
import com.project.bsky.bean.Response;
import com.project.bsky.model.CpdPaymentReportModel;
import com.project.bsky.service.CpdPaymentReportService;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class CpdPaymentReportController {

	@Autowired
	private CpdPaymentReportService cpdpaymentreportservice;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getNote")
	public CpdPaymentReportModel getQueryTypeList() {
		CpdPaymentReportModel Cpdpaymentreport = new CpdPaymentReportModel();
		try {
			Cpdpaymentreport = cpdpaymentreportservice.getNote();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return Cpdpaymentreport;
	}

	@GetMapping(value = "/getcpdpaymentdetails")
	public List<CpdPaymentReportBean> cpdpayment(@RequestParam(required = false, value = "userid") Long userid,
			@RequestParam(required = false, value = "actiontype") Long actiontype,
			@RequestParam(required = false, value = "year") String year,
			@RequestParam(required = false, value = "month") String month,
			@RequestParam(required = false, value = "hospitalcode") String hospitalcode,
			@RequestParam(required = false, value = "statecode") String statecode,
			@RequestParam(required = false, value = "districtcode") String districtcode,
			@RequestParam(required = false, value = "flag") Long flag) {
		return cpdpaymentreportservice.details(userid, actiontype, year, month, hospitalcode, statecode, districtcode,
				flag);
	}

	@GetMapping(value = "/getCPDUserList")
	public ResponseEntity<?> getCPDUserList() {
		return new ResponseEntity<>(cpdpaymentreportservice.getCPDUserList(), org.springframework.http.HttpStatus.OK);
	}

	@GetMapping(value = "/getCPDPaymentCalculationList")
	public ResponseEntity<?> getCPDPaymentCalculationList(@RequestParam(value = "fromDate") java.util.Date fromDate,
			@RequestParam(value = "toDate") java.util.Date toDate,
			@RequestParam(value = "cpdUserId", required = false) Long cpdUserId) {
		try {
			return new ResponseEntity<>(
					cpdpaymentreportservice.getCPDPaymentCalculationList(fromDate, toDate, cpdUserId),
					org.springframework.http.HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/getCPDPaymentDetailsUserId")
	public ResponseEntity<?> getCPDPaymentDetailsUserId(@RequestParam(value = "fromDate") java.util.Date fromDate,
			@RequestParam(value = "toDate") Date toDate, @RequestParam(value = "userId") Long cpdUserId,
			@RequestParam(value = "actionCode") Integer actionCode) {
		try {
			return new ResponseEntity<>(
					cpdpaymentreportservice.getCPDPaymentDetailsUserId(fromDate, toDate, cpdUserId, actionCode),
					org.springframework.http.HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ResponseBody
	@GetMapping(value = "/getmoratlityDetails")
	public Map<String, Object> getmoratlityDetails(@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "fromdate") Date fromdate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "stateCodeList") String stateCodeList,
			@RequestParam(required = false, value = "districtCodeList") String districtCodeList,
			@RequestParam(required = false, value = "hospitalCodeList") String hospitalCodeList) {
		List<Object> mortalylist = null;
		Map<String, Object> records = new HashMap<String, Object>();
		try {
			mortalylist = cpdpaymentreportservice.getmoratlityDetailsdata(userId, fromdate, todate, stateCodeList,
					districtCodeList, hospitalCodeList);
			records.put("status", "success");
			records.put("details", mortalylist);
		} catch (Exception e) {
			logger.error("Exception Occurred in getmoratlityDetails Method of CpdPaymentReportController : "
					+ e.getMessage());
			records.put("status", "fails");
			records.put("status", e.getMessage());
		}
		return records;
	}

	@ResponseBody
	@GetMapping(value = "/getUrnWisePaymentUtilizeListForBlock")
	public Map<String, Object> getUrnWisePaymentUtilizeListForBlock(
			@RequestParam(required = false, value = "distrctCode") Long distrctCode,
			@RequestParam(required = false, value = "userid") Long userid,
			@RequestParam(required = false, value = "fromDate") Date fromDate,
			@RequestParam(required = false, value = "toDate") Date toDate) {
		List<Object> blocklist = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			blocklist = cpdpaymentreportservice.getUrnWiseBlockDateData(distrctCode, userid, fromDate, toDate);
			details.put("status", "success");
			details.put("details", blocklist);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getUrnWisePaymentUtilizeListForBlock Method of CpdPaymentReportController : "
							+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@ResponseBody
	@GetMapping(value = "/getUrnWisePaymentUtilizeListForGp")
	public Map<String, Object> getUrnWisePaymentUtilizeListForGp(
			@RequestParam(required = false, value = "districtcode") Long districtcode,
			@RequestParam(required = false, value = "blockcode") Long blockcode,
			@RequestParam(required = false, value = "fromDate") Date fromDate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "userid") Long userid) {
		List<Object> gplist = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			gplist = cpdpaymentreportservice.getUrnWiseGpDateData(districtcode, blockcode, fromDate, todate, userid);
			details.put("status", "success");
			details.put("details", gplist);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getUrnWisePaymentUtilizeListForGp Method of CpdPaymentReportController : "
							+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@ResponseBody
	@GetMapping(value = "/getUrnWisePaymentUtilizeListForVillage")
	public Map<String, Object> getUrnWisePaymentUtilizeListForVillage(
			@RequestParam(required = false, value = "districtcode") Long districtcode,
			@RequestParam(required = false, value = "blockcode") Long blockcode,
			@RequestParam(required = false, value = "gpcode") Long gpcode,
			@RequestParam(required = false, value = "fromDate") Date fromDate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "userid") Long userid) {
		List<Object> villagelist = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			villagelist = cpdpaymentreportservice.getUrnWisevillageData(districtcode, blockcode, gpcode, fromDate,
					todate, userid);
			details.put("status", "success");
			details.put("details", villagelist);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getUrnWisePaymentUtilizeListForVillage Method of CpdPaymentReportController : "
							+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@ResponseBody
	@GetMapping(value = "/getPatienttreatedinoutsideodishareportforblock")
	public Map<String, Object> getPatienttreatedinoutsideodishareportforblock(
			@RequestParam(required = false, value = "districtcode") Long districtcode,
			@RequestParam(required = false, value = "fromDate") Date fromDate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "userId") Long userId) {
		List<Object> outsideblocklist = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			outsideblocklist = cpdpaymentreportservice.getPatienttreatedinoutsideodishareportforblockData(districtcode,
					fromDate, todate, userId);
			details.put("status", "success");
			details.put("details", outsideblocklist);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getPatienttreatedinoutsideodishareportforblock Method of CpdPaymentReportController : "
							+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@ResponseBody
	@GetMapping(value = "/getPatienttreatedinoutsideodishareportforPanchayat")
	public Map<String, Object> getPatienttreatedinoutsideodishareportforPanchayat(
			@RequestParam(required = false, value = "districtcode") Long districtcode,
			@RequestParam(required = false, value = "blockcode") Long blockcode,
			@RequestParam(required = false, value = "fromDate") Date fromDate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "userId") Long userId) {
		List<Object> outsidepanchayatlist = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			outsidepanchayatlist = cpdpaymentreportservice.getPatienttreatedinoutsideodishareportforPanchayatkData(
					districtcode, blockcode, fromDate, todate, userId);
			details.put("status", "success");
			details.put("details", outsidepanchayatlist);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getPatienttreatedinoutsideodishareportforPanchayat Method of CpdPaymentReportController : "
							+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@ResponseBody
	@GetMapping(value = "/getPatienttreatedinoutsideodishareportforVillage")
	public Map<String, Object> getPatienttreatedinoutsideodishareportforVillage(
			@RequestParam(required = false, value = "districtcode") Long districtcode,
			@RequestParam(required = false, value = "blockcode") Long blockcode,
			@RequestParam(required = false, value = "fromDate") Date fromDate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "gpcode") Long gpcode) {
		List<Object> outsidevillagelist = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			outsidevillagelist = cpdpaymentreportservice.getPatienttreatedinoutsideodishareportforVillageData(
					districtcode, blockcode, fromDate, todate, userId, gpcode);
			details.put("status", "success");
			details.put("details", outsidevillagelist);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getPatienttreatedinoutsideodishareportforVillage Method of CpdPaymentReportController : "
							+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@ResponseBody
	@GetMapping(value = "/getCPDProcessingPaymentDetails")
	public Map<String, Object> getCPDProcessingPaymentDetails(
			@RequestParam(required = false, value = "userId") Long userId,
			@RequestParam(required = false, value = "year") Long year,
			@RequestParam(required = false, value = "month") Long month,
			@RequestParam(required = false, value = "hospitalcode") String hospitalcode,
			@RequestParam(required = false, value = "statecode") String statecode,
			@RequestParam(required = false, value = "districtcode") String districtcode,
			@RequestParam(required = false, value = "status") String status) {
		List<Object> cpdpayinnerlist = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			cpdpayinnerlist = cpdpaymentreportservice.getgetCPDProcessingPaymentDetailsData(userId, year, month,
					hospitalcode, statecode, districtcode, status);
			details.put("status", "success");
			details.put("details", cpdpayinnerlist);
		} catch (Exception e) {
			logger.error("Exception Occurred in getCPDProcessingPaymentDetails Method of CpdPaymentReportController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@ResponseBody
	@GetMapping(value = "/getpackagedetailsData")
	public Map<String, Object> getpackagedetailsData() {
		List<Object> pack = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			pack = cpdpaymentreportservice.getpackagedetailsDetails();
			details.put("status", "success");
			details.put("details", pack);
		} catch (Exception e) {
			logger.error("Exception Occurred in getpackagedetailsData Method of CpdPaymentReportController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@ResponseBody
	@GetMapping(value = "/getpackagedetailsForCalculation")
	public Map<String, Object> getpackagedetailsForCalculation(
			@RequestParam(required = false, value = "packlist") String packlist,
			@RequestParam(required = false, value = "userid") String userid,
			@RequestParam(required = false, value = "statedata") String statedata,
			@RequestParam(required = false, value = "districtdata") String districtdata,
			@RequestParam(required = false, value = "hospitaldata") String hospitaldata,
			@RequestParam(required = false, value = "hospitaltype") String hospitaltype) {
		List<Object> pack = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			pack = cpdpaymentreportservice.getpackagedetailsRecordList(packlist, userid, statedata, districtdata,
					hospitaldata, hospitaltype);
			details.put("status", "success");
			details.put("details", pack);
		} catch (Exception e) {
			logger.error("Exception Occurred in getpackagedetailsData Method of CpdPaymentReportController : "
					+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@ResponseBody
	@GetMapping(value = "/getpackagedetailsForImpantCalculation")
	public Map<String, Object> getpackagedetailsForImpantCalculation(
			@RequestParam(required = false, value = "userid") String userid,
			@RequestParam(required = false, value = "procedurecode") String procedurecode,
			@RequestParam(required = false, value = "statedata") String statedata,
			@RequestParam(required = false, value = "districtdata") String districtdata,
			@RequestParam(required = false, value = "hospitaldata") String hospitaldata,
			@RequestParam(required = false, value = "hospitaltype") String hospitaltype) {
		List<Object> implant = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			implant = cpdpaymentreportservice.getpackagedetailsForImpantCalculationList(userid, procedurecode,
					statedata, districtdata, hospitaldata, hospitaltype);
			details.put("status", "success");
			details.put("details", implant);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getpackagedetailsForImpantCalculation Method of CpdPaymentReportController : "
							+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@ResponseBody
	@GetMapping(value = "/getpackagedetailsForHedCalculation")
	public Map<String, Object> getpackagedetailsForHedCalculation() {
		List<Object> hedlist = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			hedlist = cpdpaymentreportservice.getpackagedetailsForHedCalculationList();
			details.put("status", "success");
			details.put("details", hedlist);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getpackagedetailsForHedCalculation Method of CpdPaymentReportController : "
							+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@ResponseBody
	@GetMapping(value = "/getpaymentcpdlist")
	public Map<String, Object> getCPDPaymentList(@RequestParam(required = false, value = "month") Long month,
			@RequestParam(required = false, value = "year") Long year) {
		String hedlist = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			hedlist = cpdpaymentreportservice.getCPDPaymentList(month, year);
			details.put("status", "success");
			details.put("details", hedlist);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getpackagedetailsForHedCalculation Method of CpdPaymentReportController : "
							+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@PostMapping(value = "/cpdpostpaymentupdation")
	public ResponseEntity<?> cpdUpdatePostPayment(@RequestBody PostPaymentRequest paymentRequest) {
		Response response = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = cpdpaymentreportservice.updatePostPayment(paymentRequest);
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
	@GetMapping(value = "/cpdpostpaymentupdationview")
	public Map<String, Object> cpdPostPaymentUpdationView(@RequestParam(required = false, value = "cpdUserId") Long cpdUserId,
			@RequestParam(required = false, value = "year") Long year) {
		String hedlist = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			hedlist = cpdpaymentreportservice.cpdPostPaymentUpdationView(cpdUserId, year);
			details.put("status", "success");
			details.put("details", hedlist);
		} catch (Exception e) {
			logger.error(
					"Exception Occurred in getpackagedetailsForHedCalculation Method of CpdPaymentReportController : "
							+ e.getMessage());
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}
}
