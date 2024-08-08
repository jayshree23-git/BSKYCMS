package com.project.bsky.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.entity.OnlineServiceApprovalNotings;
import com.project.bsky.repository.OnlineServiceApprovalNotingsRepository;
import com.project.bsky.service.OnlineServiceApprovalNotingsService;

@Service
public class OnlineServiceApprovalNotingsServiceImpl implements OnlineServiceApprovalNotingsService {
	@Autowired
	private OnlineServiceApprovalNotingsRepository onlineServiceApprovalNotingsRepository;
	@Override
	public OnlineServiceApprovalNotings saveAll(OnlineServiceApprovalNotings onlineServiceApprovalNotings) {
		OnlineServiceApprovalNotings onlineServiceApprovalNoting=onlineServiceApprovalNotingsRepository.save(onlineServiceApprovalNotings);
		return onlineServiceApprovalNoting;
	}
}
