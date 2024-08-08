package com.project.bsky.service;

import com.project.bsky.model.HospitalCategoryMaster;

/**
 * @author ronauk
 *
 */
public interface HospitalCategoryService {
	
	HospitalCategoryMaster findCategoryNameById(Long categoryId);

}
