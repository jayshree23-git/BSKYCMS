package com.project.bsky.controller;

import java.util.List;

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

import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageDetailsHospital;
import com.project.bsky.model.Ward;
import com.project.bsky.service.WardService;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")

public class WardController {

	@Autowired
	private WardService wardService;

	@Autowired
	private Logger logger;

	@PostMapping(value = "/saveward")
	@ResponseBody
	public Response saveWard(@RequestBody Ward ward) {
		return wardService.saveWard(ward);
	}

	@GetMapping(value = "/getallward")
	@ResponseBody
	public List<Ward> getward() {
		return wardService.getward();
	}

	@DeleteMapping(value = "/deleteward/{wardMasterId}")
	@ResponseBody
	public Response deleteWard(@PathVariable(value = "wardMasterId", required = false) Long wardMasterId) {
		return wardService.deleteWard(wardMasterId);
	}

	@GetMapping(value = "/getbyward/{wardMasterId}")
	@ResponseBody
	public Ward getbywardMasterId(@PathVariable(value = "wardMasterId", required = false) Long wardMasterId) {
		return wardService.getbywardMasterId(wardMasterId);
	}

	@PutMapping(value = "/updateward/{wardMasterId}")
	@ResponseBody
	public Response updateward(@RequestBody Ward ward,
			@PathVariable(value = "wardMasterId", required = false) Long wardMasterId) {
		return wardService.update(wardMasterId, ward);
	}

	@ResponseBody
	@GetMapping(value = "/checkDuplicatewardname")
	public ResponseEntity<Response> checkDuplicateWardName(
			@RequestParam(value = "wardName", required = false) String wardName, Response response) {
		try {
			Long wardMasterIdLong = wardService.checkDuplicateWardName(wardName);

			if (wardMasterIdLong != null)
				response.setStatus("Present");
			else
				response.setStatus("Absent");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@GetMapping(value = "/checkDuplicatewardcode")
	public ResponseEntity<Response> checkDuplicateWardCode(
			@RequestParam(value = "wardCode", required = false) String wardCode, Response response) {
		try {
			Long wardMasterIdLong = wardService.checkDuplicateWardCode(wardCode);

			if (wardMasterIdLong != null)
				response.setStatus("Present");
			else
				response.setStatus("Absent");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/getPackageDescription")
	@ResponseBody
	public List<PackageDetailsHospital> getPackageDetailsDescription(@RequestParam Integer hospitalCategoryId) {

		return wardService.getpackageDetailsDescrition(hospitalCategoryId);
	}
}
