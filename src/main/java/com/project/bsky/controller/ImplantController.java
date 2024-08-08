package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.Surveygroupmapping;
import com.project.bsky.model.Implant;
import com.project.bsky.service.ImplantService;


@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")

public class ImplantController {
	
	@Autowired
	private ImplantService implantService;
	
	@PostMapping(value = "/saveimplant")
	@ResponseBody
	public Response saveImplant(@RequestBody Implant implant){
		return implantService.saveImplant(implant);
	}
	
	
	
	@GetMapping(value = "/getimplant")
	@ResponseBody
	public List<Implant> getimplant(){
		return implantService.getImplant();
	}
	
	@DeleteMapping(value = "/deleteimplant/{implantId}")
	@ResponseBody
	public Response deleteImplant(@PathVariable(value = "implantId", required = false) Long implantId){
	return implantService.deleteImplant(implantId);
	}
	
	@GetMapping(value = "/getbyimplant/{implantId}")
	@ResponseBody
	public Implant getbyimplantId(@PathVariable(value = "implantId", required = false) Long implantId){
		return implantService.getbyimplantId(implantId);
	}
	
	@PutMapping(value = "/updateimplant/{implantId}")
	@ResponseBody
	public Response updateimplant(@RequestBody Implant implant,
			@PathVariable(value = "implantId", required = false) Long implantId) {
		return implantService.update(implantId, implant);
	}
	
	
	@GetMapping(value="/getpackageicddetails")
	public Map<String,Object> getpackageicddetails(@RequestParam(value = "procedure", required = false) String procedurecode) {
		Map<String,Object> data=new HashMap<>();
		try {
			data.put("status", 200);
			data.put("message", "Api Called Successfully");
			data.put("data",implantService.getpackageicddetails(procedurecode));
		}catch (Exception e) {
			e.printStackTrace();
			data.put("status", 400);
			data.put("message", "Something Went Wrong");
			data.put("error", e.getMessage());
		}
	return data;
	}
	
	@GetMapping(value="/implantproceduremappeddata")
	public Map<String,Object> implantproceduremappeddata(@RequestParam(value = "procedure", required = false) String procedurecode,
			@RequestParam(value = "packageheadercode", required = false) String packageheadercode) {
		Map<String,Object> data=new HashMap<>();
		try {
			data.put("status", 200);
			data.put("message", "Api Called Successfully");
			data.put("data",implantService.implantproceduremappeddata(procedurecode,packageheadercode));
		}catch (Exception e) {
			e.printStackTrace();
			data.put("status", 400);
			data.put("message", "Something Went Wrong");
			data.put("error", e.getMessage());
		}
	return data;
	}
	
	@PostMapping(value="/saveimplantconfiguration")
	public Map<String,Object> saveimplantconfiguration(@RequestBody Surveygroupmapping implantconfigdata) {
		Map<String,Object> data=new HashMap<>();
		try {
			data=implantService.getpackageicddetails(implantconfigdata);
		}catch (Exception e) {
			e.printStackTrace();
			data.put("status", 400);
			data.put("message", "Something Went Wrong");
			data.put("error", e.getMessage());
		}
	return data;
	}
	
}



