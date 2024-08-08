/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UnBundlingPackage;
import com.project.bsky.service.UnBundlingPackageService;

/**
 * @author rajendra.sahoo
 *	DT- 06-11-2023
 */

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")
public class UnBundlingPackageController {
	
	@Autowired
	private UnBundlingPackageService unBundlingpackageservice;
	
	@PostMapping(value = "/submitunBundlingPackage")
	@ResponseBody
	public Response SubmitdunamicConfiguration(@RequestBody UnBundlingPackage unBundlingPackage){
		return unBundlingpackageservice.SubmitdunamicConfiguration(unBundlingPackage);
	}
	
	@GetMapping(value = "/getunBundlingPackagelist")
	@ResponseBody
	public List<UnBundlingPackage> getdynamicconfigurationlist(){
		return unBundlingpackageservice.getdynamicconfigurationlist();
	}
	
	@GetMapping(value = "/getunBundlingPackagebyid")
	@ResponseBody
	public UnBundlingPackage getunBundlingPackagebyid(@RequestParam(value = "slno", required = false) Long slno){
		return unBundlingpackageservice.getunBundlingPackagebyid(slno);
	}
	
	@PostMapping(value = "/updateunBundlingPackage")
	@ResponseBody
	public Response updatedunamicConfiguration(@RequestBody UnBundlingPackage unBundlingPackage){
		return unBundlingpackageservice.updatedunamicConfiguration(unBundlingPackage);
	}
}
