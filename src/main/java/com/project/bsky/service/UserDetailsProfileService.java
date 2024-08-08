package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.UserDetailsProfileBean;
import com.project.bsky.model.UserDetailsProfile;

/**
 * @author ronauk
 *
 */
public interface UserDetailsProfileService {

	UserDetailsProfile saveUserDetailSNO(UserDetailsProfileBean userDetaisSNOBean);

	List<UserDetailsProfile> listSNODetail();

	Integer checkSNOByUserName(String userName);

	UserDetailsProfile findAllBybskyUserId(Integer bskyUserId);

	UserDetailsProfile getUserProfileFromUserId(Map<String, String> mapRequest) throws Exception;

	int updateSNOData(UserDetailsProfileBean userDetaisSNOBean);

	UserDetailsProfile getsno(Integer userId);

	Integer checkUserByuserName(String userName);

	List<UserDetailsProfile> findAllFilteredByDistrict(String groupId, String stateId, String districtId);

}
