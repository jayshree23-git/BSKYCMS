package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.Cpdlogbean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.ActionRemark;
import com.project.bsky.model.State;
import com.project.bsky.service.CPDRejectedService;
import com.project.bsky.service.SnoClaimProcessingDetails;

@RestController
@RequestMapping(value = "/api")
public class CPDRejectedController {

	@Autowired
	private CPDRejectedService cpdrejectedservice;

	@Autowired
	private SnoClaimProcessingDetails snoClaimProcessing;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/cpdrejectedlist")
	public List<Object> cpdrejectedlist(@RequestBody CPDApproveRequestBean requestBean) throws Exception {
		List<Object> rejectedlist = null;
		try {
			rejectedlist = cpdrejectedservice.getcpdrejectedlist(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return rejectedlist;
	}

	@GetMapping(value = "/getcpdrejecteddetailsid")
	@ResponseBody
	public Map<String, String> CPDrejectedById(@RequestParam("claimid") Integer claimid) {
		String cpdReject = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			cpdReject = cpdrejectedservice.getCpdrejecteddetailsdata(claimid);
			details.put("status", "success");
			details.put("details", cpdReject);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@ResponseBody
	@PostMapping(value = "/cpdaction")
	public ResponseEntity<Response> saveCpdrejected(@RequestBody Cpdlogbean LogBean) {
		Response response = null;
		try {
			response = cpdrejectedservice.saveRejectedDetails(LogBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/getAllRemark")
	@ResponseBody
	public List<ActionRemark> GetRemarks() {
		List<ActionRemark> allActionRemark = cpdrejectedservice.getAllActionRemarks();
		return allActionRemark;
	}

	@GetMapping("/getRemarksByid")
	@ResponseBody
	public ActionRemark GetRemarksById(@RequestParam(value = "remarkId") Long remarkId) {
		ActionRemark allActionRemark = cpdrejectedservice.getActionRemarkByid(remarkId);
		return allActionRemark;
	}

	@GetMapping("/getStatecpd")
	@ResponseBody
	public List<State> getState() {
		List<State> statedetails = snoClaimProcessing.getAllState();
		return statedetails;
	}

	@ResponseBody
	@GetMapping(value = "/getDistrictByStateCpd")
	public String getDistrict(@RequestParam("stateCode") String stateCode, @RequestParam("userId") Long userId) {
		String districtList = null;
		try {
			districtList = snoClaimProcessing.getDistrictList(stateCode, userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return districtList;
	}

	@ResponseBody
	@GetMapping(value = "/getHospitalListByCpd")
	public String getHospital(@RequestParam("stateCode") String stateCode, @RequestParam("distCode") String distCode,
			@RequestParam("userId") Long userId) {
		String hospitalList = null;
		try {
			hospitalList = snoClaimProcessing.getHospital(stateCode, distCode, userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return hospitalList;

	}

	@ResponseBody
	@GetMapping(value = "/getPreAuthDatacpd")
	public String PreAuthData(@RequestParam("urn") String urn) {
		String preAuth = null;
		try {
			preAuth = snoClaimProcessing.getPreAuthData(urn);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return preAuth;

	}

}
