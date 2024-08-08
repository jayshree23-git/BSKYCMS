package com.project.bsky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.Implant;
import com.project.bsky.model.NotConnected;
import com.project.bsky.service.NotConnectedService;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")
public class NotConnectedCallController {
	@Autowired
	private NotConnectedService notConnectedService;
	
	@GetMapping(value = "/getbynotconnectedCall/{id}")
	@ResponseBody
	public NotConnected  getbynotconnectedCall(@PathVariable(value = "id", required = false) Long id){
		return notConnectedService.getbynotconnectedCall(id);
	}
	
}
