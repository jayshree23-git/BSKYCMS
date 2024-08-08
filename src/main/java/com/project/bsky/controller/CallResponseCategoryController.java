package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.CallResponsecategory;
import com.project.bsky.service.CallResponseCategoryService;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")
public class CallResponseCategoryController {
	
	@Autowired
	private CallResponseCategoryService callResponseCategoryService;
	
	
	@GetMapping(value = "/getallcallResponseCategory")
	@ResponseBody
	public List<CallResponsecategory> getallCallResponseCategory(@RequestParam Integer statusId){
		return callResponseCategoryService.getallCallResponseCategory(statusId);
	}
}
