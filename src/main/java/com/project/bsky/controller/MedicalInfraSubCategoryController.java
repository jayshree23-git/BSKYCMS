package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.MedicalInfraCategory;
import com.project.bsky.model.MedicalInfraSubCategory;
import com.project.bsky.service.MedicalInfraCategoryMasterService;
import com.project.bsky.service.MedicalInfraSubCategoryService;

@RestController
@RequestMapping(value = "/api")
public class MedicalInfraSubCategoryController {

	@Autowired
	private MedicalInfraCategoryMasterService medicalInfraCategoryMasterService;

	@Autowired
	private MedicalInfraSubCategoryService medicalInfraSubCategoryService;

	@ResponseBody
	@GetMapping(value = "/getMedicalInfraCategorylist")
	public List<MedicalInfraCategory> getCategorylist() {

		List<MedicalInfraCategory> categorylist = null;
		try {
			categorylist = medicalInfraCategoryMasterService.getActiveCategory();
			return categorylist;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categorylist;
	}

	@ResponseBody
	@GetMapping(value = "/saveMedicalInfraSubCategory")
	public Response addPackageMasterData(@RequestParam(required = false, value = "medInfracatId") Integer medInfracatId,
			@RequestParam(required = false, value = "medInfraSubCatName") String medInfraSubCatName,
			@RequestParam(required = false, value = "createdBy") Integer createdBy) {
		Response returnObj = null;
		try {
			returnObj = medicalInfraSubCategoryService.saveDetails(medInfracatId, medInfraSubCatName, createdBy);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj;
	}

	@ResponseBody
	@GetMapping(value = "/getMedicalInfraSubcatList")
	public List<MedicalInfraSubCategory> getSubCategorylist() {

		List<MedicalInfraSubCategory> subcategorylist = null;
		try {
			subcategorylist = medicalInfraSubCategoryService.getDetails();
			return subcategorylist;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return subcategorylist;
	}

	@ResponseBody
	@GetMapping(value = "/getsubcategorybyid")
	public MedicalInfraSubCategory getDatabyid(
			@RequestParam(value = "medInfraSubCatId", required = false) Integer medInfraSubCatId) {

		return medicalInfraSubCategoryService.getbyid(medInfraSubCatId);
	}

	@ResponseBody
	@GetMapping(value = "/updateMedicalInfraSubCategory")
	public Response updateMedicalInfraSubCat(
			@RequestParam(required = false, value = "medInfracatId") Integer medInfracatId,
			@RequestParam(required = false, value = "medInfraSubCatId") Integer medInfraSubCatId,
			@RequestParam(required = false, value = "medInfraSubCatName") String medInfraSubCatName,
			@RequestParam(required = false, value = "updatedBy") Integer updatedBy,
			@RequestParam(required = false, value = "statusFlag") Integer statusFlag) {

		Response returnObj = null;
		try {
			returnObj = medicalInfraSubCategoryService.updateMedicalInfraSubCat(medInfracatId, medInfraSubCatId,
					medInfraSubCatName, updatedBy, statusFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnObj;
	}

	@GetMapping(value = "/getMedicalInfraSubcatListById")
	public List<Map<String, Object>> getMedicalInfraSubcatListById(
			@RequestParam(value = "categoryId") Integer categoryId) {

		List<Map<String, Object>> subcategorylist = new ArrayList<>();
		try {
			subcategorylist = medicalInfraSubCategoryService.getMedicalInfraSubcatListById(categoryId);
			return subcategorylist;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return subcategorylist;
	}

}
