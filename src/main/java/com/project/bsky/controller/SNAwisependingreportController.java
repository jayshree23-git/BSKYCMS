/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Cpdwiseunprocessedbean;
import com.project.bsky.bean.NonComplianceBean;
import com.project.bsky.service.SNAwisependingreportService;

/**
 * @author rajendra.sahoo
 *
 */
@RestController
@RequestMapping(value = "/api")
public class SNAwisependingreportController {

	@Autowired
	private SNAwisependingreportService snawiseserv;
	
	@Autowired
	private Logger logger;
	
	@PostMapping(value = "/getsnawisependingreport")
	@ResponseBody
	public List<Object> getsnawisependingreport(@RequestBody NonComplianceBean requestBean) throws Exception {
		List<Object> snoclaimList = new ArrayList<Object>();		
		
		try {
			snoclaimList = snawiseserv.getsnawisependingreport(requestBean);			
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return snoclaimList;

	}
	
	@PostMapping(value = "/getsnawisependingreportdetails")
	@ResponseBody
	public List<Object> getsnawisependingreportdetails(@RequestBody NonComplianceBean requestBean) throws Exception {
		List<Object> snoclaimList = new ArrayList<Object>();		
		
		try {
			snoclaimList = snawiseserv.getsnawisependingreportdetails(requestBean);			
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return snoclaimList;

	}
	@PostMapping(value = "/getcpdwiseunprocessed")
	@ResponseBody
	public List<Object> getcpdwisependingreport(@RequestBody Cpdwiseunprocessedbean requestData) throws Exception {
		List<Object> scpdunprocessedist = new ArrayList<Object>();		
		try {
			scpdunprocessedist = snawiseserv.getcpdwiseunprocesseddata(requestData);			
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return scpdunprocessedist;

	}
	@PostMapping(value = "/getcpdwiseunprocesseddetails")
	@ResponseBody
	public List<Object> getscpdwisependingreportdetails(@RequestBody Cpdwiseunprocessedbean requestBean) throws Exception {
		List<Object> cpdunprocessedlistdetails = new ArrayList<Object>();		
	
		try {
			cpdunprocessedlistdetails = snawiseserv.getcpdwisependingreportdetails(requestBean);			
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return cpdunprocessedlistdetails;

	}
	
}
