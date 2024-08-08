package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.MedicalInfraCategory;
import com.project.bsky.model.MedicalInfraSubCategory;
import com.project.bsky.model.UserDetails;
import com.project.bsky.repository.MedicalInfraCategoryRepository;
import com.project.bsky.repository.MedicalInfraSubCategoryRepository;
import com.project.bsky.repository.UserDetailsRepository;
import com.project.bsky.service.MedicalInfraSubCategoryService;

@Service
public class MedicalInfraSubCategoryServiceImpl implements MedicalInfraSubCategoryService {

	@Autowired
	MedicalInfraSubCategoryRepository medicalInfraSubCategoryRepository;

	@Autowired
	MedicalInfraCategoryRepository medicalInfraCategoryRepository;

	@Autowired
	private UserDetailsRepository userdetailsrepo;

	@Override
	public Response saveDetails(Integer medInfracatId, String medInfraSubCatName, Integer createdBy) {
		Response response = new Response();
		try {
			Calendar calendar = Calendar.getInstance();
			String catname = medicalInfraCategoryRepository.findBycatId(medInfracatId);
			Integer countsubcategoryname = medicalInfraSubCategoryRepository
					.checkduplicateSubCategoryname(medInfraSubCatName, medInfracatId);
			if (countsubcategoryname == 0) {

				MedicalInfraSubCategory medicalInfraSubCategory = new MedicalInfraSubCategory();
				medicalInfraSubCategory.setMedInfraSubCatName(medInfraSubCatName);
				medicalInfraSubCategory.setStatusFlag(0);
				medicalInfraSubCategory.setCreatedOn(calendar.getTime());
				UserDetails userdetails = userdetailsrepo.findById((long) createdBy).get();
				medicalInfraSubCategory.setUserId(userdetails);
				MedicalInfraCategory medicalInfraCategory = new MedicalInfraCategory();
				medicalInfraCategory = medicalInfraCategoryRepository.findById(medInfracatId).get();
				medicalInfraSubCategory.setMedInfracatId(medicalInfraCategory);

				medicalInfraSubCategoryRepository.save(medicalInfraSubCategory);
				response.setStatus("Success");
				response.setMessage("Medical Infra SubCategory Successfully Saved");
			} else {
				response.setStatus("Failed");
				response.setMessage("This SubCategory is Already Added in " + catname + " Category");
			}
		} catch (Exception e) {
			System.out.println(e);
			response.setStatus("Failed");
			response.setMessage("Something Went Wrong");
		}
		return response;

	}

	@Override
	public List<MedicalInfraSubCategory> getDetails() {
		List<MedicalInfraSubCategory> list = new ArrayList<>();
		try {
			list = medicalInfraSubCategoryRepository.getDetails();
			for (MedicalInfraSubCategory x : list) {
				x.setScreatedate(x.getCreatedOn().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public MedicalInfraSubCategory getbyid(Integer medInfraSubCatId) {

		MedicalInfraSubCategory medicalInfraSubCategory = null;
		try {
			medicalInfraSubCategory = medicalInfraSubCategoryRepository.findById(medInfraSubCatId).get();
		} catch (Exception e) {

		}
		return medicalInfraSubCategory;
	}

	@Override
	public Response updateMedicalInfraSubCat(Integer medInfracatId, Integer medInfraSubCatId, String medInfraSubCatName,
			Integer updatedBy, Integer statusFlag) {

		Response response = new Response();
		try {
			Calendar calendar = Calendar.getInstance();
			String catname = medicalInfraCategoryRepository.findBycatId(medInfracatId);
			System.out.println(catname);
			MedicalInfraSubCategory subCat = medicalInfraSubCategoryRepository
					.findbyMedInfraSubCatName(medInfraSubCatName, medInfracatId);
			Integer countsubcategoryname = medicalInfraSubCategoryRepository
					.checkduplicateSubCategoryname(medInfraSubCatName, medInfracatId);
			if (countsubcategoryname == 0 || (subCat.getMedInfraSubCatId().equals(medInfraSubCatId)
					&& subCat.getMedInfraSubCatName().equals(medInfraSubCatName))) {

				MedicalInfraSubCategory medicalInfraSubCategory = medicalInfraSubCategoryRepository
						.findById(medInfraSubCatId).get();
				medicalInfraSubCategory.setMedInfraSubCatName(medInfraSubCatName);
				medicalInfraSubCategory.setStatusFlag(statusFlag);
				medicalInfraSubCategory.setUpdatedOn(calendar.getTime());
				medicalInfraSubCategory.setUpdatedBy(updatedBy);
				MedicalInfraCategory medicalInfraCategory = new MedicalInfraCategory();
				medicalInfraCategory = medicalInfraCategoryRepository.findById(medInfracatId).get();
				medicalInfraSubCategory.setMedInfracatId(medicalInfraCategory);

				medicalInfraSubCategoryRepository.save(medicalInfraSubCategory);
				response.setStatus("Success");
				response.setMessage("Medical Infra SubCategory Successfully Updated");
			} else {
				response.setStatus("Failed");
				response.setMessage("This SubCategory is Already Exist in " + catname + " Category");
			}
		} catch (Exception e) {
			response.setStatus("Failed");
			response.setMessage("Something Went Wrong");
			e.printStackTrace();
		}
		return response;

	}

	@Override
	public List<Map<String, Object>> getMedicalInfraSubcatListById(Integer categoryId) {
		List<Map<String, Object>> medicalInfraSubCategory = new ArrayList<>();
		try {
			medicalInfraSubCategory = medicalInfraSubCategoryRepository.getMedicalInfraSubcatListById(categoryId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return medicalInfraSubCategory;
	}

}
