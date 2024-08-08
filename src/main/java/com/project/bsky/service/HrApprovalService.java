/**
 * 
 */
package com.project.bsky.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.Response;

/**
 * @author santanu.barad
 *
 */
public interface HrApprovalService {
	public String getFreshApplication(CPDApproveRequestBean requestData);

	public String getFreshApplicationDetails(Long userId);

	public Response scheduleApplication(Map<String, Object> requestData) throws ParseException;

	public Response finalApproveApplication(Map<String, Object> requestData) throws ParseException;

	public String getViewApplication(CPDApproveRequestBean requestData);

	void commonDownloadMethod(String fileName, String prifix, String userid, HttpServletResponse response)
			throws IOException;

	public String getApprovedApplicationDetails(Long userId);

}
