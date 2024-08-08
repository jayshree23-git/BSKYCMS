/**
 * 
 */
package com.project.bsky.service;

import org.springframework.stereotype.Service;

import com.project.bsky.bean.GetServiceUpdateRecordBean;

/**
 * @author preetam.mishra
 *
 */
@Service
public interface AppUpDateTriggerService {

	/**
	 * @param getServiceUpdateRecordReq
	 * @return
	 */
	GetServiceUpdateRecordBean getAppServiceStatus(Integer onlineServiceId);

	/**
	 * @param getServiceUpdateRecordReq
	 * @return
	 */
	String insertAppUpdateInHistoryTbl(GetServiceUpdateRecordBean resBean);

}
