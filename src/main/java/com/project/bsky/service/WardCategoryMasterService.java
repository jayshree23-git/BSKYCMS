package com.project.bsky.service;

import java.util.List;

import com.project.bsky.model.PackageDetailsHospital;
import com.project.bsky.model.WardCategoryMaster;

public interface WardCategoryMasterService {

	List<WardCategoryMaster> getwardCategory();

	//List<PackageDetailsHospital> getpackageDetailsDescrition(String packageheadercode);

}
