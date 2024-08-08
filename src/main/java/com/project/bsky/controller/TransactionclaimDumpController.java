package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.bsky.model.Hospitalpwd;
import com.project.bsky.service.TransactionclaimDumpService;

/**
 * @author jayshree.moharana
 *
 */
@CrossOrigin
@Controller
@RequestMapping(value = "/api")
public class TransactionclaimDumpController {
	@Autowired
	
	private TransactionclaimDumpService transactionclaimdump;
	@GetMapping(value = "/getdischargereport")
	@ResponseBody
	public List<Object> hospitaldeschargependingreport
	(@RequestParam(required = false, value = "userID") Long userId,
			@RequestParam(value = "formdate", required = false) String formdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "stateId", required = false) String stateId,
			@RequestParam(value = "districtId", required = false) String districtId,
			@RequestParam(value = "hospitalId", required = false) String hospitalId)

			{
		////System.out.println(formdate+" "+todate);
		return transactionclaimdump.dischargereport(userId,formdate,todate,stateId, districtId, hospitalId);
	}
	
	
	
	
	@GetMapping(value = "/getdetails")
	@ResponseBody
	public List<Object> getdetails(@RequestParam(value = "formdate", required = false) String formdate,
		@RequestParam(value = "todate", required = false) String todate) {
		
		{
		////System.out.println(formdate+" "+todate);
		return transactionclaimdump.getdischargedetails(formdate,todate);

		} 

	


	}}
