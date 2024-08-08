package com.project.bsky.service;

import com.project.bsky.bean.Response;

/**
 * @author ronauk
 *
 */
public interface CPDConfigurationLogService {
	
	Response saveConfigurationLog(Integer userId, Integer createdBy, String ipAddress);

}
