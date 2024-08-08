package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import com.project.bsky.bean.PackageSubCatagoryDto;
import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageSubCategory;
import com.project.bsky.service.PackageSubCategoryService;



@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")
public class PackageSubCategoryController {
	@Autowired
	private PackageSubCategoryService packageSubCategoryService;
	
	@Autowired
	private Logger logger;
	
	@PostMapping(value = "/savepackagesubcategory")
	@ResponseBody
	public Response savepackagesubcategory(@RequestBody PackageSubCategory packageSubCatagory){
		return packageSubCategoryService.savepackagesubcategory(packageSubCatagory);
	}
	 
	@GetMapping(value = "/getpackagesubcategory")
	@ResponseBody
	public List<PackageSubCategory> getpackageSubCatagory(){
		return packageSubCategoryService.getpackageSubCatagory();
	}
	
	@DeleteMapping(value = "/deletepackagesubcategory/{subcategoryId}")
	@ResponseBody
	public Response deletepackagesubcategory(@PathVariable(value = "subcategoryId", required = false) Long subcategoryId){
	return packageSubCategoryService.deletepackagesubcategory(subcategoryId);
	}
	
	@GetMapping(value = "/activepackagesubcategory")
	@ResponseBody
	public Response activepackageheader(@RequestParam(value = "subcategory", required = false) Long subcategory,
			@RequestParam(value = "userid", required = false) Long userid) {
		return packageSubCategoryService.activepackagesubcatagory(subcategory,userid);
	}

	@GetMapping(value = "/getpackagesubcategory/{subcategoryId}")
	@ResponseBody
	public PackageSubCategory getbypackagesubcategory(@PathVariable(value = "subcategoryId", required = false) Long subcategoryId){
		return packageSubCategoryService.getbypackagesubcategory(subcategoryId);
	}
	
	@PutMapping(value = "/updatepackagesubcategory/{subcategoryId}")
	@ResponseBody
	public Response updatepackagesubcategory(@RequestBody PackageSubCategory packageSubCategory,
			@PathVariable(value = "subcategoryId", required = false) Long subcategoryId) {
		////System.out.println("==========" + packageSubCategory);
		return packageSubCategoryService.update(subcategoryId, packageSubCategory);
	}
	
	@ResponseBody
	@GetMapping(value = "/checkDuplicatesubcategoryname")
	public ResponseEntity<Response> checkDuplicateSubCategoryName(@RequestParam(value = "packagesubcategoryname", required =false)String packagesubcategoryname,
			Response response){
		////System.out.println("Inside------>>");
		////System.out.println("PackageSubCategoryName : " + packagesubcategoryname);
		try {
			Long subcategoryIdLong = packageSubCategoryService.checkDuplicateSubCategoryName(packagesubcategoryname);
			
			if (subcategoryIdLong != null) 
				response.setStatus("Present");
			else 
				response.setStatus("Absent");
			
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		
		return ResponseEntity.ok(response);
	}
	@ResponseBody
	@GetMapping(value = "/checkDuplicatesubcategorycode")
	public ResponseEntity<Response> checkDuplicateSubCategoryCode(@RequestParam(value = "packagesubcategorycode", required =false)String packagesubcategorycode,
			Response response){
		////System.out.println("Inside------>>");
		////System.out.println("PackageSubCategoryCode : " + packagesubcategorycode);
		try {
			Long subcategoryIdLong = packageSubCategoryService.checkDuplicateSubCategoryCode(packagesubcategorycode);
			
			if (subcategoryIdLong != null) 
				response.setStatus("Present");
			else 
				response.setStatus("Absent");
			
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		
		return ResponseEntity.ok(response);
	}
	
}

