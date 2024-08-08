/**
 * 
 */
package com.project.bsky.service;

import java.util.Date;
import java.util.List;

/**
 * @author rajendra.sahoo
 *
 */
public interface SnaActionReportLogService {

	List<Object> snaactionreport(Long userId, String year, String month);

	List<Object> snaactionreportdetails(Long userId, Integer actiontype, String date);

	List<Object> getsnawisepreauthcountdetails(Date fromdate, Date todate, Long snadoctor, Integer type)
			throws Exception;
}
