package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.DistrictMaster;
import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsPassResetLog;

public interface ResetPasswordService {

	List<Object> listData(Long userId) throws Exception;

	Response resetpassword(Long userId, Long createdBy);

	List<UserDetailsPassResetLog> listview(Long userId) throws Exception;

	List<UserDetails> AllUsers(String userName);

	public List<DistrictMaster> getDistrictListByStateCode(String stateCode);

	String getHospitalMasterData(String stateCode, String distCode);

}
