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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.TreatingSubmitBean;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.HospitalBackdateConfigLog;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.service.Hospitalinforeportservice;

/**
 * @author rajendra.sahoo
 *
 */

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class HospitalInforeportcontroller {

	@Autowired
	private Hospitalinforeportservice hospitalservice;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/hospitalinforeport")
	public List<HospitalBean> hospitalreport() {
		List<HospitalBean> list = null;
		try {
			list = hospitalservice.gethospitalinfo();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/getmouexplist")
	public List<HospitalInformation> getmouexplist() {
		List<HospitalInformation> list = null;
		try {
			list = hospitalservice.getmouexplist();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/gethcexplist")
	public List<HospitalInformation> gethcexplist() {
		List<HospitalInformation> list = null;
		try {
			list = hospitalservice.gethcexplist();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/serachHospitalinfo")
	public List<HospitalBean> serachHospitalinfo(@RequestParam(value = "stateid") String stateid,
			@RequestParam(value = "distid") String distid, @RequestParam(value = "sna") Integer issna,
			@RequestParam(value = "dc") Integer isdc) {
		List<HospitalBean> list = null;
		try {
			list = hospitalservice.getfilterhospitalinfo(stateid, distid, issna, isdc);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/updatebackdateconfig")
	public Response updatebackdateconfig(@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "admission", required = false) String addmission,
			@RequestParam(value = "discharge", required = false) String discharge,
			@RequestParam(value = "hospital", required = false) String hospital) {
		Response list = new Response();
		try {
			list = hospitalservice.updatebackdateconfig(userid, addmission, discharge, hospital);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			list.setMessage("Some Thing Went Wrong");
			list.setStatus("400");
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/getallhospitallogdata")
	public List<HospitalBackdateConfigLog> getallhospitallogdata() {
		List<HospitalBackdateConfigLog> list = new ArrayList<HospitalBackdateConfigLog>();
		try {
			list = hospitalservice.getallhospitallogdata();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/getallhospitalbackdatelogdata")
	public List<HospitalBean> getallhospitalbackdatelogdata() {
		List<HospitalBean> list = new ArrayList<HospitalBean>();
		try {
			list = hospitalservice.getallhospitalbackdatelogdata();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/getincentive")
	public List<HospitalBean> getincentive(@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist) {
		List<HospitalBean> list = new ArrayList<HospitalBean>();
		try {
			list = hospitalservice.getincentive(state, dist);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/getincentivedetails")
	public Map<String, Object> getincentivedetails(@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "catagory", required = false) Integer catagory) {
		Map<String, Object> list = new HashMap<>();
		try {
			list = hospitalservice.getincentivedetails(state, dist, catagory);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/generateoptforhospitalupdate")
	@ResponseBody
	public AuthRequest generateotp(@RequestParam(value = "userid", required = false) Long userid) {
		AuthRequest auth = new AuthRequest();
		try {
			auth = hospitalservice.generateotp(userid);
		} catch (Exception e) {
			auth.setStatus("fail");
			auth.setMessage("Some error happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return auth;
	}

	@GetMapping(value = "/validateotpforhosp")
	@ResponseBody
	public AuthRequest validateotpchkbalance(@RequestParam(value = "otp", required = false) String otp,
			@RequestParam(value = "accessid", required = false) Long accessid) {
		AuthRequest auth = new AuthRequest();
		try {
			auth = hospitalservice.validateotphosp(accessid, otp);

		} catch (Exception e) {
			auth.setStatus("fail");
			auth.setMessage("Some error happen");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return auth;
	}

	@ResponseBody
	@GetMapping(value = "/hospitallistforotpconfigure")
	public Map<String, Object> hospitallistforotpconfigure(
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "otpreq", required = false) Long otpreq) {
		Map<String, Object> obj = new HashMap<>();
		try {
			obj = hospitalservice.hospitallistforotpconfigure(state, dist, userid, otpreq);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			obj.put("status", 400);
			obj.put("msg", "SomeThing Went Wrong");
			obj.put("error", e);
		}
		return obj;
	}

	@ResponseBody
	@GetMapping(value = "/hospitallistforloginotpconfigure")
	public Map<String, Object> hospitallistforloginotpconfigure(
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "otpreq", required = false) Long otpreq) {
		Map<String, Object> obj = new HashMap<>();
		try {
			obj = hospitalservice.hospitallistforloginotpconfigure(state, dist, userid, otpreq);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			obj.put("status", 400);
			obj.put("msg", "SomeThing Went Wrong");
			obj.put("error", e);
		}
		return obj;
	}

	@ResponseBody
	@PostMapping(value = "/submithospitallistforotpconfigure")
	public Response submithospitallistforotpconfigure(@RequestBody HospitalBean hospbean) {
		Response reponse = new Response();
		try {
			reponse = hospitalservice.submithospitallistforotpconfigure(hospbean.getHospdetailbean());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			reponse.setStatus("400");
			reponse.setMessage("Some Error Happen");
		}
		return reponse;
	}

	@ResponseBody
	@PostMapping(value = "/submithospitallistforloginotpconfigure")
	public Response submithospitallistforloginotpconfigure(@RequestBody HospitalBean hospbean) {
		Response reponse = new Response();
		try {
			reponse = hospitalservice.submithospitallistforloginotpconfigure(hospbean.getHospdetailbean());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			reponse.setStatus("400");
			reponse.setMessage("Some Error Happen");
		}
		return reponse;
	}

	@ResponseBody
	@GetMapping(value = "/getTreatingdoctorconfigurationlist")
	public Map<String, Object> getTreatingdoctorconfigurationlist(
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "userid", required = false) Long userid) {
		Map<String, Object> object = new HashMap<>();
		try {
			object = hospitalservice.getlistTreatingdoctorConfiguration(state, dist, type, userid);
		} catch (Exception e) {
			object.put("status", 400);
			object.put("msg", "SomeThing Went Wrong");
			object.put("error", e);
		}
		return object;
	}

	@ResponseBody
	@PostMapping(value = "/SubmitgetTreatingdoctorconfigurationlist")
	public Response SubmitgetTreatingdoctorconfigurationlist(@RequestBody TreatingSubmitBean hospbean) {
		Response reponse = new Response();
		try {
			reponse = hospitalservice.SubmitgetTreatingdoctorconfigurationlist(hospbean);
		} catch (Exception e) {
			reponse.setStatus("400");
			reponse.setMessage("Some Error Happen");
		}
		return reponse;
	}

	@ResponseBody
	@GetMapping(value = "/getlogdetailsFortreatingdoctor")
	public Map<String, Object> getlogdetailsFortreatingdoctor() {
		Map<String, Object> object = new HashMap<>();
		try {
			object = hospitalservice.getlatestlogdetails();
		} catch (Exception e) {
			object.put("status", 400);
			object.put("msg", "SomeThing Went Wrong");
			object.put("error", e);
		}
		return object;
	}
}
