/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Cpdwisehospitalwise;
import com.project.bsky.service.Cpdwisehospitalwiseaction;

/**
 * @author hrusikesh.mohanty
 *
 */
@RestController
@RequestMapping(value = "/api")
public class CpdwiseHospitalwise {

	@Autowired
	private Cpdwisehospitalwiseaction cpdwisehospitalwiseaction;

	@Autowired
	private Logger logger;

	@PostMapping(value = "/getcpdwisehospitalwiseactiondetails")
	@ResponseBody
	public List<Object> getcpdwisependingreportdetails(@RequestBody Cpdwisehospitalwise requestdatacpddetails)
			throws Exception {
		List<Object> cpdhospitalwiseactiondetails = new ArrayList<Object>();
		try {
			cpdhospitalwiseactiondetails = cpdwisehospitalwiseaction
					.getcpdwisehospitalwiseactiondetails(requestdatacpddetails);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return cpdhospitalwiseactiondetails;
	}
}
