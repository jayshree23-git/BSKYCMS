/**
 * 
 */
package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.HospitalCivilInfrastructure;
import com.project.bsky.bean.HospitalSpecialistBean;
import com.project.bsky.bean.HospitalSpecialistListBean;
import com.project.bsky.bean.QcApprovalHospitalSpecialitybean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.HospitalSpecialityUpdationService;

/**
 * @author rajendra.sahoo
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class HospitalSpecialityUpdationController {

	@Autowired
	private HospitalSpecialityUpdationService hospitalSpecilityService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/gettmasactivehospitallist")
	public List<HospitalBean> hospitalreport(@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist) {
		List<HospitalBean> list = null;
		try {
			list = hospitalSpecilityService.gethospitalinfo(state, dist);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@PostMapping(value = "/savepecialistconfig")
	public Response savepecialistconfig(@RequestBody HospitalSpecialistBean hospitalSpecialistBean) {
		Response response = new Response();
		try {
			response = hospitalSpecilityService.savepecialistconfig(hospitalSpecialistBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@ResponseBody
	@PostMapping(value = "/savecivilinfraconfig")
	public Response savecivilinfraconfig(@RequestBody HospitalCivilInfrastructure civilInfrastructure) {
		Response response = new Response();
		try {
			response = hospitalSpecilityService.savecivilinfraconfig(civilInfrastructure);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@ResponseBody
	@GetMapping(value = "/getpackagelistbyhospitalid")
	public List<HospitalSpecialistListBean> getpackagelistbyhospitalid(
			@RequestParam(value = "hospitalid", required = false) Long hospitalId,
			@RequestParam(value = "userid", required = false) Long userid) {
		List<HospitalSpecialistListBean> list = new ArrayList<HospitalSpecialistListBean>();
		try {
			list = hospitalSpecilityService.getpackagelistbyhospitalid(hospitalId, userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/getcivilinfradetailsbyhospitalid")
	public Map<String, Object> getEmpanelmentDetails(@RequestParam(value = "hospitalid") Integer hospitalId) {

		Map<String, Object> details = new LinkedHashMap<>();
		String output = "";
		try {
			details = hospitalSpecilityService.getEmpanelmentDetails(hospitalId);
			details.put("status", 200);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			output = e.getMessage();
			details.put("status", 400);
			details.put("message", "Some Error Happen");
			details.put("msg", output);
		}
		return details;
	}

	@ResponseBody
	@GetMapping(value = "/getHospitalDetailsFromCode")
	public List<HospitalBean> hospitalreport(
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode) {
		List<HospitalBean> list = null;
		try {
			list = hospitalSpecilityService.gethospitalDetails(hospitalCode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@GetMapping(value = "/getEmpaneledhospitallist")
	public String hospitalreport(@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "hospitalId", required = false) String hospitalId,
			@RequestParam(value = "userid", required = false) Long userid) {
		String list = null;
		try {
			list = hospitalSpecilityService.getHospitalEmpList(state, dist, hospitalId, userid);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@GetMapping(value = "/getcivilInfraDetailsById")
	@ResponseBody
	public Map<String, String> getcivilInfraDetailsById(@RequestParam("civilInfraId") Integer civilInfraId) {
		String civilInfraDetails = null;
		Map<String, String> details = new HashMap<String, String>();
		try {
			civilInfraDetails = hospitalSpecilityService.getCivilInfraDetailsById(civilInfraId);
			details.put("status", "success");
			details.put("details", civilInfraDetails);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;

	}

	@GetMapping(value = "/getSpecialityPackages")
	@ResponseBody
	public Map<String, Object> getSpecialityPackages(@RequestParam("packageCode") String packageCode,
			@RequestParam("hospitalCode") String hospitalCode) {
		String packageDetails = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			packageDetails = hospitalSpecilityService.getSpecialityPackages(packageCode, hospitalCode);
			details.put("status", "success");
			details.put("details", packageDetails);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("status", e.getMessage());
		}
		return details;
	}

	@PostMapping(value = "/updatePackageSpecility")
	public Map<String, Object> updatePackageSpecility(@RequestBody HospitalSpecialistBean hospitalSpecialistBean) {
		Response response = new Response();
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			response = hospitalSpecilityService.updatePackageSpecility(hospitalSpecialistBean);
			details.put("status", "success");
			details.put("details", response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("msg", e.getMessage());
		}
		return details;
	}

	@ResponseBody
	@GetMapping(value = "/getsearchdataofhospitallspecilityapproval")
	public List<Object> getSerachDataHospitalSpecialityApprovalList(
			@RequestParam(value = "statecodeval", required = false) String statecodeval,
			@RequestParam(value = "districtcodeval", required = false) String districtcodeval,
			@RequestParam(value = "userId", required = false) String userId) {
		List<Object> approvalist = null;
		try {
			approvalist = hospitalSpecilityService.getSerachDataHospitalSpecialityApprovalList(statecodeval,
					districtcodeval, userId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"Exception occured in getSerachDataHospitalSpecialityApprovalList method of getSerachDataHospitalSpecialityApprovalList"
							+ e.getMessage());
		}
		return approvalist;
	}

	@ResponseBody
	@GetMapping(value = "/getsearchdataofhospitallspecdetailslist")
	public List<Object> getsearchdataofhospitallspecdetailslist(
			@RequestParam(value = "hospitalid", required = false) String hospitalid) {
		List<Object> existinglist = null;
		try {
			existinglist = hospitalSpecilityService.getsearchdataofhospitallspecdetailslist(hospitalid);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"Exception occured in getsearchdataofhospitallspecdetailslist method of getsearchdataofhospitallspecdetailslist"
							+ e.getMessage());
		}
		return existinglist;
	}

	@ResponseBody
	@GetMapping(value = "/getsearchdataofhospitallspecdetailstpendingcase")
	public List<Object> getsearchdataofhospitallspecdetailstpendingcase(
			@RequestParam(value = "hospitalidpending", required = false) String hospitalidpending) {
		List<Object> pendinglist = null;
		try {
			pendinglist = hospitalSpecilityService.getsearchdataofhospitallspecdetailstpendingcase(hospitalidpending);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"Exception occured in getsearchdataofhospitallspecdetailstpendingcase method of getsearchdataofhospitallspecdetailstpendingcase"
							+ e.getMessage());
		}
		return pendinglist;
	}

	@ResponseBody
	@PostMapping(value = "/getQcapprovalofhospitalspeciality")
	public Response getQcapprovalofhospitalspeciality(
			@RequestBody QcApprovalHospitalSpecialitybean qcApprovalHospitalSpecialitybean) {
		Response response = new Response();
		try {
			response = hospitalSpecilityService.SavegQcapprovalofhospitalspeciality(qcApprovalHospitalSpecialitybean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@ResponseBody
	@GetMapping(value = "/specialityapprovelist")
	public ResponseEntity<?> specialityapprovelist(@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "hospitalCode", required = false) String hospitalcode,
			@RequestParam(value = "statecode", required = false) String statecode,
			@RequestParam(value = "districtcode", required = false) String distcode,
			@RequestParam(value = "userid", required = false) Long userid) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			details.put("data",
					hospitalSpecilityService.specialityapprovelist(statecode, distcode, hospitalcode, type, userid));
			details.put("status", 200);
			details.put("message", "Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", 400);
			details.put("message", e.getMessage());
		}
		return new ResponseEntity(details, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/getschemepackagelistbyhospitalid")
	public Map<String, Object> hospitalreport(@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "schemeid", required = false) Integer schemeid) {
		Map<String, Object> list = new HashMap<>();
		try {
			list = hospitalSpecilityService.getschemepackagelistbyhospitalid(hospitalCode, schemeid);
			list.put("status", 200);
			list.put("message", "success");
		} catch (Exception e) {
			list.put("status", 400);
			list.put("message", "Error");
			list.put("errormessage", e.getMessage());
		}
		return list;
	}
	
	@PostMapping(value = "/updateschemepackage")
	public Map<String, Object> updateschemepackage(@RequestBody HospitalSpecialistBean hospitalSpecialistBean) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			System.out.println(hospitalSpecialistBean);
			details = hospitalSpecilityService.updateschemepackage(hospitalSpecialistBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fails");
			details.put("msg", e.getMessage());
		}
		return details;
	}
	
	@ResponseBody
	@GetMapping(value = "/getschemehospitalmappingrpt")
	public Map<String, Object> getschemehospitalmappingrpt(@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
			@RequestParam(value = "schemeid", required = false) Integer schemeid) {
		Map<String, Object> list = new HashMap<>();
		try {
			list = hospitalSpecilityService.getschemehospitalmappingrpt(state,dist,hospitalCode, schemeid);
			list.put("status", 200);
			list.put("message", "success");
		} catch (Exception e) {
			list.put("status", 400);
			list.put("message", "Error");
			list.put("errormessage", e.getMessage());
		}
		return list;
	}
}
