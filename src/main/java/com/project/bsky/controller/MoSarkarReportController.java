/**
 * 
 */
package com.project.bsky.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.bsky.repository.MosarkarRepository;

/**
 * 
 */
@Controller
@RequestMapping(value = "/api")
public class MoSarkarReportController {

	private final Logger logger;

	@Autowired
	public MoSarkarReportController(Logger logger) {
		this.logger = logger;
	}
	@Autowired
	private MosarkarRepository mosarkarepository;

	@GetMapping(value = "/getAlldetailsmosarkar")
	@ResponseBody
	public Map<String, String> getAlldetailsmosarkar(@RequestParam("fromDate") Date fromDate,
			@RequestParam("toDate") Date toDate,
			@RequestParam("serachtype") Integer serachtype) throws Exception {
		//logger.info("Inside getAlldetailsmosarkar Method of MoSarkarReportController");
		String mosarkardetails = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			mosarkardetails = mosarkarepository.getalldetailsinmosarkar(fromDate,toDate,serachtype);
			details.put("status", "success");
			details.put("details", mosarkardetails);
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("Exception Occurred in getAlldetailsmosarkar Method of MoSarkarReportController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return details;
	}
}
