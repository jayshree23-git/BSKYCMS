package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.MobilenoActiveStatus;
import com.project.bsky.service.MobileNoActiveStatuservice;


@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")
public class MobileNoActiveStatusController {
	@Autowired
	private MobileNoActiveStatuservice mobileNoActiveStatuservice;
	
	@GetMapping(value = "/getmobileNoActiveStatus")
	@ResponseBody
	public List<MobilenoActiveStatus> getmobileNoActiveStatus(){
		return mobileNoActiveStatuservice.getmobileNoActiveStatus();
	}
	@GetMapping(value = "/getmobileNoActiveStatusNotConnected")
	@ResponseBody
	public List<MobilenoActiveStatus> getMobileNoActiveStatusNotConnected( ){
		return mobileNoActiveStatuservice.getMobileNoActiveStatusNotConnected();
	}
	

}
