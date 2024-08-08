package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.OngoingtreatmentReportBean;
import com.project.bsky.service.OngoingtreatmentReportService;

/**
 * @author jayshree.moharana
 *
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class OngoingtreatmentReportController {

	@Autowired
	private OngoingtreatmentReportService ongoingtreatmentreportservice;

	@ResponseBody
	@GetMapping(value = "/geturnwisereport")
	public List<OngoingtreatmentReportBean> urnwise(@RequestParam(required = false, value = "urn") String urn,
			@RequestParam(required = false, value = "usename") String usename) {
		return ongoingtreatmentreportservice.urnwise(urn, usename);
	}

	@GetMapping(value = "/gethospitalwisereport")
	public List<OngoingtreatmentReportBean> hospitalwise

	(@RequestParam(required = false, value = "username") String username,
			@RequestParam(required = false, value = "statecode") String statecode,
			@RequestParam(required = false, value = "districtcode") String districtcode) {
		return ongoingtreatmentreportservice.hospitalwise(username, statecode, districtcode);
	}
}
