package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.EnableHospitalDischargeBean;
import com.project.bsky.bean.HospBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.EnableHospitalDischargeService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class EnableHospitalDischargeController {

	@Autowired
	private EnableHospitalDischargeService enablehospitaldischargeservice;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/gettaggedhospitallist")
	@ResponseBody
	public List<EnableHospitalDischargeBean> gettaggedhospitallist(
			@RequestParam(required = false, value = "userid") Long userid,
			@RequestParam(required = false, value = "state") String state,
			@RequestParam(required = false, value = "dist") String dist,
			@RequestParam(required = false, value = "hospital") String hospital) {
		return enablehospitaldischargeservice.gettaggedhospitallist(userid, state, dist, hospital);
	}

	@GetMapping(value = "/gettaggedhospitallistfosna")
	@ResponseBody
	public List<HospBean> gettaggedhospitallistfosna(@RequestParam(required = false, value = "userid") Long userid) {
		List<HospBean> list = new ArrayList<HospBean>();
		try {
			list = enablehospitaldischargeservice.gettaggedhospitallistfosna(userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@PostMapping(value = "/submitenablehospital")
	public Response save(@RequestBody EnableHospitalDischargeBean sna) {
		Response response = new Response();
		try {
			enablehospitaldischargeservice.submit(sna);
			enablehospitaldischargeservice.disable(sna);
			response.setMessage("Submitted Successfully");
			response.setStatus("200");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some Error Happen");
			response.setStatus("400");
		}
		return response;
	}

}
