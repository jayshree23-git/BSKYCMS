package com.project.bsky.service;

import java.util.Date;
import java.util.List;

import com.project.bsky.bean.HospitalGroupAuthBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HospitalInformation;
import com.project.bsky.model.UserDetailsProfile;

public interface HospitalAuthorityService {

	List<UserDetailsProfile> getHospitalAuthDetails();

	List<HospitalInformation> getHospitalUserDetails();

	Response saveConfiguration(HospitalGroupAuthBean hospitalGroupBean);

	Integer checkHospitalName(String hospitalCode);

	String getHospitalAuthData(Long userId);

	String getConfigurationDetailsById(Integer userId);

	List<Object> getadminclaimTracking(Integer bskyUserId, String hospitalCode, Date fromDate, Date toDate, String urn);

	Response updateConfiguration(HospitalGroupAuthBean hospitalGroupBean);

}
