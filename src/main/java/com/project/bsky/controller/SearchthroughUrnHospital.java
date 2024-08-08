/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.SearchthroughUrnHospitalService;

/**
 * @author hrusikesh.mohanty
 *
 */
@RestController
@RequestMapping(value = "/api")
public class SearchthroughUrnHospital {
	@Autowired
	private SearchthroughUrnHospitalService SearchthroughUrnHospitalService;
	
	@Autowired
	private Logger logger;
	
	@GetMapping(value = "/searchthroughturn")
	@ResponseBody
	public List<Object> getdatathroughurn(@RequestParam(value = "urn",required = false) String urn) {
		List<Object> claiList = null;
		try {
			claiList= SearchthroughUrnHospitalService.geturn(urn);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claiList;
	}

}
