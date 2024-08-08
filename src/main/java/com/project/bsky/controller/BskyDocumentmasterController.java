/**
 * 
 */
package com.project.bsky.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.DocProcedureMstdocbean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.BskyDocumentmaster;
import com.project.bsky.model.BskyMessageMaster;
import com.project.bsky.service.BskyDocumentmasterService;

/**
 * 
 */
@RestController
@RequestMapping(value = "/api")
public class BskyDocumentmasterController {
	
	@Autowired
	private BskyDocumentmasterService bskydocumentserv;
	
	@ResponseBody
	@PostMapping(value = "/savedocumentmst")
	public Response savedocumentmst(@RequestBody BskyDocumentmaster bsydocumentmaster) {
		Response response = new Response();
		try {
			response = bskydocumentserv.savedocumentmst(bsydocumentmaster);
		} catch (Exception e) {
			response.setMessage("Something Went Wrong! Try Later");
			response.setStatus("400");
			e.printStackTrace();
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getdocumentmst")
	public Map<String,Object> getdocumentmst() {
		Map<String,Object> response = new HashMap<>();
		try {
			response.put("data", bskydocumentserv.getdocumentmst());
			response.put("status", HttpStatus.OK.value());
			response.put("message", "Api Called Successfully");
		} catch (Exception e) {
			response.put("status", HttpStatus.BAD_REQUEST.value());
			response.put("message", "Something Went Wrong");
			response.put("error",e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	@ResponseBody
	@PostMapping(value = "/updatedocumentmst")
	public Response updatedocumentmst(@RequestBody BskyDocumentmaster bsydocumentmaster) {
		Response response = new Response();
		try {
			response = bskydocumentserv.updatedocumentmst(bsydocumentmaster);
		} catch (Exception e) {
			response.setMessage("Something Went Wrong! Try Later");
			response.setStatus("400");
			e.printStackTrace();
		}
		return response;
	}
	@ResponseBody
	@GetMapping(value = "/getdocumentmstname")
	public Map<String,Object> getdocumentmstname(@RequestParam(value = "procedureCode", required = false) String packagecode) {
		Map<String,Object> response = new HashMap<>();
		try {
			response.put("data", bskydocumentserv.getdocumentmstname(packagecode));
			response.put("status", HttpStatus.OK.value());
			response.put("message", "Api Called Successfully");
		} catch (Exception e) {
			response.put("status", HttpStatus.BAD_REQUEST.value());
			response.put("message", "Something Went Wrong");
			response.put("error",e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getprocdurethroughheadercode")
	public Map<String,Object> getprocdurethroughheadercode(@RequestParam(value = "headerCode", required = false) String headerCode) {
		Map<String,Object> response = new HashMap<>();
		try {
			response.put("data", bskydocumentserv.getprocdurethroughheadercode(headerCode));
			response.put("status", HttpStatus.OK.value());
			response.put("message", "Api Called Successfully");
		} catch (Exception e) {
			response.put("status", HttpStatus.BAD_REQUEST.value());
			response.put("message", "Something Went Wrong");
			response.put("error",e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	@ResponseBody
	@PostMapping(value = "/savedocproceduremapping")
	public Response savedocproceduremapping(@RequestBody DocProcedureMstdocbean bsydocumentmapping) {
		Response response = new Response();
		try {
			response = bskydocumentserv.savedocproceduremapping(bsydocumentmapping);
		} catch (Exception e) {
			response.setMessage("Something Went Wrong! Try Later");
			response.setStatus("400");
			e.printStackTrace();
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getdocproctaggedlist")
	public Map<String,Object> getdocproctaggedlist(@RequestParam(value = "headerCode", required = false) String headerCode) {
		Map<String,Object> response = new HashMap<>();
		try {
			response.put("data", bskydocumentserv.getdocproctaggedlist(headerCode));
			response.put("status", HttpStatus.OK.value());
			response.put("message", "Api Called Successfully");
		} catch (Exception e) {
			response.put("status", HttpStatus.BAD_REQUEST.value());
			response.put("message", "Something Went Wrong");
			response.put("error",e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	@ResponseBody
	@GetMapping(value = "/getdocproctaggedlistforexcel")
	public Map<String,Object> getdocproctaggedlistforexcel(@RequestParam(value = "headerCode", required = false) String headerCode) {
		Map<String,Object> response = new HashMap<>();
		try {
			response.put("data", bskydocumentserv.getdocproctaggedlistforexcel(headerCode));
			response.put("status", HttpStatus.OK.value());
			response.put("message", "Api Called Successfully");
		} catch (Exception e) {
			response.put("status", HttpStatus.BAD_REQUEST.value());
			response.put("message", "Something Went Wrong");
			response.put("error",e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	@GetMapping(value = "/getproceduretagggeddoclist")
	public Map<String,Object> getproceduretagggeddoclist(@RequestParam(value = "procedureCode", required = false) String procedureCode) {
		Map<String,Object> response = new HashMap<>();
		try {
			response.put("data", bskydocumentserv.getproceduretagggeddoclist(procedureCode));
			response.put("status", HttpStatus.OK.value());
			response.put("message", "Api Called Successfully");
		} catch (Exception e) {
			response.put("status", HttpStatus.BAD_REQUEST.value());
			response.put("message", "Something Went Wrong");
			response.put("error",e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
}
