package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.bsky.bean.CDMOForwardBean;
import com.project.bsky.bean.FeedbackCallingReport;
import com.project.bsky.bean.LoggedUserDetailsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.config.dto.NotingDto;
import com.project.bsky.config.dto.NotingParameter;

public interface WorkflowService {

	List<Map<String, Object>> getallOfficersApi();

	List<Map<String, Object>> getallApprovalAction();

	String setWorkflow(String setWorkflowRequest) throws Exception;

	JSONObject getApplication(String request) throws Exception;

	JSONObject getAuthAction(String request) throws Exception;

	String takeAction(JSONObject takeActionReq) throws JSONException;

	String getStateId(Long serviceId);

	/**
	 * @param request
	 * @return
	 * @throws Exception
	 */
	JSONObject getApplicationList(String request) throws Exception;

	/**
	 * @param serviceId
	 * @param labelId
	 * @return
	 */
	JSONObject fillWorkflow(String serviceId, String labelId);

	List<NotingDto> getNotingIds(NotingParameter notingParameter);

	String getDistByUserName(String username);

	LoggedUserDetailsBean getUserDetails(LoggedUserDetailsBean bean);

	JSONObject getGrievanceReport(String request) throws Exception;

	public Response saveCDMOForwardDetails(@RequestBody CDMOForwardBean logBean);

	String getCDMOActionTakenDetails(Integer userId, String fromDate, String toDate) throws Exception;

	String getCCEFeedbackReportDetails(FeedbackCallingReport logBean) throws Exception;

//	List<Map<String, Object>> getDistrictList();
}
