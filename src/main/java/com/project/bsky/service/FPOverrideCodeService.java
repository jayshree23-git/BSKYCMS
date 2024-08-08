package com.project.bsky.service;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.FPOverrideListBean;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public interface FPOverrideCodeService {
	
	List<Object> getOverrideCode(String userId, String formDate, String toDate, String action, String aprvStatus,
			String hospitalcode);

	Response approveOverrideCode(String userId, Integer action, FPOverrideListBean bean);

	List<Object> getPatientDetails(String urn, Integer memberId, Date requestedDate, String hospitalCode,
			String generatedThrough);

	void downLoadOverrideFile(String fileName, String year, String hCode, HttpServletResponse response);
}
