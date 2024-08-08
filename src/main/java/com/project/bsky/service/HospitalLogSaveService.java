package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HospitalInformationLog;

/**
 * @author ronauk
 *
 */
public interface HospitalLogSaveService {
	
	Response saveHospLog(Long userId, Integer createdBy);
	
	Response saveDcConfigurationLog(Long userId, Integer createdBy, String ipAddress);

	List<HospitalInformationLog> getgethostlogdata(Integer hospid);

	List<HospitalBean> getincentivelogdata(Integer hospid);

}
