package com.project.bsky.service;

import com.project.bsky.entity.TOnlineServiceApproval;

public interface TOnlineServiceApprovalService {

	TOnlineServiceApproval getAllDetails(Integer processId, Integer serviceId);

	void save(TOnlineServiceApproval tOnlineServiceApproval);

}
