package com.project.bsky.controller;

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
import com.project.bsky.model.GrievanceBy;
import com.project.bsky.service.GrievanceByService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class GrievanceByController {
	@Autowired
	private GrievanceByService grievancebyService;
	@Autowired
	private Logger logger;
	
	@ResponseBody
	@PostMapping(value = "/saveGrievanceByData")
	public Response addgrievanceby(@RequestBody GrievanceBy grievanceby) {

		Response returnObj = null;
		try {
			returnObj=grievancebyService.saveGrievancebyData(grievanceby);
			
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
			return returnObj;
		}
	@ResponseBody
	@GetMapping(value="/getGrievanceByData")
	public List<GrievanceBy> getQueryTypeList(){
		
		List<GrievanceBy> grievanceby=null;
		try {
			grievanceby=grievancebyService.getDetails();
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return grievanceby;	
	}	
	@GetMapping(value = "/getGrievanceByDataById")
	@ResponseBody
	public GrievanceBy getgloballinkbyid(@RequestParam(value = "userid", required = false) Long userid){
		return grievancebyService.getgrievancebyId(userid);
	}
	@ResponseBody
	@PostMapping(value = "/updateGrievanceByData")
	public Response updateGrievanceByData(@RequestBody GrievanceBy grievanceby) {
		Response returnObj = null;
		try {
			returnObj=grievancebyService.updateGrievanceBy(grievanceby);
			
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}
		}

		
