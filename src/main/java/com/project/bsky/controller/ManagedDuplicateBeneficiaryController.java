package com.project.bsky.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.CheackCardBalancebean;
import com.project.bsky.bean.ManageDuplicateBeneficiaryBean;
import com.project.bsky.bean.OnlinePostConfigBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.RationCardUser;
import com.project.bsky.service.CheckCardBalanceService;
import com.project.bsky.service.ManagedDuplicateBeneficiaryService;

@RestController
@RequestMapping(value = "/api")
public class ManagedDuplicateBeneficiaryController {
	@Autowired
	private ManagedDuplicateBeneficiaryService managedduplicatebenificiary;

	@Autowired
	private Logger logger;
	
//	VIEWLIST BY URN(ACTIONCODE-3)
	@GetMapping(value = "/manageduplicatebeneficiarylist")
	@ResponseBody
	public List<Object> manageduplicatebeneficiarylist(@RequestParam(value = "searchtype", required = false) Long searchtype,
			@RequestParam(value = "searchvalue", required = false) String searchvalue) {
		List<Object> list=new ArrayList<Object>();
		try {
			list=managedduplicatebenificiary.manageduplicatebeneficiarylist(searchtype, searchvalue);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	//BENEFICIARYLIST BY URN AND UID(ACTIONCODE-1-URN,2-UID  )	
	@GetMapping(value = "/beneficiarylist")
	@ResponseBody
	public List<Object> beneficiarylist(@RequestParam(value = "searchtype", required = false) Long searchtype,
			@RequestParam(value = "searchvalue", required = false) String searchvalue) {
		List<Object> list=new ArrayList<Object>();
		try {
			list=managedduplicatebenificiary.beneficiarylist(searchtype, searchvalue);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	@GetMapping(value = "/ongoingtreatmentlist")
	@ResponseBody
	public List<Object> ongoingtreatmentlist(@RequestParam(value = "searchtype", required = false) Long searchtype,
			@RequestParam(value = "searchvalue", required = false) String searchvalue) {
		List<Object> list=new ArrayList<Object>();
		try {
			list=managedduplicatebenificiary.ongoingtreatmentlist(searchtype, searchvalue);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	@PostMapping(value = "/inactivebeneficiary")
	@ResponseBody
	public Response savepostname(@RequestBody ManageDuplicateBeneficiaryBean manageduplicatebeneficiarybean){
		Response map=new Response();
		try {
//			System.out.println(manageduplicatebeneficiarybean);
			map=managedduplicatebenificiary.inactivebeneficiary(manageduplicatebeneficiarybean);
		} catch (Exception e) {
			e.printStackTrace();
			map.setStatus("400");
			map.setMessage(e.getMessage());
		}
		return map;
	
	}
	
	@GetMapping(value = "/manageduplicatebeneficiaryviewlist")
	@ResponseBody
	public Map<String,Object> manageduplicatebeneficiaryviewlist(@RequestParam(value = "searchtype", required = false) Long searchtype,
			@RequestParam(value = "searchvalue", required = false) String searchvalue) {
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("status", 200);
			map.put("data",managedduplicatebenificiary.manageduplicatebeneficiaryviewlist(searchtype, searchvalue));
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", 400);
			map.put("error",e.getMessage());
		}
		return map;
	}
}
