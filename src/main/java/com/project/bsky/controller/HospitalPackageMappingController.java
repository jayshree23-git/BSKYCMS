package com.project.bsky.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.HospitalMappingBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HospitalPackageMapping;
import com.project.bsky.model.PackageDetailsHospital;
import com.project.bsky.model.PackageSubCategory;
import com.project.bsky.service.HospitalPackageMappingService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class HospitalPackageMappingController {

	@Autowired
	private HospitalPackageMappingService hospitalPackageMappingService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/getPackageDetailsDescription")
	@ResponseBody
	public List<PackageDetailsHospital> getPackageDetailsDescription(
			@RequestParam(value = "packageSubcategoryId", required = false) Long packageSubcategoryId,
			@RequestParam(value = "hospitalcategoryid", required = false) Integer hospitalcategoryid) {

		return hospitalPackageMappingService.getpackageDetailsDescrition(packageSubcategoryId, hospitalcategoryid);
	}

	@PostMapping(value = "/saveHospitalPackageMapping")
	@ResponseBody
	public Response saveHospitalPackageMapping(@RequestBody HospitalMappingBean hospitalMappingBean) {
		return hospitalPackageMappingService.saveHospitalPackageMapping(hospitalMappingBean);
	}

	@GetMapping(value = "/getAllHospitalPackageMapping")
	@ResponseBody
	public List<HospitalPackageMapping> getAllHospitalPackageMapping() {
		return hospitalPackageMappingService.getAllHospitalPackageMapping();
	}

	@GetMapping(value = "/getByHospitalpackageMappingId/{id}")
	@ResponseBody
	public HospitalPackageMapping getByHospitalpackageMapping(@PathVariable(value = "id", required = false) Long id) {
		return hospitalPackageMappingService.getByHospitalpackageMapping(id);
	}

	@DeleteMapping(value = "/deleteHospitalMappingById/{id}")
	@ResponseBody
	public Response deleteHospitalMappingById(@PathVariable(value = "id", required = false) Long id) {
		return hospitalPackageMappingService.deleteHospitalMappingById(id);
	}

	@PutMapping(value = "/updateHospitalMappingById/{id}")
	@ResponseBody
	public Response updateHospitalMappingById(@RequestBody HospitalMappingBean hospitalMappingBean,
			@PathVariable(value = "id", required = false) Long id) {
		return hospitalPackageMappingService.updateHospitalMappingById(id, hospitalMappingBean);
	}

	@GetMapping(value = "/getPackageSubcategory")
	@ResponseBody
	public List<PackageSubCategory> getPackageSubcategory(@RequestParam String packageheadercode) {
		return hospitalPackageMappingService.getPackageSubcategory(packageheadercode);
	}

	@GetMapping(value = "/getPackageDetailByCode")
	@ResponseBody
	public ResponseEntity<?> getPackageDetailByCode(@RequestParam String packageCode,
			@RequestParam String subPackageCode, @RequestParam String procedureCode,
			@RequestParam String hospitalCode) {
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			Map<String, Object> list = hospitalPackageMappingService.getPackageDetailByCode(packageCode, subPackageCode,
					procedureCode, hospitalCode);
			details.put("status", "success");
			details.put("data", list.get("packageInfo"));
			details.put("data1", list.get("overallInfo"));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}

	@GetMapping(value = "/getAuthenticationDetails")
	@ResponseBody
	public ResponseEntity<?> getAuthenticationDetails(@RequestParam String Urn, @RequestParam String memberid,
			@RequestParam Integer flag, @RequestParam String Hospitalcode,@RequestParam (value = "caseno", required = false) String caseno) {
		String list = null;
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			list = hospitalPackageMappingService.getAuthenticationDetails(Urn, memberid, flag, Hospitalcode,caseno);
			data.put("status", "success");
			data.put("data", list);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			data.put("status", "fail");
			data.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(data);
	}

	@GetMapping(value = "/getOverridecodeDetails")
	@ResponseBody
	public ResponseEntity<?> getOverridecodeDetails(@RequestParam String overridecode, @RequestParam String Urn,
			@RequestParam Long memberid, @RequestParam String hospitalcode) {
		String list = null;
		Map<String, Object> ovveridedetails = new HashMap<String, Object>();
		try {
			list = hospitalPackageMappingService.getOverridecodeDetails(overridecode, Urn, memberid, hospitalcode);
			ovveridedetails.put("status", "success");
			ovveridedetails.put("data", list);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			ovveridedetails.put("status", "fail");
			ovveridedetails.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(ovveridedetails);
	}
}
