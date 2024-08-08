package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
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

import com.project.bsky.bean.Response;
import com.project.bsky.bean.WhatsAppUserConfogurationBean;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.model.OutofpacketexpenditureMaster;
import com.project.bsky.model.WhatsAppTemplateModel;
import com.project.bsky.model.WhatsAppUserConfigurationModel;
import com.project.bsky.repository.GlobalLinkRepository;
import com.project.bsky.repository.WhatsAppUserConfigurationRepository;
import com.project.bsky.service.PrimaryLinkService;
import com.project.bsky.service.WhatsAppUserConfigurationService;
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class WhatsAppUserConfigurationController {
	private final Logger logger;

	@Autowired
	public WhatsAppUserConfigurationController(Logger logger) {
		this.logger = logger;
	}
	@Autowired
	WhatsAppUserConfigurationService whatsappuserconfigurationservice;
	@Autowired
	private WhatsAppUserConfigurationRepository whatsappuserconfigurationrepository;

	
	@GetMapping(value = "/getwhatsapptemplatename")
	@ResponseBody
	public List<WhatsAppTemplateModel> getwhatsapptemplatename() {
		return whatsappuserconfigurationservice.getwhatsapptemplatename();
	}
	@GetMapping("/getUserNamebyGroupId")
	public List<Object> getUserNamebyGroupId(@RequestParam(value = "grouplist",required = false) String groupid) {
		List<Object> userMaster = new ArrayList<>();
		try {
			userMaster = whatsappuserconfigurationservice.getUserNamebyGroupId(groupid);
			System.out.println(userMaster);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return userMaster;
	}
	@ResponseBody
	@PostMapping(value = "/savewhatappuserconfig")
	public Response saveconfig(@RequestBody WhatsAppUserConfogurationBean whatsapptemplatebean) {
		Response response = new Response();
		try {
			System.out.println(whatsapptemplatebean);
			response = whatsappuserconfigurationservice.savewhatappuserconfig(whatsapptemplatebean);
		} catch (Exception e) {
			response.setMessage("Something Went Wrong! Try Later");
			response.setStatus("400");
			e.printStackTrace();
		}
		return response;
	}
	@ResponseBody
	@GetMapping(value = "/getwhatsappconfigviewlist")
	public Map<String,Object> getwhatsappconfigviewlist() {
		Map<String,Object> response = new HashMap<>();
		try {
			response.put("data", whatsappuserconfigurationservice.getwhatsappconfigviewlist());
			response.put("status", HttpStatus.OK.value());
			response.put("message", "Api Called Successfully");
		} catch (Exception e) {
			response.put("status", HttpStatus.BAD_REQUEST.value());
			response.put("message", "Something Went Wrong");
			response.put("error",e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/inactiveonwhatsappconfig")
	public Response inactiveonwhatsappconfig(@RequestParam(value = "configid",required = false) Long configid,
			@RequestParam(value = "status",required = false) Integer status,
			@RequestParam(value = "updatedby",required = false) Long updatedby) {
		Response response = new Response();
		try {
			response=whatsappuserconfigurationservice.inactiveonwhatsappconfig(configid,status,updatedby);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus("400");
			response.setMessage(e.getMessage());
		}
		return response;
	}
}
