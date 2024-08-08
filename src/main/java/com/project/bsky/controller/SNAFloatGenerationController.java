package com.project.bsky.controller;

import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.SNAFloatGenerationService;

@RestController
@RequestMapping(value = "/api")
public class SNAFloatGenerationController {
	
	@Autowired
	SNAFloatGenerationService snaFloatGenerationService;
	@Autowired
	private Logger logger;
	
	@GetMapping(value = "/viewSnaFloatList")
	public String viewSnaFloatList(@RequestParam("userId") String userId,@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "finacialno", required = false) String finacialno) {
		String floatList = null;
		try {
			if (Objects.equals(fromDate, "") && Objects.equals(toDate, "")) {
				floatList = snaFloatGenerationService.viewSnaFloatList(null,null,finacialno,Integer.parseInt(userId));
				} else {
					floatList = snaFloatGenerationService.viewSnaFloatList(fromDate,toDate,finacialno,Integer.parseInt(userId));
				}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return floatList;

	}
	
	
	@GetMapping(value = "/viewIndivisualFloatDetails")
	public String viewSnaFloatList(@RequestParam("floatNo") String floatNo) {
		String indivisualFloatDetails = null;
		try {
			
			indivisualFloatDetails = snaFloatGenerationService.viewIndivisualFloatDetails(floatNo);
				

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return indivisualFloatDetails;

	}

}
