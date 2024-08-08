/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

/**
 * @author priyanka.singh
 *
 */
public interface PackageWiseDischargeRptService {

	List<Object> getpackageWiseDischargedetails(Integer userId, String fromdate, String todate, String stateId,
			String districtId, String hospitalCode);

	List<Object> getpackageData(Integer userId, String state, String dist, String hosp, String packageHeader,
			String fromDate, String toDate);

	List<Object> getpackgebenificiarydata(Integer userId, String state, String district, String hospital,
			String fromDate, String toDate, String packageCode);

}
