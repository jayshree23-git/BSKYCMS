/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.CpdLeaveApplyAdmin;
import com.project.bsky.model.CPDLeaveInfo;
import com.project.bsky.model.UserDetailsCpd;
import com.project.bsky.service.CPDLeaveInfoService;
import com.project.bsky.service.CpdLeaveApplyAdminService;
import com.project.bsky.serviceImpl.UserDetailsForSaveCpdServiceImpl;

/**
 * @author priyanka.singh
 *
 */
@RestController
@RequestMapping(value = "/api")
public class CpdLeaveApplyAdminController {

	@Autowired
	private CpdLeaveApplyAdminService cpdLeaveApplyAdminService;

	@Autowired
	private CPDLeaveInfoService cpdleaveinfoservice;

	@Autowired
	private UserDetailsForSaveCpdServiceImpl userDetailsForSaveCPdService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getCpdNameList")
	private List<UserDetailsCpd> getHospitalAuthority() {
		return userDetailsForSaveCPdService.getCpdNameForLeave();
	}

	// SAVE CPD LEAVE REQUEST ON CPD SCREEN
	@ResponseBody
	@PostMapping(value = "/saveLeaveAdmin")
	public Integer saveCPDLeaveInfoData(@RequestBody CpdLeaveApplyAdmin CpdLeaveApplyAdmin) {
		Integer I = null;
		I = cpdLeaveApplyAdminService.saveCpdLeaveForAdmin(CpdLeaveApplyAdmin);
		return I;
	}

	@GetMapping(value = "/getCpdListLeaveData")
	@ResponseBody
	public List<CPDLeaveInfo> getcpdleavehistory(@RequestParam(value = "userId", required = false) Long userId) {
		return cpdLeaveApplyAdminService.getCpdAllLeaveData(userId);
	}

	@ResponseBody
	@GetMapping(value = "/deleteAdminLeave")
	public Integer deleteLeaveInAdmin(@RequestParam(value = "leaveId", required = false) Long leaveId) {
		try {
			cpdleaveinfoservice.deletebyLeaveId(leaveId);
			return 1;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return 1;
	}

	@GetMapping(value = "/getCpdFilterLeaveData")
	@ResponseBody
	public List<CPDLeaveInfo> getallcpdfilteractiondetails(
			@RequestParam(value = "bskyUserId", required = false) Integer bskyUserId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate) {
		return cpdLeaveApplyAdminService.getCPDLeaveFilterDataAdmin(bskyUserId, fromdate, todate);
	}
}
