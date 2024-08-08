package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.CpdLeaveInfoBean;
import com.project.bsky.bean.CpdassignedhospitaldetailsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.CPDLeaveInfo;
import com.project.bsky.service.CPDLeaveInfoService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class CPDLeaveInfoController {

	@Autowired
	private CPDLeaveInfoService cpdleaveinfoservice;

	@Autowired
	private Logger logger;

	// CPD LIST ON SNO SCREEN
	@GetMapping(value = "/cpdleavetoApprovelist")
	@ResponseBody
	public List<CPDLeaveInfo> getallcpdleaveapplication(@RequestParam(value = "userId", required = false) Long userId) {
		return cpdleaveinfoservice.getallcpdleaveapplication(userId);
	}

	// CPD DETAILS ON SNO SCREEN
	@GetMapping(value = "/cpdtoleavedetails")
	@ResponseBody
	public CPDLeaveInfo getcpddetails(@RequestParam(value = "userId", required = false) Long userId) {
		return cpdleaveinfoservice.getcpddetails(userId);
	}

	// ACTION HISTORY ON SNO SCREEN
	@GetMapping(value = "/getcpdactiondetails")
	@ResponseBody
	public List<CPDLeaveInfo> getcpdactiondetails(@RequestParam(value = "userId", required = false) Long userId) {
		return cpdleaveinfoservice.getcpdactiondetails(userId);
	}

	// CPD LEAVE HISTORY ON CPD SCREEN
	@GetMapping(value = "/getcpdleavestatus")
	@ResponseBody
	public List<CPDLeaveInfo> getcpdleavestatus(@RequestParam(value = "userId", required = false) Long userId) {
		return cpdleaveinfoservice.getcpdleavestatus(userId);
	}

	// CPD HOSPITAL SCREEN SNO SCREEN
	@GetMapping(value = "/cpdmappingdetails")
	@ResponseBody
	public List<CpdassignedhospitaldetailsBean> cpdmappingdetails(
			@RequestParam(value = "user", required = false) Long user) {
		return cpdleaveinfoservice.cpdmappingdetails(user);
	}

	// APPROVE CPD LEAVE ON SNO SCREEN

	@GetMapping(value = "/getcpdleavehistory")
	@ResponseBody
	public List<CPDLeaveInfo> getcpdleavehistory(@RequestParam(value = "user", required = false) Long user) {
		return cpdleaveinfoservice.getcpdleavehistory(user);
	}

	@GetMapping(value = "/getallcpdleavefilterrequest")
	@ResponseBody
	public List<CPDLeaveInfo> getallcpdleavefilterrequest(@RequestParam(value = "userId", required = false) Long user,
			@RequestParam(value = "formDate", required = false) String formdate,
			@RequestParam(value = "Todate", required = false) String todate) {
		return cpdleaveinfoservice.getallcpdleavefilterrequest(user, formdate, todate);
	}

	@GetMapping(value = "/getallcpdfilteractiondetails")
	@ResponseBody
	public List<CPDLeaveInfo> getallcpdfilteractiondetails(@RequestParam(value = "userId", required = false) Long user,
			@RequestParam(value = "formDate", required = false) String formdate,
			@RequestParam(value = "Todate", required = false) String todate) {
		return cpdleaveinfoservice.getallcpdfilteractiondetails(user, formdate, todate);
	}

	@GetMapping(value = "/approvecpdleave")
	@ResponseBody
	public Response approvecpdleave(@RequestParam(value = "leaveId", required = false) Long leaveId,
			@RequestParam(value = "approve", required = false) Integer approve,
			@RequestParam(value = "createby", required = false) Long createby) {

		return cpdleaveinfoservice.approverequest(leaveId, approve, createby);
	}

	// SAVE CPD LEAVE REQUEST ON CPD SCREEN
	@ResponseBody
	@PostMapping(value = "/saveCPDLeaveInfoData")
	public Integer saveCPDLeaveInfoData(@RequestBody CpdLeaveInfoBean cpdLeaveInfoBean) {
		Integer I = null;
		I = cpdleaveinfoservice.saveCPDLeaveInfo(cpdLeaveInfoBean);
		return I;
	}

	@ResponseBody
	@GetMapping(value = "/deleteleaveapply")
	public Integer deleteById(@RequestParam(value = "leaveId", required = false) Long leaveId) {
		try {
			cpdleaveinfoservice.deletebyLeaveId(leaveId);
			return 1;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return 1;
	}

	@GetMapping(value = "/getCPDFilterData")
	@ResponseBody
	public List<CPDLeaveInfo> getallcpdfilteractiondetails(
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate) {
		return cpdleaveinfoservice.getCPDLeaveFilterData(userId, fromdate, todate);
	}
}
