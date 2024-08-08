package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.Response;
import com.project.bsky.model.MedicalInfraCategory;
import com.project.bsky.model.MedicalInfraSubCategory;

public interface MedicalInfraSubCategoryService {

	//Response saveDetails(MedicalInfraSubCategory medicalInfraSubCategory);

	Response saveDetails(Integer medInfracatId, String medInfraSubCatName, Integer createdBy);

	List<MedicalInfraSubCategory> getDetails();

	MedicalInfraSubCategory getbyid(Integer medInfraSubCatId);

//	Response updateMedicalInfraSubCat(Integer medInfracatId, String medInfraSubCatName, String updatedBy,
//			Integer statusFlag);

	Response updateMedicalInfraSubCat(Integer medInfracatId,Integer medInfraSubCatId, String medInfraSubCatName, Integer updatedBy,
			Integer statusFlag);

	List<Map<String, Object>> getMedicalInfraSubcatListById(Integer categoryId);

}
