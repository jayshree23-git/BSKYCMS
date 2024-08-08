/**
 * 
 */
package com.project.bsky.service;

import java.util.Date;
import java.util.List;

/**
 * @author rajendra.sahoo Dt-20/11/2023
 */
public interface BeneficiaryDistrictwisedataService {

	List<Object> getbenificiaryblockwisedata(Long age, String ageconditions, String distcode, Long userid)
			throws Exception;

	List<Object> getbenificiarygpwisedata(Long age, String ageconditions, String distcode, String blockcode,
			Long userid);

	List<Object> getbenificiaryvillagewisedata(Long age, String ageconditions, String distcode, String blockcode,
			String gpcode, Long userid) throws Exception;

	List<Object> getstatedashboarddata(Date formdate, Date todate) throws Exception;

}
