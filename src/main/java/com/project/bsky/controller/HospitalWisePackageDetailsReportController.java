package com.project.bsky.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.HospitalWisePackageDetailsReportBean;
import com.project.bsky.service.HospitalWisePackageDetailsReportService;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class HospitalWisePackageDetailsReportController {
	@Autowired
	private HospitalWisePackageDetailsReportService hospitalwisepackagedetailsreportservice;
	
	@ResponseBody
	@GetMapping(value = "/gethospitalwisepackage")
public List<HospitalWisePackageDetailsReportBean> hospital	
	(     	@RequestParam(required = false, value = "userid") Long userid,
			@RequestParam(required = false, value = "fromdate") Date fromdate,
			@RequestParam(required = false, value = "todate") Date todate,
			@RequestParam(required = false, value = "hospitalcode") String hospitalcode,
            @RequestParam(required = false, value = "statecode") String statecode,
			@RequestParam(required = false, value = "districtcode") String districtcode
			){
		////System.out.println(userid+" "+fromdate+" "+todate+" "+hospitalcode+" "+statecode+" "+districtcode);
	return hospitalwisepackagedetailsreportservice.list(userid,fromdate,todate,hospitalcode,statecode,districtcode);
	}

}
