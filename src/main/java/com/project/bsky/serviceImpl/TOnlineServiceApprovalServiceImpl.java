package com.project.bsky.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.entity.TOnlineServiceApproval;
import com.project.bsky.repository.TOnlineServiceApprovalRepository;
import com.project.bsky.service.TOnlineServiceApprovalService;

@Service
public class TOnlineServiceApprovalServiceImpl implements TOnlineServiceApprovalService {
@Autowired
private TOnlineServiceApprovalRepository tOnlineServiceApprovalRepository;
	@Override
	public TOnlineServiceApproval getAllDetails(Integer processId, Integer serviceId) {
		////System.out.println(processId+"---------"+serviceId);
		return tOnlineServiceApprovalRepository.getAllDetails(processId, serviceId);
	}
	@Override
	public void save(TOnlineServiceApproval tOnlineServiceApproval) {
		tOnlineServiceApprovalRepository.save(tOnlineServiceApproval);
		
	}

}
