package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.CDMOConfigurationBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.CDMOConfiguration;
import com.project.bsky.service.CDMOConfigurationService;
@CrossOrigin

@RestController
@RequestMapping(value = "/api")
public class CDMOConfigurationController {
	
	@Autowired
	private CDMOConfigurationService cdmoConfigurationservice;
	
	@Autowired
	private Logger logger;

	
	@ResponseBody
	@PostMapping("/saveCDMOConfiguration")
	public ResponseEntity<Response> saveCPDConfiguration(@RequestBody CDMOConfigurationBean cdmoConfigurationBean,
			Response response) {
		try {
			response = cdmoConfigurationservice.saveCDMOConfiguration(cdmoConfigurationBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);

	}
	@ResponseBody
	@GetMapping(value="/getCdmoData")
	public List<CDMOConfiguration> getQueryTypeList(){
		
		List<CDMOConfiguration> cdmoConfiguration=null;
		try {
			cdmoConfiguration=cdmoConfigurationservice.getDetails();
			return cdmoConfiguration;
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return cdmoConfiguration;	
	}	
	
	@ResponseBody
	@PostMapping("/updatecdmoConfiguration")
	public ResponseEntity<Response> updateCPDConfiguration(@RequestBody CDMOConfigurationBean cdmoConfigurationBean,
			Response response) {
		try {
			response = cdmoConfigurationservice.updateCDMOConfiguration(cdmoConfigurationBean);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);

	}

}
