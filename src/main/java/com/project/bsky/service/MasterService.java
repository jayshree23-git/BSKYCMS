/**
 * 
 */
package com.project.bsky.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.project.bsky.model.User;

/**
 * @author ronauk
 *
 */
public interface MasterService {

	String getStateMasterDetails();

	String getDistrictDetailsByStateId(String stateId);

	String getSNODetails();

	String getHospitalByDistrictId(String districtId, String stateId);

	String getCPDDetails();

	String getUserDetails();

	String getDCDetails();

	String getDistrictDetailsByStateAndDistrictCode(String stateCode, String distCode);

	JSONArray getGroups();

	String getSNAEXDetails();

	String getCDMODetails();

	String getMonths();

	String getYears();

	List<User> getDClist();

	String getHospCatMaster();

	List<Map<String, String>> getDistrictByStateCode(List<String> list) throws Exception;

	List<Map<String, String>> getHospitalByDistrictCode(List<String> Statecodeforhospitallist,
			List<String> districtcode) throws Exception;

	Map<String, Object> getDischargeandClaimsummarydetailsinnerpage(Long userid, String month, String years,
			Integer searcby, List<String> satedata, List<String> districtdata, List<String> hospitaldata)
			throws Exception;

	String getblockByDistrictId(String districtCode, String stateCode);

	List<Object> getDClistCDMO(Long userId);

	List<Object> getDClistByStateAndDist(String stateId, String distId);

	List<Map<String, String>> getHospitalByDCUserId(List<Integer> list) throws Exception;

	public String getSNAList();

	List<Map<String, String>> getdcwiseHospitaldetailsformulticheckbox(List<String> statecodeforhospitallist,
			List<String> districtcode, Long userid) throws Exception;

	String getDistrictDetailsByNFSAData();

	public List<Map<String, Object>> getSchemeDetails(Map<String, Object> request);

	List<Object> getHospitalPackageSchemeWiseDataForPackageNameData(String schemeId, String schemeCategoryId);

	List<Object> getHospitalPackageHeadercode(String schemeId, String schemeCategoryId, String acronym);
	Map<String, Object> getMappedAuthDetails(Map<String, Object> response) throws SQLException, Exception;
}
