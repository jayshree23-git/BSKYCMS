/**
 * 
 */
package com.project.bsky.service;

import java.util.Date;
import java.util.List;

/**
 * @author priyanka.singh
 *
 */
public interface OldBlockDataReportService {

	List<Object> OldBlockData(Integer userId, String fromdate, String todate, String stateId, String districtId,
			String hospitalCode);

	List<Object> OldBlockDataReportList(Integer userId, String reportData, String stateId, String districtId,
			String hospitalCode);

	List<Object> getoldblockgenericsearch(String fieldvalue) throws Exception;

	List<Object> getoldblockdataviewlist(Date formdate, Date todate, String stetecode, String distcode,
			String hospitalcode) throws Exception;

	Object getoldblockdataviewdetails(Long txnid) throws Exception;

}
