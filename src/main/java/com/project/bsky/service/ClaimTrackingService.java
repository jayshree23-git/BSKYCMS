package com.project.bsky.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.project.bsky.model.HospitalInformation;

/**
 * @author jayshree.moharana
 *
 */
public interface ClaimTrackingService {

	String getClaimdetails(Integer claimid) throws Exception;

	List<HospitalInformation> dchospitallist(Long userid);

	Map<Long, List<Object>> gethospitalclaimTracking(Date fromdate, String userid, Date toDate, String urn,
			Long searchby, String hospitalcode, Integer pageIn, Integer pageEnd);

	Map<Long, List<Object>> getclaimreport(Date fromdate, Date toDate, String urn, String claimno, String hospitalcode,
			Integer searchby, Integer pageIn, Integer pageEnd);

	String getvilatparameterdetails(String urn);
}
