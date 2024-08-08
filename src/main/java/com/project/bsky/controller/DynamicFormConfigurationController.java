package com.project.bsky.controller;

import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bsky.service.DynamicFormConfigurationService;

@RestController
@CrossOrigin(origins = "*")
public class DynamicFormConfigurationController {

	@Autowired
	private DynamicFormConfigurationService dynamicFormConfigurationService;
	
	@PostMapping(value = "/getFormName")
	public ResponseEntity<?> getFormAndModuleName(@RequestBody String formIdAndModuleId) throws JSONException {
		JSONObject response = dynamicFormConfigurationService.getFormAndModuleName(formIdAndModuleId);
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping(value = "/addFormConfig")
	public ResponseEntity<?> saveDynamicFormConfiguration(@RequestParam HashMap<String, String> formData)
			throws JSONException, SQLException {
		JSONObject response = dynamicFormConfigurationService.saveDynamicFormConfiguration(formData);
		return ResponseEntity.ok(response.toString());
	}

	@PostMapping(value = "/viewFormConfig")
	public ResponseEntity<?> getFormConfigurationDetails(@RequestBody String itemId) throws JSONException {
		JSONObject response = dynamicFormConfigurationService.getFormConfigurationDetails(itemId);
		return ResponseEntity.ok(response.toString());
	}

	/*
	 * 
	 * For-Save in Final table
	 * 
	 * 
	 */

	@PostMapping(value = "/finalSubmitData")
	public ResponseEntity<?> saveDataInFinalTable(@RequestBody String data) throws Exception {
		JSONObject response=dynamicFormConfigurationService.saveDataInFinalTable(data);
		return ResponseEntity.ok(response.toString());

	}


}
