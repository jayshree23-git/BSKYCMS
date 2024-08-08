package com.project.bsky.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageMasterBSKY;
import com.project.bsky.service.PackageMasterService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class PackageMasterController {

	@Autowired
	private PackageMasterService packageMasterService;

	@Autowired
	private Logger logger;

	@ResponseBody
	@PostMapping(value = "/savePackageMasterData")
	public Response addPackageMasterData(@RequestBody PackageMasterBSKY packageMasterBSKY) {
		Response returnObj = null;
		try {
			returnObj = packageMasterService.savePackageMasterData(packageMasterBSKY);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

	@ResponseBody
	@GetMapping(value = "/getPackageMasterData")
	public List<PackageMasterBSKY> getQueryTypeList() {

		List<PackageMasterBSKY> packageMasterDetails = null;
		try {
			packageMasterDetails = packageMasterService.getDetails();
			return packageMasterDetails;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return packageMasterDetails;
	}

	@GetMapping(value = "/getPackageDataById")
	@ResponseBody
	public PackageMasterBSKY getgloballinkbyid(@RequestParam(value = "userid", required = false) Long userid) {
		return packageMasterService.getbyId(userid);
	}

	@ResponseBody
	@PostMapping(value = "/updatePackageMasterData")
	public Response updatePackageMasterData(@RequestBody PackageMasterBSKY packageMasterBSKY) {
		Response returnObj = null;
		try {
			returnObj = packageMasterService.updatePackageData(packageMasterBSKY);

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return returnObj;
	}

	@GetMapping(value = "/getPackageDetailsByProcedure")
	public ResponseEntity<?> getPackageDetailsByProcedure(@RequestParam("procedureCode") String procedureCode) {

		String packageMasterDetails = null;
		Map<String, Object> details = new HashedMap<>();
		try {
			packageMasterDetails = packageMasterService.getPackageByProcedure(procedureCode);
			details.put("status", "success");
			details.put("data", packageMasterDetails);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return ResponseEntity.ok(details);
	}
}
