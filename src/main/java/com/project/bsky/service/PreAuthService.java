package com.project.bsky.service;

import com.project.bsky.bean.CPDPreauthActionBean;
import com.project.bsky.bean.PreAuthDetails;
import com.project.bsky.bean.PreAuthGroupBean;
import com.project.bsky.bean.Response;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PreAuthService {

	List<Object> getPreAuthorizationData(CPDPreauthActionBean requestBean);

	Response updatePreAuthorizationData(String userId, Integer action, PreAuthGroupBean preAuthGroupBean);

	void downLoadPreauthFile(String fileName, String year, String hCode, HttpServletResponse response);

	public String getPreAuthDetails(Long txnId, String urn) throws Exception;

	public Response updatetPreAuthorizationDeatails(PreAuthDetails preAuthDetails) throws Exception;

	public String getPreAuthCaseDetails(Long txnId) throws Exception;

	public Response updateSpecialtyRequest(PreAuthDetails preAuthDetails) throws Exception;

}
