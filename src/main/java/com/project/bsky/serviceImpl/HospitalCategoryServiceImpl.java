package com.project.bsky.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.model.HospitalCategoryMaster;
import com.project.bsky.repository.HospitalCategoryMasterRepository;
import com.project.bsky.service.HospitalCategoryService;

/**
 * @author ronauk
 *
 */
@Service
public class HospitalCategoryServiceImpl implements HospitalCategoryService {

	@Autowired
	private HospitalCategoryMasterRepository categoryRepo;
	
	@Override
	public HospitalCategoryMaster findCategoryNameById(Long categoryId) {
		// TODO Auto-generated method stub
		return categoryRepo.findById(categoryId).get();
	}

}
