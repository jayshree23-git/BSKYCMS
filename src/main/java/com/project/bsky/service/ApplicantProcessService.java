package com.project.bsky.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ApplicantProcessService {

	JSONObject getFormDetails() throws Exception;

	JSONObject getSchemeApplyDetails(String data) throws JsonProcessingException, Exception;

	JSONObject schemeApply(WebRequest request,Integer processId, Integer sectionId, Integer intOnlineServiceId, Integer profileId, Integer userId) throws Exception;

	JSONObject getPreviewDynamicForm(String data, WebRequest request) throws Exception;

	JSONObject applyForProcess(String data) throws Exception;

	List<Object[]> getQueryDetails(String data) throws JSONException;
	
}
