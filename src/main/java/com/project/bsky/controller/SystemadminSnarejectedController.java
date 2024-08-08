/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.Systemadminsnarejected;
import com.project.bsky.service.SystemadminsnarejectedService;

/**
 * @author hrusikesh.mohanty
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class SystemadminSnarejectedController {

	@Autowired
	private SystemadminsnarejectedService systemadminsnarejectedService;

	@Autowired
	private Logger logger;

	@PostMapping(value = "/getsystemadminsnarejectedlist")
	public ResponseEntity<?> getsystemadminsnarejectedlist(@RequestBody Systemadminsnarejected systemadminrejected) {
		Map<String, Object> details = new HashMap<String, Object>();
		List<Object> listdetails = new ArrayList<Object>();
		try {
			listdetails = systemadminsnarejectedService.getsystemadminsnarejectedlist(systemadminrejected);
			details.put("statusCode", 200);
			details.put("status", "Success");
			details.put("data", listdetails);
			details.put("msg", "System Admin- SNA Rejected List Fetched Successfully");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("statusCode", 500);
			details.put("status", "Fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok().body(details);
	}

	@PostMapping(value = "/getdetailsystemadminsnarejectedList")
	@ResponseBody
	public ResponseEntity<?> getdetailsystemadminsnarejectedList(@RequestBody CPDApproveRequestBean requestBean)
			throws Exception {
		Map<String, Object> details = new HashMap<String, Object>();
		List<Object> systemsnalist = new ArrayList<Object>();
		Long size = null;
		try {
			Map<Long, List<Object>> systemadminlist = systemadminsnarejectedService
					.getsystemAdminSnadetails(requestBean);
			for (Map.Entry<Long, List<Object>> entry : systemadminlist.entrySet()) {
				size = entry.getKey();
				systemsnalist = entry.getValue();
			}
			details.put("status", "success");
			details.put("size", size);
			details.put("data", systemsnalist);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}
}
