/**
 * 
 */
package com.project.bsky.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.bsky.bean.HospitalGroupAuthBean;
import com.project.bsky.bean.HospitalGroupBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.UserDetailsProfile;
import com.project.bsky.serviceImpl.HospitalAuthServiceImpl;

/**
 * @author arabinda.guin
 *
 */
@Controller
@RequestMapping(value = "/master")
public class HospitalGroupController {

	@Autowired
	private HospitalAuthServiceImpl service;
	
	@Autowired
	private Logger logger;
	
	@GetMapping("/getHospitalAuthDetails")
	@ResponseBody
	public List<UserDetailsProfile> getAuthorityDetails() {
		return service.getHospitalAuthDetails();
	}
	
	@GetMapping("/getHospitalUserDetails")
	@ResponseBody
	public List<HospitalInformation> getUserDetails() {
		return service.getHospitalUserDetails();
	}
	@PostMapping("/checkAuthAssignedToHosp")
	@ResponseBody
	public ResponseEntity<Response> checkSNOAssignedToHosp(@RequestBody HospitalGroupBean hospitalGroupBean, Response response) {
		boolean CPDhospiltalDuplicacy = false;
		
		Integer checkHospital = 0;
		String h = null;
		Integer s = null;

		try {
			h = hospitalGroupBean.getHosCode();
			checkHospital = service.checkHospitalName(h);
			
			if (checkHospital > 0) {
				CPDhospiltalDuplicacy = true;
			}
			if (CPDhospiltalDuplicacy == true) {
				response.setMessage("Authority already assigned to "+hospitalGroupBean.getHosName());
				response.setStatus("Info");
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);

	}

	/**
	 *
	 * @param hospitalGroupBean
	 * @param response
	 * @return
	 * save API for Authority tagging hospital
	 */
	@ResponseBody
	@PostMapping("/saveHospitalConfiguration")
	public ResponseEntity<Response> saveCPDConfiguration(@RequestBody HospitalGroupAuthBean hospitalGroupBean,
			Response response) {
		
		
		try {

			response = service.saveConfiguration(hospitalGroupBean);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);

	}
	@GetMapping("/getHospitalAuthorityList")
	@ResponseBody
	private String getHospitalAuth(@RequestParam(value = "userId",required = false) Long userId){
		
		String listData=null;
		try {
			listData = service.getHospitalAuthData(userId);
			 return listData;
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		 return listData;
	}
	@GetMapping(value = "/getAuthById")
	@ResponseBody
	public String getConfigurationDetailsById(
			@RequestParam(required = false, value = "UserId") Integer userId) {
		return service.getConfigurationDetailsById(userId);

	}

	@ResponseBody
	@GetMapping(value = "/getadminclaimTracking")
	public List<Object> getadminclaimTracking(

			@RequestParam(value = "bskyUserId", required = false) Integer bskyUserId,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "urn", required = false) String urn) throws Exception {
		
		List<Object> getclaimtracking = null;
		try {
			getclaimtracking = service.getadminclaimTracking( bskyUserId, hospitalCode,fromDate, toDate,urn);
			
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getclaimtracking;

	}
	/**
	 *
	 * @param hospitalGroupBean
	 * @param response
	 * @return
	 * update API for Authority tagging hospital
	 */
	@PostMapping("/updateHospitalConfiguration")
	public ResponseEntity<Response> updateHospitalConfiguration(@RequestBody HospitalGroupAuthBean hospitalGroupBean, Response response) {
		try {
			
			response = service.updateConfiguration(hospitalGroupBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

}
