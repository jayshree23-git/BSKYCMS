package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.CpdLeaveApplyAdmin;
import com.project.bsky.model.CPDLeaveInfo;

public interface CpdLeaveApplyAdminService {
	
	List<CPDLeaveInfo> getCpdAllLeaveData(Long userId);

	Integer saveCpdLeaveForAdmin(CpdLeaveApplyAdmin cpdLeaveApplyAdmin);

	List<CPDLeaveInfo> getCPDLeaveFilterDataAdmin(Integer bskyUserId, String fromdate, String todate);
}
