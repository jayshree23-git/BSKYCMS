/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

/**
 * @author priyanka.singh
 *
 */
public interface SNAWiseDischargeandClmReportService {

	List<Object> getSNADischargeclaimData(Long snoUserId, Long year);

	List<Object> snamonthwisedischargelist(Long userId, String year, Integer month);

	List<Object> hospitalwisedischargelist(Long userId, Integer year, Integer month, String statecode, String distcode,
			String hospcode);

}
