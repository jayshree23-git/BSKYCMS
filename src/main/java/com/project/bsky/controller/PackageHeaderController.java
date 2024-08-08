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
import com.project.bsky.bean.Submitspecialitybean;
import com.project.bsky.model.PackageHeader;
import com.project.bsky.service.PackageHeaderService;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")
public class PackageHeaderController {

	@Autowired
	private PackageHeaderService packageHeaderService;

	@Autowired
	private Logger logger;

	@PostMapping(value = "/savepackageheader")
	@ResponseBody
	public Response savepackageHeader(@RequestBody PackageHeader packageHeader) {
		return packageHeaderService.savepackageHeader(packageHeader);
	}

	@GetMapping(value = "/getpackageheader")
	@ResponseBody
	public List<PackageHeader> getpackageHeader() {
		return packageHeaderService.getpackageHeader();
	}

	@DeleteMapping(value = "/deletepackageheader/{headerId}")
	@ResponseBody
	public Response deletepackageheader(@PathVariable(value = "headerId", required = false) Long headerId) {
		return packageHeaderService.deletepackageheader(headerId);
	}
	
	@GetMapping(value = "/activepackageheader")
	@ResponseBody
	public Response activepackageheader(@RequestParam(value = "headerId", required = false) Long headerId,
			@RequestParam(value = "userid", required = false) Long userid) {
		return packageHeaderService.activepackageheader(headerId,userid);
	}

	@GetMapping(value = "/getbypackageheader/{headerId}")
	@ResponseBody
	public PackageHeader getbypackageHeader(@PathVariable(value = "headerId", required = false) Long headerId) {
		return packageHeaderService.getbypackageHeader(headerId);
	}

	@PutMapping(value = "/updatepackageheader/{headerId}")
	@ResponseBody
	public Response updatepackageheader(@RequestBody PackageHeader packageHeader,
			@PathVariable(value = "headerId", required = false) Long headerId) {
		return packageHeaderService.update(headerId, packageHeader);
	}

	@ResponseBody
	@GetMapping(value = "/checkDuplicateheadername")
	public ResponseEntity<Response> checkDuplicateHeaderName(
			@RequestParam(value = "packageheadername", required = false) String packageheadername, Response response) {
		try {
			Long headerIdLong = packageHeaderService.checkPackageHeaderName(packageheadername);

			if (headerIdLong != null)
				response.setStatus("Present");
			else
				response.setStatus("Absent");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@GetMapping(value = "/checkDuplicatepackageheadercode")
	public ResponseEntity<Response> checkDuplicatePackageheadercode(
			@RequestParam(value = "packageheadercode", required = false) String packageheadercode, Response response) {
		try {
			Long headerIdLong = packageHeaderService.checkDuplicatePackageheadercode(packageheadercode);

			if (headerIdLong != null)
				response.setStatus("Present");
			else
				response.setStatus("Absent");

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);
	}

	@ResponseBody
	@GetMapping(value = "/getpackageheaderforspeciality")
	public List<Object> getpackageheaderforspeciality() {
		List<Object> packageList = null;
		try {
			packageList = packageHeaderService.getAllpackageheaderdata();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return packageList;
	}

	@ResponseBody
	@GetMapping(value = "/getpackagesubcategoryforspeciality")
	public List<Object> getpackagesubcategoryforspeciality(
			@RequestParam(value = "packageheadercode", required = false) String packageheadercode) {
		List<Object> packagesubList = null;
		try {
			packagesubList = packageHeaderService.getAllpackagesubctaegorydata(packageheadercode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return packagesubList;
	}

	@ResponseBody
	@GetMapping(value = "/getprocedurecodeforspeciality")
	public List<Object> getprocedurecodeforspeciality(
			@RequestParam(value = "packagesubcode", required = false) String packagesubcode) {
		List<Object> procedureubList = null;
		try {
			procedureubList = packageHeaderService.getAllprocedurecodedata(packagesubcode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return procedureubList;
	}

	@GetMapping(value = "/getviewspeciality")
	@ResponseBody
	public List<Object> getviewspeciality(
			@RequestParam(value = "packageheadercode", required = false) String packageheadercode,
			@RequestParam(value = "packagesubcode", required = false) String packagesubcode,
			@RequestParam(value = "procedurecode", required = false) String procedurecode,
			@RequestParam(value = "searchtype", required = false) Integer searchtype) {
		List<Object> viewlist = null;
		try {
			viewlist = packageHeaderService.getviewspecialitydetails(packageheadercode, packagesubcode, procedurecode,
					searchtype);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return viewlist;
	}

	@PostMapping(value = "/savespecilaity")
	public ResponseEntity<Response> savespecilaity(@RequestBody Submitspecialitybean resbean) {
		Response response = null;
		try {
			response = packageHeaderService.savespecilaityRequest(resbean);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/getpackagedetailsdata")
	@ResponseBody
	public List<Object> getpackagedetailslist(
			@RequestParam(value = "procedurecode", required = false) String procedurecode) {
		List<Object> list = null;
		try {
			list = packageHeaderService.getpackagedetailslist(procedurecode);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}

}
