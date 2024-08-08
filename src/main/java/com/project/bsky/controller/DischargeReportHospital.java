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

import com.project.bsky.service.DischargeReportHospitalService;

@RestController
@RequestMapping(value = "/api")
public class DischargeReportHospital {
	
	@Autowired
	private DischargeReportHospitalService dischargehospital;
	
	@Autowired
	private Logger logger;
	
//	@ResponseBody
//	@GetMapping(value = "/getDischargereport")
//	public List<Object> getallDataForRejected(
//			@RequestParam(value = "urn", required = false) String  urn,
//			@RequestParam(value = "groupId" ,required = false) String groupId,
//			@RequestParam(value = "hospitalcode" ,required = false) String hospitalcode,
//	        @RequestParam(value = "userID" ,required = false) String userID)
//	{
//		//System.out.println("data comes");
//		List<Object> DisList = null;
//		try {
//			DisList=dischargehospital.getdischargwdetails(urn,groupId,hospitalcode,userID);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return DisList;
//	}

	@ResponseBody
	@GetMapping(value = "/getDischargereport")
	public List<Object> getallDataForRejected(
			@RequestParam(value = "userID", required = false) Long userId,
			@RequestParam(value = "searchBy", required = false) String searchBy,
			@RequestParam(value = "fieldValue", required = false) String fieldValue,
			@RequestParam(value = "groupId" ,required = false) String groupId) throws Exception {
//		//System.out.println(userId + "\t" + searchBy + "\t" + fieldValue);
		
		if(fieldValue!=null && fieldValue!="") {
			fieldValue=fieldValue.trim();
		}
		//System.out.println("data comes");
		List<Object> DisList = null;
		try {
			DisList=dischargehospital.getdischargwdetails(userId,searchBy,fieldValue,groupId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return DisList;

	}

}
