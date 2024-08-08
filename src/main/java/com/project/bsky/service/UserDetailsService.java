package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.IntGrvUserBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.UserDetailsBean;
import com.project.bsky.model.OtpConfigLog;
import com.project.bsky.model.UserDetails;

public interface UserDetailsService {

	Integer checkUserDetailsByuserName(String userName);

	Integer checkUserDetailsByuserNameForHosp(String userName);

	Integer saveUserDetails(String fullName, String userName, String mobileNo, String emailId, String groupId,
			String createon);

	Integer updateUserDetails(String fullName, String userName, String mobileNo, String emailId, String groupId,
			String createon, String userid);

	List<UserDetails> findAll();

	List<UserDetails> findAllUsers(String userId);

	List<UserDetails> getDetailsFilteredByGroup(String groupId);

	UserDetailsBean getbyid(Long id);

	Response statusChange(Long userId, Integer status, Long updatedBy);

	Response checkStatus(Long userId);

	Integer updateUserLoginOtp(Map<String, Object> bean);

	List<OtpConfigLog> userLoginOtpLog(IntGrvUserBean configLog);

	Map<String, Object> getuserlistformobilenoupdate(Long userId, Integer grp) throws Exception;

	Map<String, Object> updatemobilenoofuser(Long userid, String mobile, String email, String rqst, String description, Long createby) throws Exception;

	Map<String, Object> getmobilenoupdateloglist(Long userId) throws Exception;

}
