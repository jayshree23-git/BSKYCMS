/**
 * 
 */
package com.project.bsky.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.TxnclamFloateDetails;
import com.project.bsky.service.Adminfloatereportservice;
import com.project.bsky.service.FinancialOfficerService;

/**
 * @author rajendra.sahoo
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class Adminfloatereport {

	@Autowired
	private Adminfloatereportservice adminfloatereport;

	@Autowired
	private FinancialOfficerService financilaservice;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getfloatedetails")
	@ResponseBody
	public List<Object> getfloatedetails(@RequestParam(value = "floateno", required = false) String floateno) {
		return financilaservice.getUSerDetailsDAta(floateno);
	}

	@GetMapping(value = "/snaFloatrevertData")
	@ResponseBody
	public ResponseEntity<?> snaFloatrevertData(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "toDate", required = false) Date todate,
			@RequestParam(value = "userId", required = false) Long userid) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data", adminfloatereport.snaFloatrevertData(formdate, todate, userid));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@GetMapping(value = "/snaFloatDataforrevert")
	@ResponseBody
	public ResponseEntity<?> snaFloatDataforrevert(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "toDate", required = false) Date todate,
			@RequestParam(value = "userId", required = false) Long userid,
			@RequestParam(value = "floateno", required = false) String floateno) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data", adminfloatereport.snaFloatDataforrevert(formdate, todate, userid, floateno));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@GetMapping(value = "/snaFloatrevert")
	@ResponseBody
	public Response snaFloatrevert(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "toDate", required = false) Date todate,
			@RequestParam(value = "userId", required = false) Long userid,
			@RequestParam(value = "floateno", required = false) String floateno) {
		Response response = new Response();
		try {
			response = adminfloatereport.snaFloatrevert(formdate, todate, userid, floateno);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("00");
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@GetMapping(value = "/getsnafreezelist")
	@ResponseBody
	public ResponseEntity<?> getsnafreezelist(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "toDate", required = false) Date todate,
			@RequestParam(value = "userId", required = false) Long userid,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "hospital", required = false) String hospital) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data", adminfloatereport.getsnafreezelist(formdate, todate, userid,state,dist,hospital));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@GetMapping(value = "/applyforunfreeze")
	@ResponseBody
	public ResponseEntity<?> applyforunfreeze(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "toDate", required = false) Date todate,
			@RequestParam(value = "userId", required = false) Long userid,
			@RequestParam(value = "claimid", required = false) String claimid) {
		Response details = new Response();
		try {
			details = adminfloatereport.applyforunfreeze(formdate, todate, userid, claimid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.setStatus("400");
			details.setMessage(e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@GetMapping(value = "/getsnafreezelistforapprove")
	@ResponseBody
	public ResponseEntity<?> getsnafreezelistforapprove(@RequestParam(value = "fromdate", required = false) Date fromdate,
			@RequestParam(value = "todate", required = false) Date todate,
			@RequestParam(value = "snaid", required = false) Long snaid,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "hospital", required = false) String hospital) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data", adminfloatereport.getsnafreezelistforapprove(fromdate,todate,snaid,state,dist,hospital));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@GetMapping(value = "/approveforunfreeze")
	@ResponseBody
	public ResponseEntity<?> approveforunfreeze(@RequestParam(value = "userId", required = false) Long userid,
			@RequestParam(value = "claimid", required = false) String claimid) {
		Response details = new Response();
		try {
			details = adminfloatereport.approveforunfreeze(userid, claimid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.setStatus("400");
			details.setMessage(e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@GetMapping(value = "/getspecialfloatereport")
	@ResponseBody
	public ResponseEntity<?> getspecialfloatereport(@RequestParam(value = "todate", required = false) Date todate,
			@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "snaid", required = false) Long snaid) {
		Map<String, Object> details = new HashMap<>();
		try {
			details.put("data", adminfloatereport.getspecialfloatereport(todate, formdate, userId, snaid));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(e.getMessage());
			details.put("status", 400);
			details.put("message", "Something Went Wrong");
			details.put("error", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}
	
	@GetMapping(value = "/gethospwisependingclaimdetails")
	@ResponseBody
	public ResponseEntity<?> gethospwisependingclaimdetails(@RequestParam(value = "floatNumber", required = false) String floatNumber,
			@RequestParam(value = "hospcode", required = false) String hospcode) {
		Map<String,Object> details = new HashedMap<String, Object>();
		try {
			details.put("data",adminfloatereport.gethospwisependingclaimdetails(floatNumber, hospcode));
			details.put("status",HttpStatus.OK.value());
			details.put("message","success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status",HttpStatus.BAD_REQUEST.value());
			details.put("error",e.getMessage());
			details.put("message","Error");
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}
}
