package com.project.bsky.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.ReferralBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HealthCardSample;
import com.project.bsky.model.Scheme;
import com.project.bsky.model.SchemeCategoryMaster;

public interface ReferralService {
	Response saveReferals(ReferralBean referral);

	void saveDoc(MultipartFile file, Long refId);

	List<Object> getPatientDetails(Integer userId, Date fromDate, Date toDate, String hospitacode);

	List<Object> getPatientDataByID(Long id);

	List<HealthCardSample> getNameByCardNo(Integer schemeCategoryId,String urn);

	HealthCardSample getAgeByName(String name);

	Response updatePatientDetails(ReferralBean bean);

	void downloadFileForReferral(String fileName, String year, String hCode, HttpServletResponse response);

	List<Object> gethospitallistdcwise(Long userid);
	
	List<SchemeCategoryMaster> getSchemeCategoryById(Integer schemeId);
	
	List<Scheme> getSchemeList();

}
