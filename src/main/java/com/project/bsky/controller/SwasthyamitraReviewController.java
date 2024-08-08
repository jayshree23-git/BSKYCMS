package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.service.SwasthyamitraReviewService;
/**
 * Rajendra Sahoo
 */
@RestController
@RequestMapping(value = "/api")
public class SwasthyamitraReviewController {
	
	@Autowired
	private SwasthyamitraReviewService reviewservice;

	@GetMapping(value = "/getsmhelpdeskregister")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> getsmhelpdeskregister(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "todate", required = false) Date todate,
			@RequestParam(value = "state", required = false) String statecode,
			@RequestParam(value = "dist", required = false) String distcode,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "smaid", required = false) Long smid,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "userId", required = false) Long userId) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data",reviewservice.getsmhelpdeskregister(formdate,todate,statecode,distcode,hospitalCode,smid,status,userId));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			e.printStackTrace();
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/downloadsmreviewdoc")
	public String downloadsmreviewdoc(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		String hospitalcode = json.getString("h");
		try {
			if (fileName == null || fileName == "" || fileName.equalsIgnoreCase("")) {
				resp = "Document not found";
			} else {
				String year = fileName.substring(6, 10);
				reviewservice.downloadsmreviewdoc(fileName, year,hospitalcode,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	@GetMapping(value = "/getsmpendingreport")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> getsmpendingreport(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "todate", required = false) Date todate,
			@RequestParam(value = "state", required = false) String statecode,
			@RequestParam(value = "dist", required = false) String distcode,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "userId", required = false) Long userId) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data",reviewservice.getsmpendingreport(formdate,todate,statecode,distcode,hospitalCode,userId));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			e.printStackTrace();
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getsmlistforscoring")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> getsmlistforscoring(@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "state", required = false) String statecode,
			@RequestParam(value = "dist", required = false) String distcode,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "smaid", required = false) Long smid,
			@RequestParam(value = "userId", required = false) Long userId) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data",reviewservice.getsmlistforscoring(year,month,statecode,distcode,hospitalCode,smid,userId));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			e.printStackTrace();
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getsmdetailsforscoring")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> getsmdetailsforscoring(@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "month", required = false) String month,
			@RequestParam(value = "smid", required = false) Long smid) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details=reviewservice.getsmdetailsforscoring(smid,year,month);
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			e.printStackTrace();
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}

	@GetMapping(value = "/submitsmscore")
	@ResponseBody
	public ResponseEntity<Response> getsmdetailsforscoring(@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "smid", required = false) Long smid,
			@RequestParam(value = "remark", required = false) String remark,
			@RequestParam(value = "score", required = false) Integer score,
			@RequestParam(value = "userId", required = false) Long userId) {
		Response details = new Response();
		try {
			details=reviewservice.submitsmscore(smid,year,month,remark,score,userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getsmscoreview")
	@ResponseBody
	public ResponseEntity<Map<String,Object>> getsmscoreview(@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "userId", required = false) Long userId) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data",reviewservice.getsmscoreview(year,month,userId));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			e.printStackTrace();
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getsmscoringreport")
	@ResponseBody
	public ResponseEntity<Response> getsmscoringreport(@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "state", required = false) String statecode,
			@RequestParam(value = "dist", required = false) String distcode,
			@RequestParam(value = "hospital", required = false) String hospitalCode,
			@RequestParam(value = "smid", required = false) Long smid) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data",reviewservice.getsmscoringreport(year,month,userId,statecode,distcode,hospitalCode,smid));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			e.printStackTrace();
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}
	
	@GetMapping(value = "/getsmfinalincenivereport")
	@ResponseBody
	public ResponseEntity<Response> getsmfinalincenivereport(@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "state", required = false) String statecode,
			@RequestParam(value = "dist", required = false) String distcode,
			@RequestParam(value = "hospital", required = false) String hospitalCode,
			@RequestParam(value = "smid", required = false) Long smid) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data",reviewservice.getsmfinalincenivereport(year,month,userId,statecode,distcode,hospitalCode,smid));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			e.printStackTrace();
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}
}
