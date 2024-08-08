package com.project.bsky.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.DuplicateCheck;
import com.project.bsky.bean.Response;
import com.project.bsky.model.MedicalExpertiseModel;
import com.project.bsky.model.TypeOfExpertiseModel;
import com.project.bsky.service.EmpanelmentMasterService;

/**
 * @author jayshree.moharana
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/empMaster")
public class EmpanelmentMasterController {

	private final Logger logger;

	public EmpanelmentMasterController(Logger logger) {
		this.logger = logger;
	}

	@Autowired
	private EmpanelmentMasterService empanelmentMasterService;

	@GetMapping(value = "/getmedicalexpertiselist")
	@ResponseBody
	public List<MedicalExpertiseModel> getmedicalexpertiselist() {
		return empanelmentMasterService.getmedicalexpertiseData();
	}

	@ResponseBody
	@GetMapping(value = "/getEmpanelmentMasterDetails")
	public ResponseEntity<?> getEmpanelmentMasterDetails
	(@RequestParam(value = "flag") String flag, 
			@RequestParam(value = "queryParam") String queryParam) 
	{
		logger.info("Inside getEmpanelmentMasterDetails() Method of EmpanelmentMasterController.");
		Map<String, Object> details = new LinkedHashMap<>();
		try {
			List<Map<String, Object>> response = empanelmentMasterService.getEmpanelmentMasterDetails(flag, queryParam);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error("Exception Occurred in getEmpanelmentMasterDetails() Method of EmpanelmentMasterController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}
	
	@ResponseBody
	@GetMapping(value = "/getBankIFSCDetails")
	public ResponseEntity<?> getBankIFSCDetails(@RequestParam(value = "bankName") String bankName,
			@RequestParam(value = "districtName") String districtName) {
		logger.info("Inside getBankIFSCDetails() Method of EmpanelmentMasterController.");
		Map<String, Object> details = new LinkedHashMap<>();
		try {
			List<Map<String, Object>> response = empanelmentMasterService.getBankIFSCDetails(bankName, districtName);
			details.put("status", "success");
			details.put("data", response);
		} catch (Exception e) {
			logger.error("Exception Occurred in getBankIFSCDetails() Method of EmpanelmentMasterController : "
					+ e.getMessage());
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@ResponseBody
	@PostMapping(value = "/savemedicalexpertisedata")
	public Response addmedicalexpertise(@RequestBody MedicalExpertiseModel expertiseModel) {
		Response returnObj = null;
		try {
			returnObj = empanelmentMasterService.savemedicalexpertisedata(expertiseModel);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj;
	}

	@ResponseBody
	@GetMapping(value = "/getmedicalexpertiseData")
	public List<MedicalExpertiseModel> getQueryTypeList() {

		List<MedicalExpertiseModel> medicalexpertisemodel = null;
		try {
			medicalexpertisemodel = empanelmentMasterService.getmedicalexpertiseData();
			return medicalexpertisemodel;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return medicalexpertisemodel;
	}

	@GetMapping(value = "/getmedicalexpertiseDataById")
	@ResponseBody
	public MedicalExpertiseModel getmedicalexpertiseDataById(
			@RequestParam(value = "userid", required = false) Long userid) {
		return empanelmentMasterService.getmedicalexpertiseDataById(userid);
	}

	@ResponseBody
	@PostMapping(value = "/updateMedicalexpertiseData")
	public Response updateMedicalexpertiseData(@RequestBody MedicalExpertiseModel medicalexpertisemodel) {
		Response returnObj = null;
		try {
			returnObj = empanelmentMasterService.updateMedicalexpertise(medicalexpertisemodel);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj;
	}

	@ResponseBody
	@GetMapping(value = "/getMedicalexpname")
	public List<MedicalExpertiseModel> getMedicalexpname() {
		return empanelmentMasterService.getmedicalexpname();
	}

	@ResponseBody
	@GetMapping(value = "/saveTypeofExpertise")
	public Integer saveTypeofExpertise(@RequestParam(required = false, value = "medicalexpid") long medicalexpid,
			@RequestParam(required = false, value = "typeofexpertise") String typeofexpertise,
			@RequestParam(required = false, value = "createdby") String createdby) {
		return empanelmentMasterService.saveTypeofExpertise(medicalexpid, typeofexpertise, createdby);
	}

	@ResponseBody
	@GetMapping(value = "/getExpertisetypeData")
	public List<TypeOfExpertiseModel> getExpertisetypeData() {
		return empanelmentMasterService.getExpertisetypeData();
	}

	@ResponseBody
	@GetMapping(value = "/Delete")
	public Integer delete(@RequestParam(required = false, value = "typeofexpertiseid") long typeofexpertiseid) {
		return empanelmentMasterService.delete(typeofexpertiseid);
	}

	@ResponseBody
	@GetMapping(value = "/getbyid")
	public TypeOfExpertiseModel getbyid(
			@RequestParam(required = false, value = "typeofexpertiseid") long typeofexpertiseid) {
		return empanelmentMasterService.getbyid(typeofexpertiseid);
	}

	@ResponseBody
	@GetMapping(value = "/updateexpertisetype")
	public Integer updateexpertisetype(@RequestParam(required = false, value = "medicalexpid") long medicalexpid,
			@RequestParam(required = false, value = "typeofexpertise") String typeofexpertisename,
			@RequestParam(required = false, value = "updateby") String updateby,
			@RequestParam(required = false, value = "typeofexpertiseid") long typeofexpertiseid,
			@RequestParam(required = false, value = "status") Integer status) {
		return empanelmentMasterService.update(medicalexpid, typeofexpertisename, updateby, typeofexpertiseid,
				status);
	}
	
	@PostMapping(value = "/checkDuplicateEmpHsptlInfo")
	@ResponseBody
	public Map<String, Object> checkduplicate(@RequestBody DuplicateCheck duplicateCheck){
		return empanelmentMasterService.checkduplicate(duplicateCheck);
	}
}
