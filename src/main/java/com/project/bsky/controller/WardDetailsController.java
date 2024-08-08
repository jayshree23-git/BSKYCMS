package com.project.bsky.controller;

import java.util.List;

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
import com.project.bsky.model.PackageDetailsHospital;
import com.project.bsky.model.Ward;
import com.project.bsky.model.WardDetails;
import com.project.bsky.service.WardDetailsService;
import com.project.bsky.service.WardService;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")

public class WardDetailsController {
	
	@Autowired
	private WardDetailsService wardDetailsService;

	@PostMapping(value = "/savewarddetails")
	@ResponseBody
	public Response saveWardDetails(@RequestBody WardDetails wardDetails) {
		return wardDetailsService.saveWardDetails(wardDetails);
	}

	@GetMapping(value = "/getallwarddetails")
	@ResponseBody
	public List<WardDetails> getwarddetails() {
		return wardDetailsService.getwarddetails();
	}

	@GetMapping(value = "/getWardMaster")
	@ResponseBody
	public List<Ward> getAllWard(@RequestParam Long WardMasterId) {

		return wardDetailsService.getAllward(WardMasterId);
	}
}
