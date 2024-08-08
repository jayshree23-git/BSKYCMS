/**
 * 
 */
package com.project.bsky.service;

/**
 * @author priyanka.singh
 *
 */
public interface OldClaimProcessBlockRptService {

	String oldclaimprocessblockData(Integer userId, String fromdate, String todate, String stateId,
			String districtId, String hospitalCode);

	

	String getOldDischargeData(Integer userId, String fromdate, String todate, String stateId, String districtId,
			String hospitalCode);

}
