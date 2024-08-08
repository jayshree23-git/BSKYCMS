package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.QueryTypeBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.QueryType;
import com.project.bsky.service.QueryTypeService;

@RestController
@RequestMapping(value = "/api")
public class QueryTypeController {

	@Autowired
	private QueryTypeService queryTypeService;

	@Autowired
	private Logger logger;

	@PostMapping(value = "/saveQueryTypeData")
	public Response addQueryType(@RequestBody QueryTypeBean queryTypeBean) {
		Response returnObj = null;

		try {
			returnObj = queryTypeService.saveQueryType(queryTypeBean);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

	@PostMapping(value = "/updateQueryTypeData")
	public Response updateQueryType(@RequestBody QueryTypeBean queryTypeBean) {
		Response returnObj = null;
		try {
			returnObj = queryTypeService.updateQueryType(queryTypeBean);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

	@GetMapping(value = "/getQueryDetailList")
	public List<QueryType> getQueryTypeList() {

		List<QueryType> queryDetails = null;
		try {
			queryDetails = queryTypeService.getDetails();
			return queryDetails;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return queryDetails;
	}

	@ResponseBody
	@GetMapping(value = "/getQueryById")
	public QueryType getById(@RequestParam(value = "typeId", required = false) Long typeId) {
		QueryType queryType = queryTypeService.getQueryById(typeId);
		return queryType;
	}
}
