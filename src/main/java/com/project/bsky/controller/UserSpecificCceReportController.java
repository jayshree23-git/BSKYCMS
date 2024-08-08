package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.UserDetails;
import com.project.bsky.service.UserSpecificCceReportService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class UserSpecificCceReportController {
	@Autowired
	UserSpecificCceReportService userspecificccereportservice;

	@GetMapping(value = "/getccelist")
	@ResponseBody
	public List<UserDetails> getCceUserlist() {
		return userspecificccereportservice.getCceUserlist();
	}
}
