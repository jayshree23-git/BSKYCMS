package com.project.bsky.service;

import com.project.bsky.bean.Response;

/**
 * @author ronauk
 *
 */
public interface SNOConfigurationLogService {
	
	Response saveConfigurationLog(Integer userId, Integer createdBy, String ipAddress);
	
	Response saveConfigurationLogForHospital(String hospitalCode, Integer createdBy, String ipAddress);

}
