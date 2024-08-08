/**
 * 
 */
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.BskyMessageMaster;
import com.project.bsky.service.BskyMessageMasterService;

/**
 * @author rajendra.sahoo
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class BskyMessageMasterController {

	@Autowired
	private BskyMessageMasterService messageservice;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/savemessage")
	public Response savemessage(@RequestBody BskyMessageMaster messagemaster) {
		Response response = new Response();
		try {
			response = messageservice.savemessage(messagemaster);
		} catch (Exception e) {
			response.setMessage("Something Went Wrong! Try Later");
			response.setStatus("Failed");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@ResponseBody
	@PostMapping(value = "/updatemessage")
	public Response updatemessage(@RequestBody BskyMessageMaster messagemaster) {
		Response response = new Response();
		try {
			response = messageservice.updatemessage(messagemaster);
		} catch (Exception e) {
			response.setMessage("Something Went Wrong! Try Later");
			response.setStatus("Failed");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@ResponseBody
	@GetMapping(value = "/getmessage")
	public List<BskyMessageMaster> getmessage() {
		List<BskyMessageMaster> list = new ArrayList<BskyMessageMaster>();
		try {
			list = messageservice.getalldata();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

}
