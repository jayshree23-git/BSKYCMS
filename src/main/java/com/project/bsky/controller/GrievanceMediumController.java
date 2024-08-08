package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.GrievanceMedium;
import com.project.bsky.service.GrievanceMediumService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class GrievanceMediumController {
	@Autowired
	private GrievanceMediumService grivancemediumService;
	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/saveGrivancemediumData")
	public Response saveGrivancetypeData(@RequestBody GrievanceMedium grivancemedium) {

		Response response = new Response();
		try {
			response = grivancemediumService.saveGrivanceMediumData(grivancemedium);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("Failed");
		}
		return response;
	}

	@GetMapping(value = "/getGrievancMediumDataById")
	@ResponseBody
	public GrievanceMedium getgloballinkbyid(@RequestParam(value = "userid", required = false) Long userid) {
		return grivancemediumService.getgrievanceMediumbyId(userid);
	}

	@ResponseBody
	@GetMapping(value = "/getGrivancemediumData")
	public List<GrievanceMedium> getGrivanceMediumData() {
		List<GrievanceMedium> list = new ArrayList<GrievanceMedium>();
		try {
			list = grivancemediumService.getGrivanceMediumData();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@PostMapping(value = "/updateGrivanceMediumData")
	public Response updateGrivanceMediumData(@RequestBody GrievanceMedium grivancemedium) {
		Response response = new Response();
		try {
			response = grivancemediumService.updateGrivanceMediumData(grivancemedium);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("Failed");
		}
		return response;
	}

	@ResponseBody
	@GetMapping(value = "/getGrivancemediumList")
	public List<GrievanceMedium> getGrivancemediumList() {
		List<GrievanceMedium> list = new ArrayList<>();
		try {
			list = grivancemediumService.getGrivanceMediumList();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

}
