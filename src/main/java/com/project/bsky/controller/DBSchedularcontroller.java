/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
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

import com.project.bsky.bean.Response;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.Mstschedular;
import com.project.bsky.model.Schedulartracker;
import com.project.bsky.service.DBSchedularService;

/**
 * @author rajendra.sahoo
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class DBSchedularcontroller {

	@Autowired
	private DBSchedularService schedularservice;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getschedularreportlist")
	public List<Schedulartracker> getschedularreportlist(
			@RequestParam(value = "procedure", required = false) String procedure,
			@RequestParam(value = "fromdate", required = false) String year,
			@RequestParam(value = "todte", required = false) String month) {
		List<Schedulartracker> list = new ArrayList<Schedulartracker>();
		try {
			list = schedularservice.getdbschedularlist(procedure, month, year);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/getdbschedularlist")
	public List<Mstschedular> getdbschedularlist() {
		List<Mstschedular> list = new ArrayList<Mstschedular>();
		try {
			list = schedularservice.getalldata();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/getschedulardetailslist")
	public Map<String, Object> getschedulardetailslist(@RequestParam(value = "procid", required = false) Integer procid,
			@RequestParam(value = "date", required = false) Date date) {
		Map<String, Object> list = new HashMap<>();
		try {
			list = schedularservice.getschedulardetailslist(procid, date);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@PostMapping(value = "/savescheduler")
	public Response savescheduler(@RequestBody Mstschedular mstscheduler) {
		Response response = new Response();
		try {
			response = schedularservice.savescheduler(mstscheduler);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Some Error Happen");
		}
		return response;
	}

	@ResponseBody
	@GetMapping(value = "/generateotpforscheduler")
	public AuthRequest generateotpforscheduler() {
		AuthRequest list = new AuthRequest();
		try {
			list = schedularservice.generateotpforscheduler();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/validateotpforscheduler")
	public AuthRequest validateotpforscheduler(@RequestParam(value = "otp", required = false) String otp) {
		AuthRequest list = new AuthRequest();
		try {
			list = schedularservice.validateotpforscheduler(otp);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/getallschedulerlist")
	public List<Mstschedular> getallschedulerlist() {
		List<Mstschedular> list = new ArrayList<Mstschedular>();
		try {
			list = schedularservice.getallschedulerlist();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@PostMapping(value = "/updatescheduler")
	public Response updatescheduler(@RequestBody Mstschedular mstscheduler) {
		Response response = new Response();
		try {
			response=schedularservice.updatescheduler(mstscheduler);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("400");
			response.setMessage("Some Error Happen");
		}
		return response;
	}

	@ResponseBody
	@GetMapping(value = "/getschedulerloglist")
	public List<Object> getschedulerloglist(@RequestParam Long scheduler) {
		List<Object> response = new ArrayList<Object>();
		try {
			response = schedularservice.getschedulerloglist(scheduler);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getcpddishonorcountlist")
	public ResponseEntity<Map<String,Object>> getcpddishonorcountlist(@RequestParam(value = "formdate", required = false) Date formdate) {
		Map<String,Object> response=new HashMap<>();
		try {
			response.put("data", schedularservice.getcpddishonorcountlist(formdate));
			response.put("status", 200);
			response.put("message", "API Called Successfully");
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.put("status", 400);
			response.put("message", "Something Went Wrong");
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@ResponseBody
	@GetMapping(value = "/deactivecpddishonour")
	public ResponseEntity<Map<String,Object>> deactivecpddishonour(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "remark", required = false) String remark,
			@RequestParam(value = "cpdid", required = false) String cpdid,
			@RequestParam(value = "userId", required = false) Long createdby) {
		Map<String,Object> response=new HashMap<>();
		try {
			response=schedularservice.deactivecpddishonour(formdate,remark,cpdid,createdby);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.put("status", 400);
			response.put("message", "Something Went Wrong");
			response.put("error", e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
