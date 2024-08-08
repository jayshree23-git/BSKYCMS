/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

/**
 * @author priyanka.singh
 *
 */
public interface YearWiseGenderDataReportService {

	List<Object> getbenificiarygenderdtls(Long age, String ageconditions);

	List<Object> getbenificiarygenderblockdata(String districtId);

	List<Object> getbenificiarygendergramdetails(String districtId, String blockId);

	List<Object> getbenificiarygendervillagedata(String districtId, String blockId, String gramId);

	List<Object> getbenificiarydata(String districtId, String blockId, String gramId, String villageId);

}
