package com.project.bsky.service;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CCEOutBoundService {

	Map<Long, List<Object>> getCceOutBoundData(String userId, String formDate, String toDate, String action,
			String hospitalCode, Long cceId, Integer pageIn, Integer pageEnd, String queryStatus, String stateCode,
			String distCode);
	Map<Long, List<Object>> getDgoCallCenterData(String userId, String formDate, String toDate, String action, String hospitalCode,
			Long cceId,Integer pageIn, Integer pageEnd,String queryStatus,String stateCode,String distCode);


	Integer updateCCeOutBound(String userId, Long cceId, String urn, String hospitalCode, MultipartFile cceDoc1,
			MultipartFile cceDoc2, MultipartFile cceDoc3, String dateOfAdmission, String remarks, String action,
			String alternateNo);

	Integer updateDgoCallCenterData(String userId, Long cceId, String urn, String hospitalCode, MultipartFile dgoDoc,
			String dateOfAdmission, String remarks, Integer action);

	Map<Long, List<Object>> getSupervisorCallCenterData(String userId, String formDate, String toDate, String action,
			String hospitalCode, Long cceId, Integer cceUserId, Integer pageIn, Integer pageEnd, String stateCode,
			String distCode);

	Response addReassignRemark(Long id, String reAssignRemarks, Integer reAssignFlag, Integer reAssignUser);

	List<UserDetails> getUserNameByGroupId();

	String getDistrictList(String stateCode, Long userId);

	String getHospital(String stateCode, String distCode, Long userId);

	Map<Long, List<Object>> getITACceOutBoundData(String userId, String formDate, String toDate, String action,
			String hospitalCode, String stateCode, String distCode);

	Map<Long, List<Object>> getITADgoCallCenterData(String userId, String formDate, String toDate, String action,
			String hospitalCode, String stateCode, String distCode);

	Response saveReassignRemark(Long id, String reAssignRemarks, Integer reAssignUser, Long cceUserId);
}
