package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.PackageTaggingReportBean;
import com.project.bsky.bean.TaggingHistoryBean;
import com.project.bsky.service.HospitalSpecialityReportService;
import com.project.bsky.service.TaggingHistoryService;


@RestController
@RequestMapping(value = "/api")
public class TaggingHistoryController {
	@Autowired
	private TaggingHistoryService tagginghistoryservice ;
	@Autowired
	private Logger logger;
	
	@ResponseBody
	@GetMapping(value = "/gettaggedhistory")
	public List<TaggingHistoryBean> packageTaggingReport(
			@RequestParam(value = "stateId", required = false) String stateId,
			@RequestParam(value = "districtId", required = false) String districtId,
			@RequestParam(value = "hospitalId", required = false) String hospitalId
//			@RequestParam(value = "taggedType", required = false) Long taggedType
			) {
		List<TaggingHistoryBean> list = new ArrayList<>();
		try {
			list = tagginghistoryservice.gettaggedhistory(stateId, districtId, hospitalId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
}
