/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.SnaSysRejectedList;
import com.project.bsky.service.SystemRejectedReportsService;

/**
 * @author rajendra.sahoo
 *
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class SystemRejectedReportscontroller {
	
	@Autowired
	private SystemRejectedReportsService sysservice;
	
	@ResponseBody
	@GetMapping(value = "/sysrejreports")
	public List<SnaSysRejectedList> sysrejreports(@RequestParam(required = false, value = "formdate") String formdate,
			@RequestParam(required = false, value = "todate") String todate,
			@RequestParam(required = false, value = "state") String state,
			@RequestParam(required = false, value = "dist") String dist,
			@RequestParam(required = false, value = "hospital") String hospital,
			@RequestParam(required = false, value = "userID") String userid){
		////System.out.println(formdate+" "+todate+" "+state+" "+dist+" "+hospital+" "+userid);
		return sysservice.sysrejreports(formdate,todate,state,dist,hospital,userid);
		
	}

}
