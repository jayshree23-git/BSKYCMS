package com.project.bsky.service;

import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public interface DynamicFormConfigurationService {

	JSONObject saveDynamicFormConfiguration(HashMap<String, String> formData) throws JSONException, SQLException;

	JSONObject getFormConfigurationDetails(String itemId) throws JSONException;

	JSONObject saveDataInFinalTable(String data) throws SQLException, Exception;

	JSONObject getFormAndModuleName(String formIdAndModuleId) throws JSONException;
	
}