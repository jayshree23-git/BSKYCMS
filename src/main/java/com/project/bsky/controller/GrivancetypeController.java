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
import com.project.bsky.model.Grivancetype;
import com.project.bsky.service.GrivancetypeService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class GrivancetypeController {
	
	
	@Autowired
	private GrivancetypeService grivancetypeService;
	
	@Autowired
	private Logger logger;
	
	@ResponseBody
	@PostMapping(value = "/saveGrivancetypeData")
	public Response saveGrivancetypeData(@RequestBody Grivancetype grivancetype) {
		Response response=new Response();
		try {
		response=grivancetypeService.saveGrivancetypeData(grivancetype);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("Failed");
		}
		return response;
	}
	@GetMapping(value = "/getGrievanceTypeDataById")
	@ResponseBody
	public Grivancetype getgloballinkbyid(@RequestParam(value = "userid", required = false) Long userid){
		return grivancetypeService.getgrievancetypeId(userid);
	}
	
	@ResponseBody
	@GetMapping(value = "/getGrivancetypeData")
	public List<Grivancetype> getGrivancetypeData() {
		List<Grivancetype> list=new ArrayList<Grivancetype>();
		try {
			list=grivancetypeService.getGrivancetypeData();
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	@ResponseBody
	@PostMapping(value = "/updateGrivancetypeData")
	public Response updateGrivancetypeData(@RequestBody Grivancetype grivancetype) {
		Response response=new Response();
		try {
		response=grivancetypeService.updateGrivancetypeData(grivancetype);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("Failed");
		}
		return response;
	}

	

}
