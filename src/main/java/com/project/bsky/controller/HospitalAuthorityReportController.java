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

import com.project.bsky.bean.HospitalAuthTaggingBean;
import com.project.bsky.model.TxnClaimActionLog;
import com.project.bsky.model.UserDetailsProfile;
import com.project.bsky.service.HospitalAuthTaggingService;
import com.project.bsky.service.UserDetailsProfileService;

@RestController
@RequestMapping(value = "/api")
public class HospitalAuthorityReportController {

	@Autowired
	private UserDetailsProfileService userDetailsProfileService;

	@Autowired
	private HospitalAuthTaggingService hospitalAuthTaggingService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/gethospauthrtydetailsreport")
	@ResponseBody
	public List<Object> getdetailsreport(@RequestParam(value = "userid", required = false) Long userId,
			@RequestParam(value = "hospital", required = false) String hospital,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "searchtype", required = false) Integer searchtype

	) {
		List<Object> getdetailsreport = null;
		try {
			getdetailsreport = hospitalAuthTaggingService.gethospauthrtydetailsreport(userId, hospital, fromdate,
					todate, searchtype);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return getdetailsreport;

	}

	@GetMapping(value = "/getviewremark")
	@ResponseBody
	public TxnClaimActionLog getviewremark(@RequestParam(value = "claim", required = false) Long claim,
			@RequestParam(value = "type", required = false) Integer type

	) {
		TxnClaimActionLog txncaimaction = null;
		try {

			if (type == 2) {
				type = 3;
			} else if (type == 3) {
				type = 4;
			} else if (type == 4) {
				type = 2;
			} else if (type == 5) {
				type = 1;
			} else {
				txncaimaction.setRemarks("No Data Found !!");
				return txncaimaction;
			}
			txncaimaction = hospitalAuthTaggingService.getviewremark(claim, type);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return txncaimaction;

	}

	@ResponseBody
	@GetMapping(value = "/getclaimDetailsForHospitalAuth")
	public List<Object> getallDataForClaimRequest(@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "userId", required = false) Long userId) {
		List<Object> claimList = null;
		try {

			claimList = hospitalAuthTaggingService.getclaimrasiedataForAuthority(fromDate, toDate, type, hospitalCode,
					userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimList;
	}

	@ResponseBody
	@GetMapping(value = "/getclaimQryByCPDForHospitalAuth")
	public List<Object> getclaimQryByCPDForHospitalAuth(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "userId", required = false) Long userId) {
		List<Object> claimList = null;
		try {

			claimList = hospitalAuthTaggingService.getclaimQuryByCPDDataForAuthority(fromDate, toDate, type,
					hospitalCode, userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimList;
	}

	@ResponseBody
	@GetMapping(value = "/getclaimQryBySNAForHospitalAuth")
	public List<Object> getclaimQryBySNAForHospitalAuth(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "userId", required = false) Long userId) {
		List<Object> claimList = null;
		try {

			claimList = hospitalAuthTaggingService.getclaimQuryBySNADataForAuthority(fromDate, toDate, type,
					hospitalCode, userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return claimList;
	}

}
