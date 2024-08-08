/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.project.bsky.bean.Response;
import com.project.bsky.model.DynamicReportConfiguration;
import com.project.bsky.model.DynamicReportDetailsmodel;
import com.project.bsky.model.TblMeAction;
import com.project.bsky.service.DynamicReportService;

/**
 * @author rajendra.sahoo
 *
 */
@SuppressWarnings("unused")
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")
public class DynamicReportController {

	@Autowired
	private DynamicReportService dynamicreportservice;

	@Autowired
	private Logger logger;

	@PostMapping(value = "/submitdunamicConfiguration")
	@ResponseBody
	public Response SubmitdunamicConfiguration(@RequestBody DynamicReportConfiguration dynamicReport) {
		return dynamicreportservice.SubmitdunamicConfiguration(dynamicReport);
	}

	@GetMapping(value = "/getdynamicconfigurationlist")
	@ResponseBody
	public List<DynamicReportConfiguration> getdynamicconfigurationlist() {
		return dynamicreportservice.getdynamicconfigurationlist();
	}

	@GetMapping(value = "/getdynamicbyid")
	@ResponseBody
	public DynamicReportConfiguration getdynamicbyid(@RequestParam(value = "slno", required = false) Long slno) {
		return dynamicreportservice.getdynamicbyid(slno);
	}

	@PostMapping(value = "/updatedunamicConfiguration")
	@ResponseBody
	public Response updatedunamicConfiguration(@RequestBody DynamicReportConfiguration dynamicReport) {
		return dynamicreportservice.updatedunamicConfiguration(dynamicReport);
	}

	@GetMapping(value = "/getdynamicreport")
	@ResponseBody
	public List<Object> getdynamicreport(@RequestParam(value = "formdate", required = false) String fromdate,
			@RequestParam(value = "toDate", required = false) String todate,
			@RequestParam(value = "trigger", required = false) Integer trigger) {
		return dynamicreportservice.getdynamicreport(fromdate, todate, trigger);
	}

	@GetMapping(value = "/getdynamicreportdetails")
	@ResponseBody
	public Map<String, Object> getdynamicreportdetails(
			@RequestParam(value = "formdate", required = false) String fromdate,
			@RequestParam(value = "toDate", required = false) String todate,
			@RequestParam(value = "flag", required = false) Integer flag,
			@RequestParam(value = "report", required = false) String report,
			@RequestParam(value = "val", required = false) String value) {
		return dynamicreportservice.getdynamicreportdetails(fromdate, todate, flag, report, value);
	}

	@GetMapping(value = "/getdynamicreportsubdetails")
	@ResponseBody
	public Map<String, Object> getdynamicreportsubdetails(
			@RequestParam(value = "formdate", required = false) String fromdate,
			@RequestParam(value = "toDate", required = false) String todate,
			@RequestParam(value = "flag", required = false) Integer flag,
			@RequestParam(value = "report", required = false) String report) {
		return dynamicreportservice.getdynamicreportsubdetails(fromdate, todate, flag, report);
	}

	@GetMapping(value = "/getmeTreatmentHistoryoverpackgae")
	@ResponseBody
	public String getTreatmentHistoryoverpackgae(@RequestParam(required = false, value = "txnId") Long txnId,
			@RequestParam(required = false, value = "urnnumber") String urnnumber,
			@RequestParam(required = false, value = "hospitalcode") String hospitalcode,
			@RequestParam(required = false, value = "caseno") String caseno,
			@RequestParam(required = false, value = "uidreferencenumber") String uidreferencenumber,
			@RequestParam(required = false, value = "userid") Long userid) {
		String treatmetlist = null;
		try {
			treatmetlist = dynamicreportservice.getmeTreatmentHistoryoverpackgae(txnId, urnnumber, hospitalcode, caseno,
					uidreferencenumber, userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return treatmetlist;
	}

	@GetMapping(value = "/sumbitmeremark")
	@ResponseBody
	public Response sumbitmeremark(@RequestParam(required = false, value = "txnId") Long txnId,
			@RequestParam(required = false, value = "remark") String remark,
			@RequestParam(required = false, value = "userid") Long userid,
			@RequestParam(required = false, value = "urnno") String urn,
			@RequestParam(required = false, value = "claimid") Long claimid,
			@RequestParam(required = false, value = "flag") Integer flag,
			@RequestParam(required = false, value = "year") Integer year,
			@RequestParam(required = false, value = "month") Integer month) {
		Response response = new Response();
		try {
			response = dynamicreportservice.sumbitmeremark(txnId, remark, userid, urn, claimid, flag, year, month);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}

	@GetMapping(value = "/getmeactiontakendetails")
	@ResponseBody
	public List<DynamicReportDetailsmodel> getmeactiontakendetails(
			@RequestParam(required = false, value = "userid") Long userid,
			@RequestParam(required = false, value = "fromdate") Date fromdate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "trigger") Integer trigger) {
		List<DynamicReportDetailsmodel> response = new ArrayList<DynamicReportDetailsmodel>();
		try {
			response = dynamicreportservice.getmeactiontakendetails(userid, fromdate, todate, trigger);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@GetMapping(value = "/getmanderemark")
	@ResponseBody
	public Map<String, Object> getmanderemark(@RequestParam(required = false, value = "txnId") Long txnid) {
		Map<String, Object> response = new LinkedHashMap<>();
		TblMeAction actionlog = new TblMeAction();
		try {
			actionlog = dynamicreportservice.getmanderemark(txnid);
			if (actionlog != null) {
				if (actionlog.getRemark() != null) {
					response.put("status", 200);
					response.put("message", actionlog.getRemark());
				} else {
					response.put("status", 400);
					response.put("message", actionlog.getRemark());
				}
				if (actionlog.getAgeremark() != null) {
					String[] s = actionlog.getAgeremark().split(" ");
					response.put("year", actionlog.getAgeremark());
				} else {
					response.put("year", "");
				}
			} else {
				response.put("status", 400);
				response.put("message", "");
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.put("status", 400);
			response.put("message", "");
		}
		List<Object[]> list = dynamicreportservice.getsnoidbytxnid(txnid);
		for (Object[] obj : list) {
			response.put("snaname", obj[1]);
			response.put("snaid", obj[0]);
		}
		response.put("meactionlog", dynamicreportservice.getmeactionlog(txnid));
		return response;
	}

	@GetMapping(value = "/getspecificcaseremarklist")
	@ResponseBody
	public Map<String, Object> getspecificcaseremarklist(
			@RequestParam(required = false, value = "searchby") Integer searchby,
			@RequestParam(required = false, value = "fieldvalue") String fieldvalue,
			@RequestParam(required = false, value = "userId") Long userId) {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			List<Object> list = dynamicreportservice.getspecificcaseremarklist(searchby, fieldvalue, userId);
			if (list == null) {
				response.put("status", 400);
				response.put("message", "Someting Went Wrong");
			} else {
				response.put("status", 200);
				response.put("message", "Api called Successfully");
				response.put("data", list);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.put("status", 400);
			response.put("message", "Something Went Wrong");
		}
		return response;
	}

	@GetMapping(value = "/findAllActiveTrigger")
	@ResponseBody
	public List<DynamicReportConfiguration> findAllActiveTrigger() {
		return dynamicreportservice.findAllActiveTrigger();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping(value = "/getmeabstractreport")
	@ResponseBody
	public ResponseEntity<?> getmeabstractreport(@RequestParam(value = "fromdate", required = false) String formdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "trigger", required = false) Integer trigger) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data", dynamicreportservice.getmeabstractreport(formdate, todate, trigger));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@GetMapping(value = "/getMeGrievancedetails")
	@ResponseBody
	public List<DynamicReportDetailsmodel> getMeGrievancedetails(
			@RequestParam(required = false, value = "snaUserId") Long snaUserId,
			@RequestParam(required = false, value = "fromdate") Date fromdate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "trigger") Integer trigger,
			@RequestParam(required = false, value = "stateCode") String stateCode,
			@RequestParam(required = false, value = "districtCode") String districtCode,
			@RequestParam(required = false, value = "hospitalCode") String hospitalCode) {
		List<DynamicReportDetailsmodel> response = new ArrayList<>();
		try {
			response = dynamicreportservice.getMeTriggerGrievancedetails(snaUserId, fromdate, todate, trigger,
					stateCode, districtCode, hospitalCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@GetMapping(value = "/getdynamicreportforexceldoenload")
	@ResponseBody
	public ResponseEntity<?> getdynamicreportforexceldownload(@RequestParam(value = "fromdate", required = false) String formdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "trigger", required = false) Integer trigger) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details=dynamicreportservice.getdynamicreportforexceldownload(formdate, todate, trigger);
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}
}
