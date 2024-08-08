package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.MedicalInfraCategory;
import com.project.bsky.service.MedicalInfraCategoryMasterService;

@RestController
@RequestMapping(value = "/api")
public class MedicalInfraCategoryMasterController {

	@Autowired
	private MedicalInfraCategoryMasterService medicalInfraCategoryMasterService;

	@ResponseBody
	@PostMapping(value = "/saveMedicalInfraCategory")
	public Response addPackageMasterData(@RequestBody MedicalInfraCategory medicalInfraCategory) {
		Response returnObj = null;
		System.out.println("fufyguhu");
		try {
			returnObj = medicalInfraCategoryMasterService.saveDetails(medicalInfraCategory);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj;
	}

	@ResponseBody
	@GetMapping(value = "/getMedicalInfracatList")
	public List<MedicalInfraCategory> getCategorylist() {

		List<MedicalInfraCategory> categorylist = null;
		try {
			categorylist = medicalInfraCategoryMasterService.getDetails();

			return categorylist;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categorylist;
	}

	@ResponseBody
	@GetMapping(value = "/getcategorybyid")
	public MedicalInfraCategory getDatabyid(
			@RequestParam(value = "medInfracatId", required = false) Integer medInfracatId) {

		return medicalInfraCategoryMasterService.getbyid(medInfracatId);

	}

	@ResponseBody
	@PostMapping(value = "/updateMedicalinfracat")
	public Response updateCategoryData(@RequestBody MedicalInfraCategory medicalInfraCategory) {
		Response returnObj = null;
		try {
			returnObj = medicalInfraCategoryMasterService.updateCategory(medicalInfraCategory);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj;
	}

	@ResponseBody
	@GetMapping(value = "/getMedicalInfraCategoryList")
	public List<Map<String, Object>> getMedicalInfraCategoryList() {

		List<Map<String, Object>> categorylist = new ArrayList<>();
		try {
			categorylist = medicalInfraCategoryMasterService.getMedicalInfraCategoryList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categorylist;
	}

}
