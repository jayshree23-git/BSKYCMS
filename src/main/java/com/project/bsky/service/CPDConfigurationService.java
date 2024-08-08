/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import org.json.JSONArray;

import com.project.bsky.bean.CPDConfigurationBean;
import com.project.bsky.bean.HospObj;
import com.project.bsky.bean.Response;

/**
 * @author ronauk
 *
 */
public interface CPDConfigurationService {

	Response saveCPDConfiguration(CPDConfigurationBean cpdConfigurationBean);

	CPDConfigurationBean getCpdConfigurationDetailsById(Integer cpdUserId);

	Response updateCpdDetailsData(CPDConfigurationBean cPDConfigurationBean);

	Integer checkCpdName(Integer cpdUserId);

	List<Integer> checkCpdStatus(Integer cpdUserId);

	Integer checkHospitalName(String hospitalCode);

	Integer checkCPDHospitalName(String hospitalCode, Integer cpdId);

	JSONArray getDistinctCPD(Integer bskyUserId);

	JSONArray getCPDConfigDetails(Integer bskyUserId, String stateId, String districtId);

	String checkCPDAssignedClaims(Integer cpdId, List<HospObj> hospitals);

	List<String> getRestrictedHospitals(Integer cpdId);

}
