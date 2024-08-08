package com.project.bsky.service;

import java.util.Map;

/**
 * @author ronauk
 *
 */
public interface UserProfileLogSaveService {

	Map<String, Object> saveProfileLog(Long userId, Long createdBy) throws Exception;

}
