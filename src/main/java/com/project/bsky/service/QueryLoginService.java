/**
 * 
 */
package com.project.bsky.service;

import java.util.Map;

import org.json.JSONObject;

import com.project.bsky.bean.AuthResponse;
import com.project.bsky.model.AuthRequest;

/**
 * @author santanu.barad
 *
 */
public interface QueryLoginService {
	public String authenticate(AuthRequest authRequest) throws Exception;

	public Map<String, Object> queryRequest(AuthRequest authRequest) throws Exception;
}
