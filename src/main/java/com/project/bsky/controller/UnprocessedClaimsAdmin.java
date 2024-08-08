/**
 * 
 */
package com.project.bsky.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.Unprocessedservice;

/**
 * @author hrusikesh.mohanty
 *
 */
@RestController
@RequestMapping(value = "/api")
public class UnprocessedClaimsAdmin {

	@Autowired
	private Unprocessedservice unprocessedservice;
	
	@Autowired
	private Logger logger;

	@GetMapping(value = "/seacrchunprocessed")
	@ResponseBody
	public String Unprocessedclaim(@RequestParam(value = "snoid", required = false) Long snoid,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "userId", required = false) Long userId) {
		String unList = null;
		try {
			unList = unprocessedservice.getdetailsunrocessed(snoid, fromdate, todate, userId);
			return unList;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return unList;
	}
}
