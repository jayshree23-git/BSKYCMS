package com.project.bsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.bsky.model.Group;
import com.project.bsky.model.Subgroup;
import com.project.bsky.service.Subgroupservice;



/**
 * @author rajendra.sahoo
 *
 */
@Controller
@RequestMapping(value = "/api")
public class SubGroupController {
	@Autowired
	private Subgroupservice subgroupservice;
	
	
	
	@ResponseBody
	@GetMapping(value = "/getGroupname")
	public List<Group> getGroupname() {
		////System.out.println("hiii");
		return subgroupservice.getgroupname();
	}
	
	
	@ResponseBody
	@GetMapping(value = "/savesubgroup")
	public Integer savesubgroup(@RequestParam(required = false, value = "groupid") long groupid,
			@RequestParam(required = false, value = "subgroupname") String subgroupname,
			@RequestParam(required = false, value = "createdby") String createdby) {
		Integer response=subgroupservice.savesubgroup(groupid,subgroupname,createdby);
		////System.out.println(response);
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getalldata")
	public List<Subgroup> getalldata() {
		return subgroupservice.getalldata();
	}
	
	@ResponseBody
	@GetMapping(value = "/Delete")
	public Integer delete(@RequestParam(required = false, value = "subgroupid") long subgroupid) {
		////System.out.println(subgroupid);
		return subgroupservice.delete(subgroupid);
	}
	
	@ResponseBody
	@GetMapping(value = "/getbyid")
	public Subgroup getbyid(@RequestParam(required = false, value = "subgroupid") long subgroupid) {
		return subgroupservice.getbyid(subgroupid);

	}
	
	@ResponseBody
	@GetMapping(value = "/updatesubgroup")
	public Integer updatesubgroup(@RequestParam(required = false, value = "groupid") long groupid,
			@RequestParam(required = false, value = "subgroupname") String subgroup,
			@RequestParam(required = false, value = "updateby") String updateby,
			@RequestParam(required = false, value = "subgroupid") long subgroupid,
			@RequestParam(required = false, value = "status") Integer status) {
		////System.out.println(status);
		Integer i=subgroupservice.update(groupid,subgroup,updateby,subgroupid,status);
		return i;
	}

}
