package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.bsky.bean.SnaDoctorTagBean;
import com.project.bsky.service.SnaDoctorTagService;

/**
 * @author jayshree.moharana
 *
 */
@Controller
@RequestMapping(value = "/api")
public class SnaDoctorTagController {
	
	@Autowired
	private SnaDoctorTagService snadoctortag;
	
	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/getsnadoctortag")
	public List<SnaDoctorTagBean> getsnadoctortag(@RequestParam(value = "userId", required = false) String userId) throws Exception {
		List<SnaDoctorTagBean> getsna = null;
		try {
			getsna = snadoctortag.getlist(userId);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getsna;
	}

	@ResponseBody
	@GetMapping(value = "/getsnataghospital")
	public List<SnaDoctorTagBean> gettagedhospital(@RequestParam(value = "userId", required = false) String userId)
			throws Exception {
		List<SnaDoctorTagBean> getsna = null;
		try {
			getsna = snadoctortag.gethospitallist(userId);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getsna;
	}
}
