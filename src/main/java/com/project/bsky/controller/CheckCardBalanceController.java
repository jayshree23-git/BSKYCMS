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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.CheackCardBalancebean;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.RationCardUser;
import com.project.bsky.service.CheckCardBalanceService;

/**
 * @author rajendra.sahoo
 *
 */
@RestController
@RequestMapping(value = "/api")
public class CheckCardBalanceController {

	@Autowired
	private CheckCardBalanceService ccbservice;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/checkcardbalance")
	@ResponseBody
	public CheackCardBalancebean checkcardbalance(@RequestParam(value = "urn", required = false) String urn,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "schemeId", required = false) Long schemeId,
			@RequestParam(value = "schemeCategoryId", required = false) Long schemeCategoryId) {
		CheackCardBalancebean jsonArray = new CheackCardBalancebean();
		try {
			jsonArray = ccbservice.checkcardbalance(urn, search, schemeId, schemeCategoryId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return jsonArray;
	}

	@GetMapping(value = "/checkbeneficry")
	@ResponseBody
	public CheackCardBalancebean checkbeneficry(@RequestParam(value = "urn", required = false) String urn,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "accessid", required = false) Long accessid,
			@RequestParam(value = "schemeId", required = false) Long schemeId,
			@RequestParam(value = "schemeCategoryId", required = false) Long schemeCategoryId) {
		CheackCardBalancebean jsonArray = new CheackCardBalancebean();
		try {
			jsonArray = ccbservice.checkbeneficry(urn, search, accessid, schemeId, schemeCategoryId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return jsonArray;
	}

	@GetMapping(value = "/getaccessuserlist")
	@ResponseBody
	public List<RationCardUser> getaccessuserlist() {
		List<RationCardUser> list = new ArrayList<RationCardUser>();
		try {
			list = ccbservice.getaccessuserlist();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/generateotpforcheckcardbalance")
	@ResponseBody
	public AuthRequest generateotp(@RequestParam(value = "accessid", required = false) Long accessid) {
		AuthRequest auth = new AuthRequest();
		try {

			auth = ccbservice.generateotp(accessid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			auth.setStatus("fail");
			auth.setMessage("Some error happen");
		}
		return auth;
	}

	@GetMapping(value = "/validateotpchkbalance")
	@ResponseBody
	public AuthRequest validateotpchkbalance(@RequestParam(value = "otp", required = false) String otp,
			@RequestParam(value = "accessid", required = false) Long accessid) {
		AuthRequest auth = new AuthRequest();
		try {

			auth = ccbservice.validateotpchkbalance(accessid, otp);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			auth.setStatus("fail");
			auth.setMessage("Some error happen");
		}
		return auth;
	}

	@GetMapping(value = "/beneficiarysearchbyname")
	@ResponseBody
	public List<Object> beneficiarysearchbyname(@RequestParam(value = "distid", required = false) String distid,
			@RequestParam(value = "searchtype", required = false) String searchtype,
			@RequestParam(value = "textvalue", required = false) String textvalue,
			@RequestParam(value = "schemeId", required = false) Integer schemeId,
			@RequestParam(value = "schemeCategoryId", required = false) String schemeCategoryId) {
		List<Object> list = new ArrayList<Object>();
		try {
			list = ccbservice.beneficiarysearchbyname(distid, searchtype, textvalue, schemeId, schemeCategoryId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/getDistrictListofnfsa")
	@ResponseBody
	public List<Map<String, Object>> getDistrictListofnfsa() {
		List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
		try {
			details = ccbservice.getDistrictListofnfsa();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return details;
	}

	@GetMapping(value = "/getlistthroughurn")
	@ResponseBody
	public List<Object> getlistthroughurn(@RequestParam(value = "urnno", required = false) String urn,
			@RequestParam(value = "schemeId", required = false) Integer schemeId,
			@RequestParam(value = "schememCategoryId", required = false) String schememCategoryId) {
		List<Object> details = new ArrayList<Object>();
		try {
			details = ccbservice.getlistthroughurn(urn, schemeId, schememCategoryId);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return details;
	}

	@GetMapping(value = "/getcarddetailsthroughurn")
	@ResponseBody
	public Map<String, Object> getcarddetailsthroughurn(@RequestParam(value = "urn", required = false) String urn,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "schemeidvalue", required = false) Integer schemeidvalue,
			@RequestParam(value = "schemeCategoryIdValue", required = false) String schemeCategoryIdValue) {
		Map<String, Object> details = new HashMap<>();
		try {
			details = ccbservice.getcarddetailsthroughurn(urn, search, schemeidvalue, schemeCategoryIdValue);
			details.put("status", 200);
			details.put("message", "Api Called Successfull");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", "Something Went Wrong");
			details.put("error", e.getMessage());
		}
		return details;
	}

	@GetMapping(value = "/checkcardbalancelog")
	@ResponseBody
	public Map<String, Object> getCardBalanceLog() { 
		Map<String, Object> details = new HashMap<>();
		try {
			String logData = ccbservice.getCardBalanceLog();
			details.put("status", "success");
			details.put("data", logData);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("message", "Something Went Wrong");
			details.put("error", e.getMessage());
		}
		return details;
	}
}
