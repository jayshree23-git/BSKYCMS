package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.CceReportService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class CceReportController {
	 @Autowired
	    private CceReportService cceReportService ;
	 
	 @Autowired
	 private Logger logger;

	    @GetMapping(value = "/getCceReport")
	    @ResponseBody
	    public List<Object> getCceReport(
	    		@RequestParam(value = "userId", required = false)String userId,
	            @RequestParam(value = "formDate", required = false) String formDate,
	            @RequestParam(value = "toDate", required = false) String toDate,
	            @RequestParam(value = "action", required = false) String action,
	            @RequestParam(value = "hospitalCode", required = false) String hospitalCode) {
	        //System.out.println("CceReport");
	        List<Object> getCceReport = null;
	        try {
	        	getCceReport = cceReportService.getCceReport(userId,formDate,toDate,action,hospitalCode);
	        } catch (Exception e) {
	        	logger.error(ExceptionUtils.getStackTrace(e));
	        }
	        return getCceReport;
	    }
	   
	    
	    @GetMapping(value="/getccetotalcountedDetails")
		public List<Object> getCceTotalCountedDetails(
				@RequestParam(value = "userId", required = false)String userId,
				@RequestParam(value = "fromDate", required = false) String fromDate,
				@RequestParam(value = "toDate", required = false) String toDate,
				@RequestParam(value = "action", required = false) String action,
				@RequestParam(value = "hospitalCode", required = false) String hospitalCode)
		
		{
//			////System.out.println("Data come from frontend+++++ "+","+admissionFromDate+","+admissionToDate+","
//					+ ""+hospitalName+","+hospitalCode+","+hospitalCode+","+admission+","+blocking+","+unblocking+","+discharge+","
//					+preauth);
			List<Object> gethospitalAllReport = null;
			try {
				gethospitalAllReport = cceReportService.getCceTotalCountedDetails( userId,fromDate, toDate,action,hospitalCode);
				////System.out.println(gethospitalAllReport);
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
			return gethospitalAllReport;

		}	
			

	}


