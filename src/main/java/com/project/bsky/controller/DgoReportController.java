package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.DgoReportService;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")

public class DgoReportController {

	@Autowired
	private DgoReportService dgoReportService;
	
	@Autowired
	private Logger logger;
	
	@GetMapping(value = "/getdgoCallCenterData")
    @ResponseBody
    
    
    public  ResponseEntity<?> getDgoCallCenterData(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "formDate", required = false) String formDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "district", required = false) String district,
            @RequestParam(value = "hospitalCode", required = false) String hospitalCode,
            @RequestParam(value = "cceId", required = false) Long cceId,
            @RequestParam(value = "cceUserId", required = false) Integer cceUserId,
            @RequestParam(value = "pageIn", required = false) Integer pageIn,
			@RequestParam(value = "pageEnd", required = false) Integer pageEnd) {
        ////System.out.println(userId);
        Map<Long, List<Object>> map = new HashMap<Long, List<Object>>();
		List<Object> getDgoCallCenterData = new ArrayList<Object>();
		Map<String, Object> json = new HashMap<String, Object>();
       // List<Object> getDgoCallCenterData = null;
        try {
        	map = dgoReportService.getDgoCallCenterData(userId, formDate, toDate,action,state,district,hospitalCode,cceId,cceUserId,pageIn,pageEnd);
        	for (Map.Entry<Long, List<Object>> entry : map.entrySet()) {
				json.put("size", entry.getKey());
				getDgoCallCenterData = entry.getValue();
				json.put("list", getDgoCallCenterData);
			}
			////System.out.println(json);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(json);
	}
}
