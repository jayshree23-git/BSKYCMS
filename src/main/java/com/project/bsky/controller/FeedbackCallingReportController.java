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

import com.project.bsky.service.FeedbackCallingReportService;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")
public class FeedbackCallingReportController {

	@Autowired
	private FeedbackCallingReportService feedbackCallingReportService;
	
	@Autowired
	private Logger logger;
	
	@GetMapping(value = "/getfeedbackcallingreport")
    @ResponseBody
    public List<Object> getFeedbackCallingReport(
    		   @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "formDate", required = false) String formDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "hospitalCode", required = false) String hospitalCode
           ) {
        ////System.out.println("abc");
        List<Object> getFeedbackCallingReport = null;
        try {
        	getFeedbackCallingReport = feedbackCallingReportService.getFeedbackCallingReport(userId,formDate, toDate,action,hospitalCode);
        } catch (Exception e) {
        	logger.error(ExceptionUtils.getStackTrace(e));
        }
        return getFeedbackCallingReport;
    }

}
