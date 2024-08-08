package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.PackageDetailsHospital;
import com.project.bsky.model.WardCategoryMaster;
import com.project.bsky.service.WardCategoryMasterService;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")

public class WardMasterCategoryController {

	@Autowired
	private WardCategoryMasterService wardCategoryMasterService;
	
	@GetMapping(value = "/getallwardcategoryMaster")
	@ResponseBody
	public List<WardCategoryMaster> getwardCategoryMaster(){
		return wardCategoryMasterService.getwardCategory();
	}
//	 @GetMapping(value = "/getPackageDescription")
//	    @ResponseBody
//	    public List<PackageDetailsHospital> getPackageDetailsDescription(@RequestParam String packageheadercode) {
//
//	        return wardCategoryMasterService.getpackageDetailsDescrition(packageheadercode);
//	    }

}
