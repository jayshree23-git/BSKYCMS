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
import com.project.bsky.model.FunctionMaster;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.service.FunctionmasterService;
import com.project.bsky.service.Globallinkservice;

/**
 * @author rajendra.sahoo
 *
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class Globallinkcontroller {
	
	@Autowired
	private Globallinkservice globallinkservice;
	
	@PostMapping(value = "/savegloballink")
	@ResponseBody
	public Response savegloballink(@RequestBody GlobalLink globallink){
		////System.out.println(globallink);
		return globallinkservice.savegloballink(globallink);
	}
	
	@PostMapping(value = "/updategloballink")
	@ResponseBody
	public Response updategloballink(@RequestBody GlobalLink globallink){
		return globallinkservice.updategloballink(globallink);
	}
	
	@GetMapping(value = "/getgloballink")
	@ResponseBody
	public List<GlobalLink> getgloballink(){
		return globallinkservice.getgloballink();
	}
	
	@GetMapping(value = "/deletegloballink")
	@ResponseBody
	public Response deletegloballink(@RequestParam(value = "userId", required = false) Long userid){
		return globallinkservice.deletefunctionmaster(userid);
	}
	
	@GetMapping(value = "/getgloballinkbyid")
	@ResponseBody
	public GlobalLink getgloballinkbyid(@RequestParam(value = "userid", required = false) Long userid){
		return globallinkservice.getbyId(userid);
	}
}
