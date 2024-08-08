/**
 * 
 */
package com.project.bsky.service;

import com.project.bsky.bean.LoginBean;
import org.json.JSONArray;

import com.project.bsky.bean.AuthResponse;
import com.project.bsky.bean.Response;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.UserDetails;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author santanu.barad
 *
 */
public interface LoginService {

	public AuthResponse authenticate(HttpServletRequest httpServletRequest,Map<String, String> mapRequest) throws Exception;

	public Map<String, Object> changePassword(Map<String, String> mapRequest) throws Exception;

	public JSONArray getUserMappingList(Map<String, String> mapRequest);

	public AuthRequest forgotPassword(Map<String, String> mapRequest) throws Exception;

	public Response otpValidate(AuthRequest authRequest) throws Exception;

	public AuthRequest internalLogin(AuthRequest authRequest) throws Exception;

	public UserDetails changeuserpassword(Map<String, String> mapRequest) throws Exception;

	public AuthRequest requestOtp(AuthRequest authRequest) throws Exception;

	Map<String, Object> getSSOLoginInformation(LoginBean loginBean);

	Map<String, String> validateOtp(String authOtp, String username);
	
	String getProfilePath(Long userId);

	AuthResponse mobileApilogin(String username);
	
}
