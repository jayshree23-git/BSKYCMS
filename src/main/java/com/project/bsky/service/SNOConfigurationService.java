/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import org.json.JSONArray;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.SNOConfigurationBean;
import com.project.bsky.bean.SnaExecBean;
import com.project.bsky.model.SNOConfiguration;

/**
 * @author ronauk
 *
 */
public interface SNOConfigurationService {

	Response saveSNOConfiguration(SNOConfigurationBean bean);

	List<SNOConfigurationBean> getAllSnoConfigurationDetails(String stateId, String districtId);

	SNOConfigurationBean getSnoConfigurationDetailsById(Integer snoMappingId);

	SNOConfiguration getSnoConfFromHospCode(String hospitalCode);

	Response updateSnoDetailsData(SNOConfigurationBean sNOConfigurationBean);

	Response updateSnoDetailsDataById(SNOConfigurationBean sNOConfigurationBean, Integer snoMappingId);

	Integer checkSnoName(Integer SnoUserId);

	List<Integer> checkSnoStatus(int snoUserId);

	Integer checkHospitalName(String hospitalCode, Integer snoId);

	Integer checkHospitalNameForSNA(String hospitalCode, Integer snoUserId);

	JSONArray getDistinctSNA(Integer userId);

	JSONArray getSNAConfigDetails(Integer userId, String stateId, String districtId);

	void assignClaimsToSna(Integer snoId, List<String> hospitalCodes);

	Response saveSNAExecutiveConfiguration(SnaExecBean bean);

	JSONArray getSNAExec(Integer userId);

	JSONArray getSNAExecDetails(Integer userId);

	SnaExecBean getSnaExecutiveById(Integer snoMappingId);

	Response updateSnaExecDetails(SnaExecBean bean);

	String getSnaListByExecutive(Integer userId)throws Exception;
	
	String getHospitalListById(Integer userId)throws Exception;
}
