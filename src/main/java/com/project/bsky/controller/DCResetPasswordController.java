package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.DCResetPasswordService;

@RestController
@RequestMapping(value = "/api")
public class DCResetPasswordController {

	@Autowired
	private DCResetPasswordService dCResetPasswordService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getHospitalInfoForResetpassOfDC")
	private List<Object> getResetPasswordData(@RequestParam(value = "userId", required = false) Long userId) {
		List<Object> list = new ArrayList<Object>();
		try {
			list = dCResetPasswordService.listData(userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
}