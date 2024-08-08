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
import com.project.bsky.model.UnlinkedFunctionMaster;
import com.project.bsky.service.FunctionmasterService;

/**
 * @author rajendra.sahoo
 *
 */

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class Functionmastercontroller {
	
	@Autowired
	private FunctionmasterService functionmasterservice;
	
	@PostMapping(value = "/savefunctionmaster")
	@ResponseBody
	public Response savefunctionmaster(@RequestBody FunctionMaster functionmaster){
		return functionmasterservice.savefunctionmaster(functionmaster);
	}
	
	@PostMapping(value = "/saveunlinkedfunctionmaster")
	@ResponseBody
	public Response saveunlinkedfunctionmaster(@RequestBody UnlinkedFunctionMaster unlinkedFunctionMaster){
		return functionmasterservice.saveunlinkedfunctionmaster(unlinkedFunctionMaster);
	}
	
	@GetMapping(value = "/getfunctionmaster")
	@ResponseBody
	public List<FunctionMaster> getfunctionmaster(){
		return functionmasterservice.getfunctionmaster();
	}
	
	@GetMapping(value = "/getunlinkedfunctionmaster")
	@ResponseBody
	public List<UnlinkedFunctionMaster> getUnlinkedFunctionMaster(){
		return functionmasterservice.getUnlinkedFunctionMaster();
	}
	
	@GetMapping(value = "/deletefunctionmaster")
	@ResponseBody
	public Response deletefunctionmaster(@RequestParam(value = "userId", required = false) Long userid,
			@RequestParam(value = "fnid", required = false) Long fnid){
		return functionmasterservice.deletefunctionmaster(userid,fnid);
	}
	
	@GetMapping(value = "/removeunlinkedfunctionmaster")
	@ResponseBody
	public Response removeUnlinkedFunctionMaster(@RequestParam(value = "userId", required = false) Long userid,
			@RequestParam(value = "fnid", required = false) Long fnid){
		return functionmasterservice.removeUnlinkedFunctionMaster(userid, fnid);
	}
	
	@GetMapping(value = "/getbyfunctionmaster")
	@ResponseBody
	public FunctionMaster getbyfunctionmaster(@RequestParam(value = "fnid", required = false) Long userid){
		return functionmasterservice.getbyfunctionmaster(userid);
	}
	
	@GetMapping(value = "/getunlinkedfunctionbyid")
	@ResponseBody
	public UnlinkedFunctionMaster getUnlinkedFunctionById(@RequestParam(value = "functionId", required = false) Long functionId){
		return functionmasterservice.getUnlinkedFunctionById(functionId);
	}
	
	@PostMapping(value = "/updatefunctionmaster")
	@ResponseBody
	public Response updatefunctionmaster(@RequestBody FunctionMaster functionmaster){
		return functionmasterservice.update(functionmaster);
	}
	
	@PostMapping(value = "/updateunlinkedfunctionmaster")
	@ResponseBody
	public Response updateUnlinkedFunctionMaster(@RequestBody UnlinkedFunctionMaster unlinkedFunctionMaster){
		return functionmasterservice.updateUnlinkedFunctionMaster(unlinkedFunctionMaster);
	}

}
