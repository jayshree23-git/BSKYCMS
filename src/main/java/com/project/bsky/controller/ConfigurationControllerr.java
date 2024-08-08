/**
 * 
 */
package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bsky.bean.Cpdconfigbean;
import com.project.bsky.bean.Cpdspecialitydocbean;
import com.project.bsky.bean.Cpdspecialitymappingbean;
import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.Response;
import com.project.bsky.service.ConfigurationService;

/**
 * @author rajendra.sahoo
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class ConfigurationControllerr {
	
	@Autowired
	private ConfigurationService configservice;
	
	@Autowired
	private Logger logger;

	@GetMapping(value = "/gettaggedhospitalofcpd")
	@ResponseBody
	public List<Cpdconfigbean> gettaggedhospitalofcpd(@RequestParam(value = "userid", required = false) Long userid){
		return configservice.gettaggedhospitalofcpd(userid);
	}
	
	@GetMapping(value = "/getcpdtagginglog")
	@ResponseBody
	public List<HospitalBean> getcpdtagginglog(@RequestParam(value = "userid", required = false) Long userid){
		return configservice.getcpdtagginglog(userid);
	}
	
	@GetMapping(value = "/getcpdtaggedhospital")
	@ResponseBody
	public List<HospitalBean> getcpdtaggedhospital(@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "type", required = false) Integer type){
		return configservice.getcpdtaggedhospital(userid,type);
	}
	
	@GetMapping(value = "/applyforexclusion")
	@ResponseBody
	public Response applyforexclusion(@RequestParam(value = "hospitalcode", required = false) String hospitalcode,
			@RequestParam(value = "bskyuserid", required = false) Integer bskyuserid){
		Response response=new Response();
		try {
			response=configservice.applyforexclusion(hospitalcode,bskyuserid);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("400");
		}
		return response;
	}
	
	@GetMapping(value = "/approveofexclusion")
	@ResponseBody
	public Response approveofexclusion(@RequestParam(value = "hospitalcode", required = false) String hospitalcode,
			@RequestParam(value = "bskyuserid", required = false) Integer bskyuserid,
			@RequestParam(value = "userid", required = false) Integer userid,
			@RequestParam(value = "ipaddress", required = false) String ipaddress){
		Response response=new Response();
		try {
			response=configservice.approveofexclusion(hospitalcode,bskyuserid,userid,ipaddress);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("400");
		}
		return response;
	}
	
	@GetMapping(value = "/approveofinclusion")
	@ResponseBody
	public Response approveofinclusion(@RequestParam(value = "hospitalcode", required = false) String hospitalcode,
			@RequestParam(value = "bskyuserid", required = false) Integer bskyuserid,
			@RequestParam(value = "userid", required = false) Integer userid,
			@RequestParam(value = "ipaddress", required = false) String ipaddress){
		Response response=new Response();
		try {
			response=configservice.approveofinclusion(hospitalcode,bskyuserid,userid,ipaddress);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("400");
		}
		return response;
	}
	
	@GetMapping(value = "/getappliedlistadmin")
	@ResponseBody
	public List<Cpdconfigbean> getappliedlistadmin(){
		List<Cpdconfigbean> list=new ArrayList<Cpdconfigbean>();
		try {
			list=configservice.getappliedexclusionlistadmin();
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	@GetMapping(value = "/getappliedlistSNA")
	@ResponseBody
	public List<Cpdconfigbean> getappliedlistSNA(@RequestParam(value = "userid", required = false) Long userid){
		List<Cpdconfigbean> list=new ArrayList<Cpdconfigbean>();
		try {
			list=configservice.getappliedexclusionlistSNA(userid);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	@GetMapping(value = "/getappliedinclusionlistadmin")
	@ResponseBody
	public List<Cpdconfigbean> getappliedinclusionlistadmin(){
		List<Cpdconfigbean> list=new ArrayList<Cpdconfigbean>();
		try {
			list=configservice.getappliedinclusionlistadmin();
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	@GetMapping(value = "/getappliedinclusionlistSNA")
	@ResponseBody
	public List<Cpdconfigbean> getappliedinclusionlistSNA(@RequestParam(value = "userid", required = false) Long userid){
		List<Cpdconfigbean> list=new ArrayList<Cpdconfigbean>();
		try {
			list=configservice.getappliedinclusionlistSNA(userid);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	@GetMapping(value = "/gethospitalforinclusion")
	@ResponseBody
	public List<HospitalBean> gethospitalforinclusion(@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "dist", required = false) String dist){
		List<HospitalBean> list=new ArrayList<HospitalBean>();
		try {
			list=configservice.gethospitalforinclusion(userid,state,dist);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	@GetMapping(value = "/applyhospitalforinclusion")
	@ResponseBody
	public Response applyhospitalforinclusion(@RequestParam(value = "userid", required = false) Integer userid,
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode){
		Response response=new Response();
		try {
			response=configservice.applyhospitalforinclusion(hospitalcode,userid);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("400");
		}
		return response;
	}	
	
	@GetMapping(value = "/cancelrequestforapply")
	@ResponseBody
	public Response cancelrequest(@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "hospitalcode", required = false) String hospitalcode){
		Response response=new Response();
		try {
			response=configservice.cancelrequest(hospitalcode,userid);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			response.setMessage("Something Went Wrong");
			response.setStatus("400");
		}
		return response;
	}	
	
	@GetMapping(value = "/cpdhospitaltaglist")
	@ResponseBody
	public Map<String,Object> cpdhospitaltaglist(@RequestParam(value = "cpdId", required = false) Long cpdId,
			@RequestParam(value = "cpduserid", required = false) Long cpduserid,
			@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "status", required = false) Integer status){
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",configservice.cpdhospitaltaglist(cpdId,cpduserid,status,userid));
			map.put("status", 200);
			map.put("message", "Api Called Successfully");
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			map.put("error", e.getMessage());
			map.put("status", 400);
			map.put("message", "Something Went Wrong");
		}
		return map;
	}	
	
	@GetMapping(value = "/getcpdmappedPackageList")
	@ResponseBody
	public Map<String,Object> getcpdmappedPackageList(@RequestParam(value = "cpdId", required = false) Long cpdId,
			@RequestParam(value = "cpduserid", required = false) Long cpduserid){
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",configservice.getcpdmappedPackageList(cpdId,cpduserid));
			map.put("status", 200);
			map.put("message", "Api Called Successfully");
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			map.put("error", e.getMessage());
			map.put("status", 400);
			map.put("message", "Something Went Wrong");
		}
		return map;
	}	
	
	@PostMapping(value = "/saveCPDSpecialityMapping")
	@ResponseBody
	public Map<String,Object> saveCPDSpecialityMapping(@RequestParam(value = "files", required = false) MultipartFile[] files,
            @RequestParam("cpdData") String cpdDataJson){
		Map<String,Object> map=new HashMap<>();
		JSONObject json=null;
		try {			
			json=new JSONObject(cpdDataJson);
			Cpdspecialitymappingbean bean=new Cpdspecialitymappingbean();
				bean.setCpdId(json.getLong("cpdId"));
				bean.setUserId(json.getLong("userId"));
			JSONArray jsonarr=new JSONArray(json.getString("speciality"));
			List<Cpdspecialitydocbean> beanlist=new ArrayList<>();
			int j=0;
			for (int i = 0; i < jsonarr.length(); i++) {
				json = jsonarr.getJSONObject(i);
				Cpdspecialitydocbean docbean=new Cpdspecialitydocbean();
					docbean.setPackageid(json.getLong("packageid"));
					docbean.setPackagename(json.getString("packagename"));
					docbean.setStatus(json.getInt("status"));
					if(json.getInt("doc")==1) {
						docbean.setFile(files[j]);
						j++;
					}
				beanlist.add(docbean);
			}
			bean.setSpeciality(beanlist);
			map=configservice.saveCPDSpecialityMapping(bean);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			map.put("error", e.getMessage());
			map.put("status", 400);
			map.put("message", "Something Went Wrong");
		}
		return map;
	}
	
	@GetMapping(value = "/downloadcpdspecdoc")
	@ResponseBody
	public String downloadcpdspecdoc(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		String resp = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		String fileName = json.getString("f");
		String userid = json.getString("c");
		try {
			if (fileName == null || fileName == "" || fileName.equalsIgnoreCase("")) {
				resp = "File not found";
			} else {
				configservice.downloadcpdspecdoc(fileName,userid,response);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}
	
	@GetMapping(value = "/getcpdtaggedPackageList")
	@ResponseBody
	public Map<String,Object> getcpdtaggedPackageList(@RequestParam(value = "cpduserid", required = false) Long cpduserid){
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",configservice.getcpdtaggedPackageList(cpduserid));
			map.put("status", 200);
			map.put("message", "Api Called Successfully");
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			map.put("error", e.getMessage());
			map.put("status", 400);
			map.put("message", "Something Went Wrong");
		}
		return map;
	}	
	
	@GetMapping(value = "/getspecilitywisecpdcount")
	@ResponseBody
	public Map<String,Object> getspecilitywisecpdcount(@RequestParam(value = "speclty", required = false) String speclty,
			@RequestParam(value = "userId", required = false) Long userId){
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",configservice.getspecilitywisecpdcount(userId,speclty));
			map.put("status", 200);
			map.put("message", "Api Called Successfully");
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			map.put("error", e.getMessage());
			map.put("status", 400);
			map.put("message", "Something Went Wrong");
		}
		return map;
	}
	
	@GetMapping(value = "/getspecilitywisecpdlist")
	@ResponseBody
	public Map<String,Object> getspecilitywisecpdlist(@RequestParam(value = "packageid", required = false) String packageid,
			@RequestParam(value = "userId", required = false) Long userId){
		Map<String,Object> map=new HashMap<>();
		try {
			map.put("data",configservice.getspecilitywisecpdlist(userId,packageid));
			map.put("status", 200);
			map.put("message", "Api Called Successfully");
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			map.put("error", e.getMessage());
			map.put("status", 400);
			map.put("message", "Something Went Wrong");
		}
		return map;
	}
}
