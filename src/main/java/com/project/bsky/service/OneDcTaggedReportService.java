/**
 * 
 */
package com.project.bsky.service;

/**
 * @author priyanka.singh
 *
 */
public interface OneDcTaggedReportService {

	String getHospitalByDistrictId(Integer userId, String districtCode, String stateCode,Integer dcId);

	String getDcDetailsList(Integer userId,Integer assignDc, String stateId1, String districtId1);

}
