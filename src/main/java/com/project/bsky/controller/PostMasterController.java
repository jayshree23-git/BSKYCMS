package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Dccdmomappingbean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.model.PostMasterModel;
import com.project.bsky.service.PostMasterService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class PostMasterController {
	
	@Autowired
	private PostMasterService postmasterservice;
	
	@PostMapping(value = "/savepostname")
	@ResponseBody
	public Response savepostname(@RequestBody PostMasterModel postmastermodel){
//		return postmasterservice.savepostname(postmastermodel);
		Response map=new Response();
		try {
			map=postmasterservice.savepostname(postmastermodel);
		} catch (Exception e) {
			e.printStackTrace();
			map.setStatus("400");
			map.setMessage(e.getMessage());
		}
		return map;
	
	}
	@GetMapping(value = "/getpostname")
	@ResponseBody
	public Map<String,Object> getpostname() {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",postmasterservice.getpostname());
			map.put("status",HttpStatus.OK.value());
			map.put("message","Success");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status",HttpStatus.BAD_REQUEST.value());
			map.put("error",e.getMessage());
			map.put("message","Error");
		}
		return map;
	}
//	@GetMapping(value = "/getpostnamebyid")
//	@ResponseBody
//	public PostMasterModel getpostnamebyid(@RequestParam(value = "userid", required = false) Long userid){
//		return postmasterservice.getpostnamebyid(userid);
//	}
	
	@PostMapping(value = "/updatepostname")
	@ResponseBody
	public Response updatepostname(@RequestBody PostMasterModel postmastermodel) {
		Response map=new Response();
		try {
			map=postmasterservice.updatepostname(postmastermodel);
		} catch (Exception e) {
			e.printStackTrace();
			map.setStatus("400");
			map.setMessage(e.getMessage());
		}
		return map;
	}
}
