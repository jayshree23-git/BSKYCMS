/**
 * 
 */
package com.project.bsky.service;

/**
 * @author priyanka.singh
 *
 */
public interface MonthWiseFloatDetailsReportService {

	String MonthWiseFloatDetails(Integer userId, String fromdate, String todate, String stateId, String districtId,
			String hospitalCode);

}
