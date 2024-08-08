package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.model.PackageHeader;
import com.project.bsky.model.TxnPackageDetails;
import com.project.bsky.service.TxnPackageDetailsService;

@CrossOrigin(originPatterns = "*")
@RestController
@RequestMapping(value = "/api")
public class TxnPackageDetailsController {
	@Autowired
	private TxnPackageDetailsService txnPackageDetailsService;
	

//	@GetMapping(value = "/gettxnpackagedetails")
//	@ResponseBody
//	public List<TxnPackageDetails> gettxnPackageDetails(){
//		return txnPackageDetailsService.gettxnPackageDetails();
//	}
}
