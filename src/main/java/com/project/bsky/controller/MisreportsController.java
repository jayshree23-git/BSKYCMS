package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import com.project.bsky.bean.Cpdperformancebean;
import com.project.bsky.bean.MisreportsBean;
import com.project.bsky.bean.Snawiseunprocessedbean;
import com.project.bsky.bean.Treatmenthistorybeandetails;
import com.project.bsky.model.IcdSearchLog;
import com.project.bsky.service.MisreportsService;

/**
 * @author rajendra.sahoo
 *
 */

@RestController
@RequestMapping(value = "/api")
public class MisreportsController {

	@Autowired
	private MisreportsService misService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/treatmenthistorybyrationcarddetails")
	@ResponseBody
	public Map<String, Object> treatmenthistorydeatils(
			@RequestParam(value = "action", required = false) String actioncode,
			@RequestParam(value = "treatmentid", required = false) Long treatmentid,
			@RequestParam(value = "packageid", required = false) Long packageid) {
		Map<String, Object> responseMap = new LinkedHashMap<>();
		try {
			responseMap = misService.treatmenthistory(actioncode, treatmentid, packageid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return responseMap;
	}

	@GetMapping(value = "/getcpdwiseperformace")
	@ResponseBody
	public Cpdperformancebean getcpdwiseperformace(@RequestParam(value = "action", required = false) String actioncode,
			@RequestParam(value = "fromdate", required = false) Date fromdate,
			@RequestParam(value = "todate", required = false) Date todate,
			@RequestParam(value = "cpdid", required = false) Long cpdid,
			@RequestParam(value = "userid", required = false) Long userid) {
		Cpdperformancebean responseMap = new Cpdperformancebean();
		try {
			responseMap = misService.getcpdwiseperformace(actioncode, fromdate, todate, cpdid, userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return responseMap;
	}

	@GetMapping(value = "/getcpdwiseperformacedetails")
	@ResponseBody
	public List<Object> getcpdwiseperformacedetails(
			@RequestParam(value = "action", required = false) Integer actioncode,
			@RequestParam(value = "fromdate", required = false) String fromdate,
			@RequestParam(value = "todate", required = false) String todate,
			@RequestParam(value = "cpdid", required = false) Long cpdid,
			@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "serchtype", required = false) Integer serchtype) {
		List<Object> responseMap = new ArrayList<Object>();
		try {
			responseMap = misService.getcpdwiseperformacedetails(actioncode, fromdate, todate, cpdid, userid,
					serchtype);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return responseMap;
	}

	@PostMapping(value = "/getSnawiseunprocessedclaimlistview")
	@ResponseBody
	public List<Object> getSnawiseunprocessedclaimlist(@RequestBody Snawiseunprocessedbean requestBean)
			throws Exception {
		List<Object> snoclaimList = new ArrayList<Object>();
		try {
			snoclaimList = misService.getSnawiseunprocessedcountdetails(requestBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return snoclaimList;

	}

	@GetMapping(value = "/getDistrictListByStateIddcid")
	@ResponseBody
	public List<Object> getDistrictListByStateIddcid(@RequestParam(value = "dcid", required = false) Long dcid,
			@RequestParam(value = "stateid", required = false) String stateid) {
		List<Object> list = new ArrayList<Object>();
		try {
			list = misService.getDistrictListByStateIddcid(dcid, stateid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/getHospitalbyDistrictIddcid")
	@ResponseBody
	public List<Object> getHospitalbyDistrictIddcid(@RequestParam(value = "dcid", required = false) Long dcid,
			@RequestParam(value = "stateid", required = false) String stateid,
			@RequestParam(value = "distid", required = false) String distid) {
		List<Object> list = new ArrayList<Object>();
		try {
			list = misService.getHospitalbyDistrictIddcid(dcid, stateid, distid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/gethospongingtreatmentdtls")
	@ResponseBody
	public Map<String, Object> gethospongingtreatmentdtls(
			@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "flag", required = false) Integer flag,
			@RequestParam(value = "hospital", required = false) String hospital,
			@RequestParam(value = "userId", required = false) Long userId) {
		Map<String, Object> details = new HashMap<>();
		try {
			details = misService.gethospongingtreatmentdtls(formdate, toDate, flag, hospital, userId);
		} catch (Exception e) {
			details.put("status", 400);
			details.put("msg", "Something went Wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return details;
	}

	@GetMapping(value = "/getauthlivestatus")
	@ResponseBody
	public Map<String, Object> getauthlivestatus(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "hospital", required = false) String hospital,
			@RequestParam(value = "userId", required = false) Long userId) {

		Map<String, Object> list = new HashMap<>();
		try {
			list = misService.getauthlivestatus(formdate, state, dist, hospital, userId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/gethospitalauthdetails")
	@ResponseBody
	public List<Object> gethospitalauthdetails(@RequestParam(value = "formdate", required = false) Date formdate,
			@RequestParam(value = "todate", required = false) Date todate,
			@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "hospital", required = false) String hospital) {
		List<Object> list = new ArrayList<>();
		try {
			list = misService.gethospitalauthdetails(formdate, todate, flag, type, hospital);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/blockedcaselogdetailsof1")
	@ResponseBody
	public Treatmenthistorybeandetails blockedcaselogdetailsof1(
			@RequestParam(value = "txnid", required = false) Long txnid,
			@RequestParam(value = "pkgid", required = false) Long pkgid,
			@RequestParam(value = "userId", required = false) Long userid) {
		Treatmenthistorybeandetails list = new Treatmenthistorybeandetails();
		try {
			list = misService.blockedcaselogdetailsof1(txnid, pkgid, userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@PostMapping(value = "/saveicdsearchlog")
	@ResponseBody
	public void saveicdsearchlog(@RequestBody IcdSearchLog icdsearchlog) {
		try {
			misService.saveicdsearchlog(icdsearchlog);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

}
