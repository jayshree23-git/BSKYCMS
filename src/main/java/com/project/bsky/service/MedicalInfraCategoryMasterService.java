package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.Response;
import com.project.bsky.model.MedicalInfraCategory;

public interface MedicalInfraCategoryMasterService {

	Response saveDetails(MedicalInfraCategory medicalInfraCategory);

	List<MedicalInfraCategory> getDetails();

	MedicalInfraCategory getbyid(Integer medInfracatId);

	Response updateCategory(MedicalInfraCategory medicalInfraCategory);

	List<MedicalInfraCategory> getActiveCategory();

	List<Map<String, Object>> getMedicalInfraCategoryList();

}
