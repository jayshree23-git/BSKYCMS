/**
 * 
 */
package com.project.bsky.controller;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.PackageMaster;
import com.project.bsky.repository.InclusionofsearchingRepository;
import com.project.bsky.service.ClaimraiseDetailsService;

/**
 * @author hrusikesh.mohanty
 *
 */
@RestController
@RequestMapping(value = "/api")
public class Inclusionofsearching {
	
	@Autowired
	private InclusionofsearchingRepository inclusionrepo;
	
	@Autowired
	private ClaimraiseDetailsService claimRaiseService;
	
	@Autowired
	private Logger logger;
	

	@ResponseBody
	@GetMapping(value = "/Inclusionofsearchingforpackagedetails")
	public List<PackageMaster> getdetailsofpackage() {
		List<PackageMaster> seraching = inclusionrepo.findAll();
		return seraching;
		
	}
	
	
	
	@ResponseBody
	@GetMapping(value = "/datasearch/{packageName}")
	public List<Object> getallDatapackagename(@PathVariable("packageName") String packageName){
		List<Object> packagedata = null;
		try {
			packagedata=claimRaiseService.getpackdata(packageName);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return packagedata;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@ResponseBody
//	@GetMapping(value = "/search")
//	public List<PackageMaster> getdetailsofpackagedetailsbsky() {
//		List<PackageMaster> serachingpack =inclusionrepo.findAll();
//		////System.out.println(serachingpack);
//		return serachingpack;
//		
//	}
	
	
	
	
	
	

}
