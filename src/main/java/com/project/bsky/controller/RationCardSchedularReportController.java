package com.project.bsky.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.RationCardSchedularReportBean;
import com.project.bsky.service.RationCardSchedularReportService;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class RationCardSchedularReportController {
	@Autowired
	private RationCardSchedularReportService rationcardschedularreportservice;

	@ResponseBody
	@GetMapping(value = "/getrationreports")
	public List<RationCardSchedularReportBean> rationcard(@RequestParam(required = false, value = "year") String year,
			@RequestParam(required = false, value = "month") String month) {
		return rationcardschedularreportservice.list(year, month);
	}

	@GetMapping(value = "/getrationdetailsreport")
	public Map<String, Object> rationdetails

	(@RequestParam(required = false, value = "userid") Long userid,
			@RequestParam(required = false, value = "date") String date,
			@RequestParam(required = false, value = "flag") String flag,
			@RequestParam(required = false, value = "type") String type) {
		return rationcardschedularreportservice.details(userid, date, flag, type);
	}

}
