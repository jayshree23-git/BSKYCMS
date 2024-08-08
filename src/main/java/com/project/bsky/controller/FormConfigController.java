package com.project.bsky.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.bean.ManageFormRequest;
import com.project.bsky.response.Root;
import com.project.bsky.service.FormConfigService;

@RestController
@CrossOrigin(origins = "*")
public class FormConfigController {

	@Autowired
	FormConfigService formConfigService;
	
	@Value("${dir.formIcon}")
	private String formIcon;
	
	@Value("${dir.formGuideline}")
	private String formGuideline;
	
	@Autowired
	private Logger logger;

	@PostMapping("/addManageForm")
	public ResponseEntity<String> createUpdateFormConfig(@RequestBody String viewFormRequest)
			throws Exception {

		Map<String, String> list = new HashMap<String, String>();
		String result = formConfigService.saveAndUpdate(viewFormRequest);
		return ResponseEntity.ok(result);

	}

	@PostMapping(value = "/viewManageFrom", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> viewFormList(@RequestBody String viewFormRequest) {

		ManageFormRequest manageFormRequest = new ManageFormRequest();
		List<HashMap<String, String>> result = null;
		Map<String, Object> list = new HashMap<String, Object>();
		Root listForm = null;
		JSONObject response = new JSONObject();

		try {
			listForm = formConfigService.viewFormList(viewFormRequest);
			list.put("result", listForm);
			list.put("status", "200");

			JSONObject object11 = new JSONObject(viewFormRequest);
			JSONObject jsonObj = new JSONObject(listForm);
			String obj = jsonObj.toString();
			String encodedResponseObject = Base64.getEncoder().encodeToString(obj.getBytes());
			response.put("RESPONSE_DATA", encodedResponseObject);
			response.put("RESPONSE_TOKEN", object11.getString("REQUEST_TOKEN"));

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response.toString());

	}

	@ResponseBody
	@PostMapping("/getmodules")
	public ResponseEntity<?> getModules() {
		List<Map<String, Object>> getmodules = null;
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			getmodules = formConfigService.getModules();
			response.put("status", 200);
			response.put("msg", "success");
			response.put("result", getmodules);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/downloadFormGuideline/{fileCode}")
	public ResponseEntity<?> downloadFormGuideline(@PathVariable("fileCode") String fileCode) throws IOException {

		Resource resource = null;
		//resource = getFileAsResource("D://ManageForm/FormGuideline/", fileCode);
		resource = getFileAsResource(formGuideline+"/", fileCode);

		if (resource == null) {
			return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
		}

		String contentType = "application/octet-stream";
		String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
	}

	public Resource getFileAsResource(String url, String fileCode) {
		Path dirPath = Paths.get(url + fileCode);

		try {
			if (dirPath != null) {
				return new UrlResource(dirPath.toUri());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return null;
	}

	@GetMapping("/downloadFormIcon/{fileCode}")
	public ResponseEntity<?> downloadFormIcon(@PathVariable("fileCode") String fileCode) throws IOException {

		Resource resource = null;
	//	resource = getFileAsResource("D://ManageForm/FormIcon/", fileCode);
		resource = getFileAsResource(formIcon+"/", fileCode);

		if (resource == null) {
			return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
		}

		String contentType = "application/octet-stream";
		String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
	}

//	@ResponseBody
//	@PostMapping("/getFormName")
//	public ResponseEntity<?> getFormName(@RequestBody String formIdAndModuleId) {
//		 List<Map<String, Object>> getFormName = null;
//		 Map<String, Object> response = new HashMap<String, Object>();
//		 
//		 try {
//			 getFormName = formConfigService.getFormName(formIdAndModuleId);
//			 response.put("status", 200);			 
//			 response.put("msg", "success");
//			 response.put("result",getFormName);
//		 }
//		 catch(Exception e) {
//			 e.printStackTrace();
//		 }
//	
//		 return ResponseEntity.ok(response);
//	}


}
