package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.CpdLeaveInfoBean;
import com.project.bsky.bean.CpdassignedhospitaldetailsBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.CPDLeaveInfo;

public interface CPDLeaveInfoService {

	List<CPDLeaveInfo> getallcpdleaveapplication(Long userId);

	CPDLeaveInfo getcpddetails(Long userId);

	Response approverequest(Long leaveId, Integer approve, Long createby);

	List<CpdassignedhospitaldetailsBean> cpdmappingdetails(Long user);

	Integer saveCPDLeaveInfo(CpdLeaveInfoBean cpdLeaveInfoBean);

	List<CPDLeaveInfo> getcpdactiondetails(Long userId);

	List<CPDLeaveInfo> getcpdleavestatus(Long userId);

	CPDLeaveInfo findAllByLeaveId(Long leaveId);

	void deletebyLeaveId(Long leaveId);

	List<CPDLeaveInfo> getcpdleavehistory(Long user);

	List<CPDLeaveInfo> getallcpdleavefilterrequest(Long user, String formdate, String todate);

	List<CPDLeaveInfo> getallcpdfilteractiondetails(Long user, String formdate, String todate);

	List<CPDLeaveInfo> getCPDLeaveFilterData(Integer userId,String fromdate, String todate);
}
