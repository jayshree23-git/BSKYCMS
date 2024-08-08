package com.project.bsky.service;

import com.project.bsky.bean.Response;

/**
 * @author ronauk
 *
 */
public interface UserDetailsCpdLogService {
	
	Response saveCpdLog(Long userId, Integer createdBy);

}
