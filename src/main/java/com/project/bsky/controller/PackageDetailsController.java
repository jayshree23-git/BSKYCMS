package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.bsky.bean.PackageDetailsMasterBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HospitalCategoryMaster;
import com.project.bsky.model.PackageDetailsMaster;
import com.project.bsky.repository.HospitalCategoryMasterRepository;
import com.project.bsky.service.PackageDetailsService;

@Controller
@RequestMapping(value = "/api")
public class PackageDetailsController {

	@Autowired
	private HospitalCategoryMasterRepository hospitalCategoryMasterRepository;

	@Autowired
	private PackageDetailsService packageDetailsService;

	@Autowired
	private Logger logger;

	@GetMapping(value = "/gethospitalcategory")
	@ResponseBody
	public List<HospitalCategoryMaster> getHospitalCategory() {
		List<HospitalCategoryMaster> headerResponse = new ArrayList<>();
		List<HospitalCategoryMaster> findAll = hospitalCategoryMasterRepository.findAll();
		try {
			if (findAll != null) {
				for (HospitalCategoryMaster hospitalCategoryMaster : findAll) {
					if (hospitalCategoryMaster != null && hospitalCategoryMaster.getDeletedFlag() == 0) {
						headerResponse.add(hospitalCategoryMaster);
					}
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return headerResponse;
	}

	@PostMapping(value = "/savepackagedetails")
	@ResponseBody
	public Response savePackageDetail(@RequestBody PackageDetailsMasterBean packageDetailsMasterBean) {
		return packageDetailsService.savePackageDetails(packageDetailsMasterBean);
	}

	@ResponseBody
	@GetMapping(value = "/getpackagedetails")
	public List<PackageDetailsMaster> getPackageDetail() {
		List<PackageDetailsMaster> packagedetailList = null;
		Map<String, Object> details = new HashMap<String, Object>();
		try {
			packagedetailList = packageDetailsService.getPackageDetails();
			details.put("status", "success");
			details.put("data", packagedetailList);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			details.put("status", "fail");
			details.put("msg", e.getMessage());
		}
		return packagedetailList;
	}

	@GetMapping(value = "/getbypackagedetailsid/{id}")
	@ResponseBody
	public String getByPackageDetailId(@PathVariable(value = "id", required = false) Long id) {
		return packageDetailsService.getByPackageDetailsIds(id);
	}

	@ResponseBody
	@DeleteMapping(value = "/deletepackagedetails/{id}")
	public Response deletePackageDetail(@PathVariable(value = "id", required = false) Long id) {
		return packageDetailsService.deletePackageDetails(id);
	}

	@ResponseBody
	@PutMapping(value = "/updatepackagedetails/{id}")
	public Response updatePackageDetail(@RequestBody PackageDetailsMasterBean packageDetailsMasterBean,
			@PathVariable(value = "id", required = false) Long id) {
		return packageDetailsService.updatePackageDetails(id, packageDetailsMasterBean);
	}

}
