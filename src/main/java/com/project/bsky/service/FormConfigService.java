package com.project.bsky.service;


import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.bsky.response.Root;

public interface FormConfigService {

	String saveAndUpdate(String manageFormRequest)throws JsonMappingException, JsonProcessingException, Exception;

	Root viewFormList(String manageFormRequest);

	List<Map<String, Object>>  getModules();

}
