

package com.project.bsky.service;

import java.util.List;
/**
 * @author jayshree.moharana
 *
 */

public interface CpdActionReportService {

	List<Object> cpdactionreport(Long userId, String year, String month);

	List<Object> cpdactiontakenreport(Long userId, String date);

	List<Object> getcpdactiontekendetails(Long userId, String date, Integer type);

}
