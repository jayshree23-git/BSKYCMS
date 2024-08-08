/**
 * 
 */
package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.ExpiredBeneficiaryBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.AuthRequest;

/**
 * @author priyanka.singh
 *
 */
public interface ExpiredBeneficiaryReportService {

	String expiredBeneficiaryDetails(Integer userId, String fromdate, String todate, String stateId, String districtId,
			String hospitalCode, String urn);

	Response expiredBeneficiaryUpdate(ExpiredBeneficiaryBean expiredBeneficiaryBean);

	AuthRequest generateotp(Long userId);

	AuthRequest validateotphosp(Long accessid, String otp);

	String aliveBeneficiaryDetails(Integer userId, String fromdate, String todate, String stateId, String districtId,
			String hospitalCode);

	List<Object> getactionlogofmakealive(Long claimid) throws Exception;

	Map<String, Object> getmortality(Long claimid) throws Exception;
}
