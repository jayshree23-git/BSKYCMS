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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.Response;
import com.project.bsky.model.FoRemark;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.service.FoRemarkservice;
import com.project.bsky.service.Globallinkservice;

/**
 * @author rajendra.sahoo
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class FoRemarkController {
	
	@Autowired
	private FoRemarkservice FoRemarkservice;

	@PostMapping(value = "/saveforemark")
	@ResponseBody
	public Response saveforemark(@RequestBody FoRemark foremark){
		////System.out.println(FoRemark);
		return FoRemarkservice.saveforemark(foremark);
	}
	
	@GetMapping(value = "/getforemark")
	@ResponseBody
	public List<FoRemark> getforemark(){
		return FoRemarkservice.getforemark();
	}
	
	@GetMapping(value = "/getactiveforemark")
	@ResponseBody
	public List<FoRemark> getactiveforemark(){
		return FoRemarkservice.getactiveforemark();
	}
	
	@PostMapping(value = "/updateforemark")
	@ResponseBody
	public Response updategloballink(@RequestBody FoRemark foremark){
		//System.out.println(foremark);
		return FoRemarkservice.updateforemark(foremark);
	}
}
