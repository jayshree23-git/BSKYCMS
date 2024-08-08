package com.project.bsky.service;

import java.util.List;

//import com.project.bsky.bean.PackageSubCatagoryDto;
import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageSubCategory;

public interface PackageSubCategoryService {

	Response savepackagesubcategory(PackageSubCategory packageSubCatagory);

	List<PackageSubCategory> getpackageSubCatagory();


	PackageSubCategory getbypackagesubcategory(Long subcategoryId);

	Response deletepackagesubcategory(Long subcategoryId);

	Response update(Long subcategoryId, PackageSubCategory packageSubCategory);



	Long checkDuplicateSubCategoryName(String packagesubcategoryname);

	Long checkDuplicateSubCategoryCode(String packagesubcategorycode);

	Response activepackagesubcatagory(Long subcategory, Long userid);



}
