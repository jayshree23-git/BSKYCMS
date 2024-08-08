/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
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

import com.project.bsky.bean.Response;
import com.project.bsky.bean.UserDetailsProfileBean;
import com.project.bsky.model.HospitalOperator;
import com.project.bsky.service.HospitalOperatorService;

/**
 * @author rajendra.sahoo
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class HospitalOperatorController {

	@Autowired
	private HospitalOperatorService hospitalservice;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/saveHospitaloperatorData")
	public Response addUserDetailsProfile(@RequestBody UserDetailsProfileBean bean) {
		Response response = new Response();
		try {
			response = hospitalservice.savehospitaloperator(bean);
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Something Went Wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@ResponseBody
	@PostMapping("/updateHospitaloperatorData")
	public Integer updateSNOuser(@RequestBody UserDetailsProfileBean bean) {
		Integer returnObject = 0;
		try {
			returnObject = hospitalservice.updatehospitaloperator(bean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			returnObject = 0;
		}
		return returnObject;

	}

	@GetMapping(value = "/getAllHospitaloperatorData")
	private List<HospitalOperator> getAllUserProfile(
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode) {
		List<HospitalOperator> listUserDetail = new ArrayList<HospitalOperator>();
		try {
			listUserDetail = hospitalservice.findbyhospitalcode(hospitalcode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listUserDetail;
	}

	@GetMapping(value = "/getappliedhospitaloperatorlist")
	private List<HospitalOperator> getAllHospitaloperatorforapprove(
			@RequestParam(value = "groupId", required = false) Integer groupid,
			@RequestParam(value = "userid", required = false) Long userid) {
		List<HospitalOperator> listUserDetail = new ArrayList<HospitalOperator>();
		try {
			listUserDetail = hospitalservice.getAllHospitaloperatorforapprove(groupid, userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return listUserDetail;
	}

	@GetMapping(value = "/gethospoperatorbyid")
	private Map<String, Object> gethospoperatorbyid(
			@RequestParam(value = "operatorid", required = false) Long operatorid) {
		Map<String, Object> deatisl = new HashedMap<>();
		try {
			deatisl.put("data", hospitalservice.gethospoperatorbyid(operatorid));
			deatisl.put("status", 200);
			deatisl.put("message", "Api Called Succesfully");
		} catch (Exception e) {
			deatisl.put("status", 400);
			deatisl.put("error", e.getMessage());
			deatisl.put("message", "Something Went Wrong");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return deatisl;
	}

	@GetMapping(value = "/takeactiononhospitaloperatorlist")
	private Response takeactiononhospitaloperatorlist(@RequestParam(value = "action", required = false) Integer action,
			@RequestParam(value = "operatorid", required = false) Long operatorid,
			@RequestParam(value = "createby", required = false) Long createby) {
		Response response = new Response();
		try {
			response = hospitalservice.takeactiononhospitaloperatorlist(operatorid, action, createby);
		} catch (Exception e) {
			response.setStatus("400");
			response.setMessage("Some Error Happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@GetMapping(value = "/gethospwiseoperatorcount")
	private List<Object> gethospwiseoperatorcount(@RequestParam(value = "statecode", required = false) String state,
			@RequestParam(value = "distcode", required = false) String dist,
			@RequestParam(value = "hospital", required = false) String hospital,
			@RequestParam(value = "userid", required = false) Long userid) {
		List<Object> objlist = new ArrayList<Object>();
		try {
			objlist = hospitalservice.gethospwiseoperatorcount(state, dist, hospital, userid);
		} catch (Exception e) {
			System.out.println(e);
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return objlist;
	}

	@GetMapping(value = "/getoperatoridthroughuserid")
	private Long gethospwiseoperatorcount(@RequestParam(value = "userId", required = false) Long userId) {
		Long operatorid = null;
		try {
			operatorid = hospitalservice.getoperatorid(userId);
		} catch (Exception e) {
			System.out.println(e);
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return operatorid;
	}

	@GetMapping(value = "/gethospwiseoperatorlistreport")
	private List<Object> gethospwiseoperatorlistreport(
			@RequestParam(value = "statecode", required = false) String state,
			@RequestParam(value = "distcode", required = false) String dist,
			@RequestParam(value = "hospital", required = false) String hospital,
			@RequestParam(value = "userid", required = false) Long userid) {
		List<Object> objlist = new ArrayList<Object>();
		try {
			objlist = hospitalservice.gethospwiseoperatorlistreport(state, dist, hospital, userid);
		} catch (Exception e) {
			System.out.println(e);
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return objlist;
	}
}
