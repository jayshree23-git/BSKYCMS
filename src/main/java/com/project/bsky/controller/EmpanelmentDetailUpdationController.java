package com.project.bsky.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.EmpanelBean;
import com.project.bsky.bean.Response;
import com.project.bsky.config.dto.HospLogin;
import com.project.bsky.service.EmpanelmentDetailUpdationService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class EmpanelmentDetailUpdationController {
	
	
	@Autowired
	private EmpanelmentDetailUpdationService empanelmentDetailUpdationService;
	
	@Autowired
	private Logger logger;
	
	
	@ResponseBody
	@GetMapping(value="/getHospListfordetailUpdation")
	public List<Object> getHospitalList(@RequestParam(value = "hospitalCode", required = false) String hospitalCode){
		List<Object> hospitalList=new ArrayList<Object>();
		try {
			//System.out.println(hospitalCode);
			hospitalList=empanelmentDetailUpdationService.hospList(hospitalCode);
			return hospitalList;
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalList;	
	}
	
	
	@ResponseBody
	@GetMapping(value="/getApplicantProfileDuplicate")
	public ResponseEntity<Response> getHospitalList(@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "profileId", required = false) String profileId){
		Response response = null;
		try {
			//System.out.println(profileId);
			response = empanelmentDetailUpdationService.checkDuplicateMobile(mobile,profileId);
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}
	
	
	
	@PostMapping(value = "/updateEmpanelDetails")
	public ResponseEntity<Response> snoactionofOldReClaim(@RequestBody EmpanelBean logBean) {
		////System.out.println("Inside Save Sno User Data------------>>");
		Response response = null;
		try {
			//System.out.println(logBean.toString());
			response = empanelmentDetailUpdationService.updateEmpanelHospitalData(logBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}
	@PostMapping(value = "/sendOtpForEmpanel")
	@Transactional
	public ResponseEntity<?> hospLogin(@RequestBody HospLogin hospLogin) throws JSONException, UnsupportedEncodingException {
		//System.out.println(hospLogin.getMobile());
		JSONObject response = new JSONObject();
		try {
			response = empanelmentDetailUpdationService.sendOtpForEMP(hospLogin.getMobile());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping(value = "/verifyOtpForEmpanel")
	@Transactional
	public ResponseEntity<?> VerifyOtp(@RequestBody HospLogin hospLogin) throws JSONException, UnsupportedEncodingException {
		//System.out.println(hospLogin.toString());
		JSONObject response = new JSONObject();
		try {
			response = empanelmentDetailUpdationService.validateOtpForEmp(hospLogin.getMobile(),hospLogin.getOtp());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response.toString());
	}

}
