/**
 * 
 */
package com.project.bsky.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.InteenalCommunicationBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.InternalCommunication_user;
import com.project.bsky.service.InternalCommunicationSercice;

/**
 * @author rajendra.sahoo
 *
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class InternalCommunicationController {
	
	@Autowired
	private InternalCommunicationSercice internalcommservice;
	
	@Autowired
	private Logger logger;
	
	@ResponseBody
	@GetMapping(value = "/getintcommuserlist")
	public List<InternalCommunication_user> getintcommuserlist(){
		List<InternalCommunication_user> list=new ArrayList<InternalCommunication_user>();
		try {
			list=internalcommservice.getintcommuserlist();
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}			
			
			@ResponseBody
			@GetMapping(value = "/getintcommlist")
			public List<InteenalCommunicationBean> getintcommlist(
					@RequestParam(required = false, value = "userid") Long userid){
				List<InteenalCommunicationBean> list=new ArrayList<InteenalCommunicationBean>();
				try {
					list=internalcommservice.getintcommlist(userid);
				}catch (Exception e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
				return list;
			}	
			
			@GetMapping(value = "/getintcommtasklist")
			public List<InteenalCommunicationBean> getintcommtasklist(
					@RequestParam(required = false, value = "userid") Long userid){
				List<InteenalCommunicationBean> list=new ArrayList<InteenalCommunicationBean>();
				try {
					list=internalcommservice.getintcommtasklist(userid);
				}catch (Exception e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
				return list;
			}	
	
	@ResponseBody
	@PostMapping(value = "/saveintcomm")
	public Response saveintcomm(InteenalCommunicationBean bean,
			@RequestParam(required = false, value = "file") MultipartFile form){
		Response list=new Response();
		try {
			////System.out.println(form);
			if(form!=null) {
				bean.setFile2(form);
			}
			list=internalcommservice.saveintcomm(bean);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	@ResponseBody
	@PostMapping(value = "/updateintcomm")
	public Response updateintcomm(@RequestParam(required = false, value = "file") MultipartFile form,
			@RequestParam(required = false, value = "remarks") String remarks,
			@RequestParam(required = false, value = "userid") Long userid,
			@RequestParam(required = false, value = "intcommid") Long commid){
		Response list=new Response();
		try {			
			list=internalcommservice.updateintcomm(form,remarks,userid,commid);
		}catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return list;
	}
	
	@ResponseBody
	@GetMapping(value = "/downLoadintcommnDoc")
	public String commonDownloadMethod(HttpServletResponse response, @RequestParam("data") String enCodedJsonString)
			throws JSONException {
		////System.out.println(enCodedJsonString);
		String resp = "";
		byte[] bytes = Base64.getDecoder().decode(enCodedJsonString);
		String jsonString = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonString);
		////System.out.println("json: " + json);
		String fileName = json.getString("f");
		////System.out.println("name: " + fileName);
		try {
			if (fileName == null || fileName == "" || fileName.equalsIgnoreCase("")) {
				////System.out.println("File not found");
				resp = "File not found";
			} else {
				String year = fileName.substring(17, 21);
				String month=fileName.substring(22, 25);
				internalcommservice.downLoadPassbook(fileName, year, response,month);
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return resp;
	}

}
