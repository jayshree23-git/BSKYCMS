package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.DistrictMaster;
import com.project.bsky.service.ResetPasswordService;

@RestController
@RequestMapping(value = "/api")
public class HospitalMasterController {

	@Autowired
	private ResetPasswordService resetPasswordService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getHospitalMaster")
	private String getHospitalMaster(@RequestParam("stateCode") String stateCode,
			@RequestParam("distCode") String distCode) {
		String listData = null;
		try {
			listData = resetPasswordService.getHospitalMasterData(stateCode, distCode);
			return listData;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listData;
	}

	@ResponseBody
	@GetMapping(value = "/getDistrictList")
	public List<DistrictMaster> getDistrict(@RequestParam("stateCode") String stateCode) {
		List<DistrictMaster> districtList = null;
		try {
			districtList = resetPasswordService.getDistrictListByStateCode(stateCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return districtList;

	}

}
