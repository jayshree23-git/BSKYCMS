package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.HospitalDoctorprofile;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.UnbundlingSubmitBean;
import com.project.bsky.service.UnbundlingPackageMasterService;
/**
 * @Project : BSKY Backend
 * @Author : HrusiKesh Mohnaty
 * @Created On : 13/06/2023 - 1:03 PM
 * @Purpose     :Unbundling Package
 */
@RestController
@RequestMapping(value = "/api")
public class UnbundlingPackageMasterController {

	@Autowired
	private UnbundlingPackageMasterService unbundlingPackageMasterService;

	@GetMapping(value = "/getunbundlingpagedetails")
	@ResponseBody
	public List<Object> getunbundlingpagedetails() {
		List<Object> list = new ArrayList<Object>();
		try {
			list = unbundlingPackageMasterService.getpackageidandpackagename();
		} catch (Exception e) {
			// System.out.println(e);
		}
		return list;
	}
	
	@PostMapping(value = "/getSubmitunbundlingpagedetails")
	public ResponseEntity<Response> getSubmitunbundlingpagedetails(@RequestBody UnbundlingSubmitBean resbean) {
		Response response = null;
		try {
			response = unbundlingPackageMasterService.getsubmitunbundlingpackage(resbean);
		} catch (Exception e) {
		}
		return ResponseEntity.ok(response);
	}
}
