package com.project.bsky.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.BskyDocumentmaster;
import com.project.bsky.model.OutofpacketexpenditureMaster;
import com.project.bsky.service.OutofpacketexpenditureService;

@RestController
@RequestMapping(value = "/api")
public class OutofpacketexpenditureController {
	
	@Autowired
	private OutofpacketexpenditureService outofpacketexpenditureservice;
	
	@ResponseBody
	@PostMapping(value = "/savemst")
	public Response savedocumentmst(@RequestBody OutofpacketexpenditureMaster outofpacketexpendituremaster) {
		Response response = new Response();
		try {
			response = outofpacketexpenditureservice.savemst(outofpacketexpendituremaster);
		} catch (Exception e) {
			response.setMessage("Something Went Wrong! Try Later");
			response.setStatus("400");
			e.printStackTrace();
		}
		return response;
	}
	@ResponseBody
	@GetMapping(value = "/getlist")
	public Map<String,Object> getexpendituremst() {
		Map<String,Object> response = new HashMap<>();
		try {
			response.put("data", outofpacketexpenditureservice.getexpendituremst());
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
	@PostMapping(value = "/update")
	public Response update(@RequestBody OutofpacketexpenditureMaster outofpacketexpendituremaster) {
		Response response = new Response();
		try {
			response = outofpacketexpenditureservice.update(outofpacketexpendituremaster);
		} catch (Exception e) {
			response.setMessage("Something Went Wrong! Try Later");
			response.setStatus("400");
			e.printStackTrace();
		}
		return response;
	}

}
