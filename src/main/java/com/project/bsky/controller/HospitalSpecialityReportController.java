package com.project.bsky.controller;

import java.util.ArrayList;
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

import com.project.bsky.bean.HospitalProcedurePackageBean;
import com.project.bsky.bean.HospitalProcedureTagging;
import com.project.bsky.bean.HospitalSpecialistListBean;
import com.project.bsky.bean.PackageTaggingReportBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.HospitalSpecialityReportService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class HospitalSpecialityReportController {
	@Autowired
	private HospitalSpecialityReportService hospitalSpecialityreportservice;

	@Autowired
	private Logger logger;

	@ResponseBody
	@GetMapping(value = "/gethospitallist")
	public List<HospitalSpecialistListBean> hospitalreport(
			@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist,
			@RequestParam(value = "hospital", required = false) String hospital,
			@RequestParam(value = "actioncode", required = false) Long actioncode) {
		List<HospitalSpecialistListBean> list = new ArrayList<>();
		try {
			list = hospitalSpecialityreportservice.gethospitalinfo(userid, actioncode, state, dist, hospital);
			
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

	@ResponseBody
	@RequestMapping("/getPackageHeaderCode")
	public List<Map<String, Object>> getPackageHeaderCode() {
		List<Map<String, Object>> map = new ArrayList<>();
		try {
			map = hospitalSpecialityreportservice.getPackageHeaderCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@ResponseBody
	@PostMapping(value = "/taggedprocedurelist")
	public List<HospitalProcedureTagging> procedureTaggingDetails(
			@RequestBody HospitalProcedurePackageBean packageBean) {
		List<HospitalProcedureTagging> list = new ArrayList<>();
		try {
			list = hospitalSpecialityreportservice.procedureTaggingDetails(packageBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			HospitalProcedureTagging bean = new HospitalProcedureTagging();
			bean.setStatus("failed");
			bean.setErrorMessage(ExceptionUtils.getStackTrace(e));
			list.add(bean);
		}
		return list;
	}

	@ResponseBody
	@PostMapping(value = "/submitTaggedProcedure")
	public Response submitTaggedProcedure(@RequestBody HospitalProcedurePackageBean packageBean) {
		Response response = new Response();
		try {
			response = hospitalSpecialityreportservice.submitTaggedProcedure(packageBean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setStatus("failed");
			response.setMessage(ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/gettaggedpackagedetails")
	public List<PackageTaggingReportBean> packageTaggingReport(
			@RequestParam(value = "stateId", required = false) String stateId,
			@RequestParam(value = "districtId", required = false) String districtId,
			@RequestParam(value = "hospitalId", required = false) String hospitalId,
			@RequestParam(value = "taggedType", required = false) Long taggedType) {
		List<PackageTaggingReportBean> list = new ArrayList<>();
		try {
			list = hospitalSpecialityreportservice.packageTaggingReport(stateId, districtId, hospitalId, taggedType);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

}
