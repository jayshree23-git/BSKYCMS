package com.project.bsky.serviceImpl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bsky.model.NotConnected;
import com.project.bsky.repository.NotConnectedRepository;
import com.project.bsky.service.NotConnectedService;

@Service

public class NotConnectedServiceImpl implements NotConnectedService {
	@Autowired
	private NotConnectedRepository notConnectedRepository;
	
	@Autowired
	private Logger logger;

	@Override
	public NotConnected getbynotconnectedCall(Long id) {
		NotConnected notConnected=null;
		try {
			notConnected=notConnectedRepository.findById(id).get();
		}catch(Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return notConnected;
	}
	
	
}
