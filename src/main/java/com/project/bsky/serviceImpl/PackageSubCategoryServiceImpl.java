package com.project.bsky.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageHeader;
import com.project.bsky.model.PackageSubCategory;
import com.project.bsky.repository.PackageSubCategoryRepository;
import com.project.bsky.service.PackageSubCategoryService;

@Service
public class PackageSubCategoryServiceImpl implements PackageSubCategoryService {

	@Autowired
	private PackageSubCategoryRepository packageSubCategoryRepo;
	
	@Autowired
	private Logger logger;

	Calendar cal = Calendar.getInstance();
	Date date = cal.getTime();

	@Override
	public Response savepackagesubcategory(PackageSubCategory packageSubCatagory) {
		Response response=new Response();
		try {
			//System.out.println(packageSubCatagory.getPackagesubcategorycode());
			Long checkPkg = packageSubCategoryRepo.getSubCategoryIdBySubcategoryCode1(packageSubCatagory.getPackagesubcategorycode());
			if(checkPkg==0) {
			packageSubCatagory.setCreatedBy(packageSubCatagory.getCreatedBy());
			packageSubCatagory.setCreatedOn(date);
			packageSubCatagory.setDeletedFlag(0);
			packageSubCategoryRepo.save(packageSubCatagory);
			response.setMessage(" Package SubCategory Added");
			response.setStatus("Success");
			}else {
				response.setMessage("Package SubCategory Already Exist");
				response.setStatus("Failed");
			}
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public List<PackageSubCategory> getpackageSubCatagory() {   
		List<PackageSubCategory> headerResponse = packageSubCategoryRepo.findAll(Sort.by(Sort.Direction.ASC,"packagesubcategoryname"));
		return headerResponse;
	}

	
//	List<PackageHeader> headerResponse=new ArrayList<>();
//	List<PackageHeader> findAll = packageHeaderRepo.findAll();
//	
//	}
//	return headerResponse;
//}

	
	
	@Override
	public PackageSubCategory getbypackagesubcategory(Long subcategoryId) {
		PackageSubCategory packageSubCatagory=null;
		try {
			packageSubCatagory=packageSubCategoryRepo.findById(subcategoryId).get();
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return packageSubCatagory;
	}

	@Override
	public Response deletepackagesubcategory(Long subcategoryId) {
		Response response=new Response();
		try {
			PackageSubCategory packageSubCatagory=packageSubCategoryRepo.findById(subcategoryId).get();
			packageSubCatagory.setDeletedFlag(1);
			packageSubCategoryRepo.save(packageSubCatagory);
			response.setMessage("Record Successfully In-Active");
			response.setStatus("Success");
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	@Override
	public Response update(Long subcategoryId, PackageSubCategory packageSubCategory) {
		// TODO Auto-generated method stub
		Response response=new Response();
		PackageSubCategory packageSubCategoryResponse=null;
		try {
			Long checkPkg = packageSubCategoryRepo.getSubCategoryIdBySubcategoryCode1(packageSubCategory.getPackagesubcategorycode());
			if(checkPkg==0) {
				packageSubCategoryResponse=packageSubCategoryRepo.findById(subcategoryId).get();
				packageSubCategory.setSubcategoryId(subcategoryId);
				packageSubCategory.setUpdatedOn(date);
				packageSubCategory.setCreatedOn(packageSubCategoryResponse.getCreatedOn());
				packageSubCategory.setCreatedBy(packageSubCategoryResponse.getCreatedBy());
				packageSubCategory.setUpdatedBy(-1l);
				packageSubCategory.setDeletedFlag(packageSubCategoryResponse.getDeletedFlag());
				packageSubCategoryRepo.save(packageSubCategory);
					response.setMessage("Package SubCategory Updated");
					response.setStatus("Success");
			}else {
				packageSubCategoryResponse=packageSubCategoryRepo.findBypackagesubcategorycode(packageSubCategory.getPackagesubcategorycode());
				if(packageSubCategoryResponse.getSubcategoryId().equals(subcategoryId)) {
					packageSubCategory.setSubcategoryId(subcategoryId);
					packageSubCategory.setUpdatedOn(date);
					packageSubCategory.setCreatedOn(packageSubCategoryResponse.getCreatedOn());
					packageSubCategory.setCreatedBy(packageSubCategoryResponse.getCreatedBy());
					packageSubCategory.setUpdatedBy(-1l);
					packageSubCategory.setDeletedFlag(packageSubCategoryResponse.getDeletedFlag());
					packageSubCategoryRepo.save(packageSubCategory);
						response.setMessage("Package SubCategory Updated");
						response.setStatus("Success");
				}else {
					response.setMessage("Package SubCategory Already Exist");
					response.setStatus("Failed");
				}
			}
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
			
		}
		
		return response;
	}

	@Override
	public Long checkDuplicateSubCategoryName(String packagesubcategoryname) {  
		Long checkPkg=null;
		try {
			checkPkg = packageSubCategoryRepo.getSubCategoryIdBySubcategoryName(packagesubcategoryname);
		//////System.out.println(checkGrp);
		return checkPkg;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkPkg;
	}

	@Override
	public Long checkDuplicateSubCategoryCode(String packagesubcategorycode) {
		Long checkPkg=null;
		try {
			checkPkg = packageSubCategoryRepo.getSubCategoryIdBySubcategoryCode(packagesubcategorycode);
		//////System.out.println(checkGrp);
		return checkPkg;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return checkPkg;
	}

	@Override
	public Response activepackagesubcatagory(Long subcategory, Long userid) {
		Response response = new Response();
		try {
			PackageSubCategory packagesub = packageSubCategoryRepo.findById(subcategory).get();
			packagesub.setDeletedFlag(0);
			packagesub.setUpdatedBy(userid);
			packagesub.setUpdatedOn(Calendar.getInstance().getTime());
			packageSubCategoryRepo.save(packagesub);
			response.setMessage("Record Successfully Active");
			response.setStatus("Success");
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Some error happen");
			response.setStatus("Failed");
		}
		return response;
	}

	
}

