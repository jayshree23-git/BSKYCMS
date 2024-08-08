package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
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

import com.project.bsky.bean.OnlinePostConfigBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.model.OnlinePostConfigModel;
import com.project.bsky.model.PostMasterModel;
import com.project.bsky.service.OnlinePostConfigService;


@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class OnlinePostConfigController {
	
	@Autowired
	private OnlinePostConfigService onlinepostconfigservice;
	
	@Autowired
	private Logger logger;
	
	@GetMapping(value = "/getpostnamebyid")
	@ResponseBody
	public Map<String,Object> getpostnamebyid() {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",onlinepostconfigservice.getpostnamebyid());
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
	
	@PostMapping(value = "/savepostconfig")
	@ResponseBody
	public Response savepostname(OnlinePostConfigBean onlinepostconfigbean){
		Response map=new Response();
		try {
//			System.out.println(onlinepostconfigbean);
			map=onlinepostconfigservice.saveonlinepostconfig(onlinepostconfigbean);
		} catch (Exception e) {
			e.printStackTrace();
			map.setStatus("400");
			map.setMessage(e.getMessage());
		}
		return map;
	
	}
	
	@ResponseBody
	@GetMapping(value = "/downLoadonlinepostDoc")
	public String commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		try {
			if (fileName == null || fileName == "" || fileName.equalsIgnoreCase("")) {
				resp = "File not found";
			} else {
				onlinepostconfigservice.downLoadonlinepostDoc(fileName,response);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}
	
	@GetMapping(value = "/getonlinepostconfiglist")
	@ResponseBody
	public Map<String,Object> getpostname() {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",onlinepostconfigservice.getonlinepostconfiglist());
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
	@PostMapping(value = "/updateonlinepostconfig")
	@ResponseBody
	public Response updatepostname(OnlinePostConfigBean onlinepostconfigbean) {
		Response map=new Response();
		try {
			System.out.println(onlinepostconfigbean);
			map=onlinepostconfigservice.updateonlinepostconfig(onlinepostconfigbean);
		} catch (Exception e) {
			e.printStackTrace();
			map.setStatus("400");
			map.setMessage(e.getMessage());
		}
		return map;
	}
	@GetMapping(value = "/getonlinepostconfigbyid")
	@ResponseBody
	public OnlinePostConfigModel getonlinepostconfigbyid(@RequestParam(value = "configid", required = false) Long configid){
		return onlinepostconfigservice.getbyId(configid);
	}

}
