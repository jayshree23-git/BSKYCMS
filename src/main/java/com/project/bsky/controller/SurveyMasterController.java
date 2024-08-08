/**
 * 
 */
package com.project.bsky.controller;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.Surveygroupmapping;
import com.project.bsky.model.QuestionMaster;
import com.project.bsky.model.SurveyMaster;
import com.project.bsky.service.SurveyMasterService;

/**
 * 
 */
@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")
public class SurveyMasterController {
	
	@Autowired
	private SurveyMasterService surveymasterserv;
	
	@Autowired
	private Logger logger;
	
	@PostMapping(value = "/savequestionmaster")
	public Response savequestionmaster(@RequestBody QuestionMaster questionmaster){
		Response response = new Response();
		try {
			response = surveymasterserv.savequestionmaster(questionmaster);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}
	
	@GetMapping(value = "/getallquestionmst")
	public Map<String,Object> getallquestionmst(){
		 Map<String,Object> response = new HashedMap<>();
		try {
			response.put("data", surveymasterserv.getallquestionmst());
			response.put("status", 200);
			response.put("message", "Success");
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.put("status", 200);
			response.put("message", "Error");
			response.put("error", e.getMessage());
		}
		return response;
	}
	
	@PostMapping(value = "/updatequestionmaster")
	public Response updatequestionmaster(@RequestBody QuestionMaster questionmaster){
		Response response = new Response();
		try {
			response = surveymasterserv.updatequestionmaster(questionmaster);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}
	
	@PostMapping(value = "/savesurveymst")
	public Response savesurveymst(@RequestBody SurveyMaster surveymaster){
		Response response = new Response();
		try {
			response = surveymasterserv.saveSurveyMaster(surveymaster);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}
	
	@GetMapping(value = "/getsurveymstlist")
	public Map<String,Object> getsurveymstlist(){
		 Map<String,Object> response = new HashedMap<>();
		try {
			response.put("data", surveymasterserv.getsurveymstlist());
			response.put("status", 200);
			response.put("message", "Success");
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.put("status", 200);
			response.put("message", "Error");
			response.put("error", e.getMessage());
		}
		return response;
	}
	
	@PostMapping(value = "/updatesurveymst")
	public Response updatesurveymst(@RequestBody SurveyMaster surveymaster){
		Response response = new Response();
		try {
			response = surveymasterserv.updatesurveymst(surveymaster);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}
	
	@GetMapping(value = "/getactivesurveylist")
	public Map<String,Object> getactivesurveylist(@RequestParam(value = "val", required = false) Integer val){
		 Map<String,Object> response = new HashedMap<>();
		try {
			response.put("data", surveymasterserv.getactivesurveylist(val));
			response.put("status", 200);
			response.put("message", "Success");
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.put("status", 200);
			response.put("message", "Error");
			response.put("error", e.getMessage());
		}
		return response;
	}
	
	@GetMapping(value = "/getgrouplistbysurveyid")
	public Map<String,Object> getgrouplistbysurveyid(@RequestParam(value = "surveyid", required = false) Long surveyid){
		 Map<String,Object> response = new HashedMap<>();
		try {
			response=surveymasterserv.getgrouplistbysurveyid(surveyid);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.put("status", 200);
			response.put("message", "Error");
			response.put("error", e.getMessage());
		}
		return response;
	}

	@PostMapping(value = "/savegroupmapping")
	public Response savegroupmapping(@RequestBody Surveygroupmapping surveymaster){
		Response response = new Response();
		try {
			System.out.println(surveymaster);
			response = surveymasterserv.savegroupmapping(surveymaster);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
		}
		return response;
	}
	
	@GetMapping(value = "/getallsurveygroupmappinglist")
	public Map<String,Object> getallsurveygroupmappinglist(@RequestParam(value = "surveyid", required = false) Long surveyid){
		 Map<String,Object> response = new HashedMap<>();
		try {
			response=surveymasterserv.getallsurveygroupmappinglist(surveyid);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.put("status", 200);
			response.put("message", "Error");
			response.put("error", e.getMessage());
		}
		return response;
	}
	
	@GetMapping(value = "/getquestionlistbysurveyid")
	public Map<String,Object> getquestionlistbysurveyid(@RequestParam(value = "surveyid", required = false) Long surveyid){
		 Map<String,Object> response = new HashedMap<>();
		try {
			response=surveymasterserv.getquestionlistbysurveyid(surveyid);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.put("status", 200);
			response.put("message", "Error");
			response.put("error", e.getMessage());
		}
		return response;
	}
	
	@PostMapping(value = "/savequestionmapping")
	public Response savequestionmapping(@RequestBody Surveygroupmapping questionmapping){
		Response response = new Response();
		try {
			System.out.println(questionmapping);
			response = surveymasterserv.savequestionmapping(questionmapping);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setStatus("400");
			response.setMessage("Something Went Wrong!");
		}
		return response;
	}
	
	@GetMapping(value = "/getallsurveyquestionmappinglist")
	public Map<String,Object> getallsurveyquestionmappinglist(@RequestParam(value = "surveyid", required = false) Long surveyid){
		 Map<String,Object> response = new HashedMap<>();
		try {
			response=surveymasterserv.getallsurveyquestionmappinglist(surveyid);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response.put("status", 200);
			response.put("message", "Error");
			response.put("error", e.getMessage());
		}
		return response;
	}
}
