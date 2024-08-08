package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.Systemadminsnarejected;

public interface SystemadminsnarejectedService {

	List<Object> getsystemadminsnarejectedlist(Systemadminsnarejected systemadminrejected);

	Map<Long, List<Object>> getsystemAdminSnadetails(CPDApproveRequestBean requestBean);
}
