package com.project.bsky.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.RunCPDScheduleService;

@RestController
@RequestMapping(value = "/api")
public class RunCPDScheduleController {
	
	
	@Autowired
	private RunCPDScheduleService runCPDScheduleService;
	
	@Autowired
	private Logger logger;
	
	@GetMapping("/getTotalCasetobeAssign")
	public Integer getTotalCasetobeAssign(){
		Integer tobeAssigncpd = null;
		try {
			tobeAssigncpd = runCPDScheduleService.getTotalCasetobeAssign();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return tobeAssigncpd;
	}
	


	
	@GetMapping("/runcpdScheduleFreshClaim")
	public void runcdpscheduleFreshClaim(){
		try {
			runCPDScheduleService.runcdpscheduleFreshClaim();

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
	
	@GetMapping("/runcpdScheduleDishonored")
	public void runcpdScheduleDishonored(){
		try {
			runCPDScheduleService.runcpdScheduleDishonored();

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}
}
