/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.SwasthyaMitraHospitalConfigurationBean;
import com.project.bsky.model.UserDetails;
import com.project.bsky.service.SwasthyaMitraHospitalConfigurationService;

/**
 * @author priyanka.singh
 *
 */
@RestController
@RequestMapping(value = "/api")
public class SwasthyaMitraHospitalConfigurationController {

	@Autowired
	private SwasthyaMitraHospitalConfigurationService swasthyaMitraHospitalConfigurationService;
	
	@Autowired
	private Logger logger;

	@GetMapping(value = "/getSwasthyaMitraList")
	public List<UserDetails> getAllUserDetails() {
		List<UserDetails> details = swasthyaMitraHospitalConfigurationService.getSwasthyaMitra();
		return details;
	}

	@ResponseBody
	@PostMapping(value = "/saveSwasthyaMitraMapping")
	public Response saveSwasthyaData(
			@RequestBody SwasthyaMitraHospitalConfigurationBean swasthyaMitraHospitalConfigurationBean) {
		Response returnObj = null;
		try {
			returnObj = swasthyaMitraHospitalConfigurationService
					.saveSwasthyaMitraDetails(swasthyaMitraHospitalConfigurationBean);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}
	
	

	@GetMapping(value = "/getSwasthyaMitraDetails")
	public List<SwasthyaMitraHospitalConfigurationBean> getSwasthyaMitraDetails() {
		List<SwasthyaMitraHospitalConfigurationBean> mitradetails = null;
		try {
			mitradetails = swasthyaMitraHospitalConfigurationService.getDetails();
			return mitradetails;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return mitradetails;
	}

	@PostMapping(value = "/updateSwasthya")
	public Response updateSwasthya(
			@RequestBody SwasthyaMitraHospitalConfigurationBean swasthyaMitraHospitalConfigurationBean) {
		Response returnObj = null;
		try {
			returnObj = swasthyaMitraHospitalConfigurationService
					.updateSwasthyaMitra(swasthyaMitraHospitalConfigurationBean);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

}
