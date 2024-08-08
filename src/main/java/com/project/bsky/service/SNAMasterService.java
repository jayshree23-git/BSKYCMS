package com.project.bsky.service;

/**
 * @author ronauk
 *
 */
public interface SNAMasterService {
	
	String getStateMasterDetails(String snoId);

	String getDistrictDetailsByStateId(String snoId, String stateId);

	String getHospitalByDistrictId(String snoId, String districtId,String stateId);

}
