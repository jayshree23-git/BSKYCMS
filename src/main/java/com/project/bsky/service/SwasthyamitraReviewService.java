/**
 * 
 */
package com.project.bsky.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.project.bsky.bean.Response;

/**
 * 
 */
public interface SwasthyamitraReviewService {

	List<Object> getsmhelpdeskregister(Date formdate, Date todate, String statecode, String distcode, String hospitalCode,
			Long smid, Integer status, Long userId) throws Exception;

	void downloadsmreviewdoc(String fileName, String year, String hospitalcode, HttpServletResponse response);

	List<Object> getsmpendingreport(Date formdate, Date todate, String statecode, String distcode, String hospitalCode,
			Long userId) throws Exception;

	List<Object> getsmlistforscoring(Integer year, Integer month, String statecode, String distcode, String hospitalCode, Long smid, Long userId) throws Exception;

	List<Object> getsmscoreview(Integer year, Integer month, Long userId) throws Exception;

	List<Object> getsmscoringreport(Integer year, Integer month, Long userId, String statecode, String distcode, String hospitalCode, Long smid) throws Exception;

	Map<String, Object> getsmdetailsforscoring(Long smid, String year, String month) throws Exception;

	Response submitsmscore(Long smid, Integer year, Integer month, String remark, Integer score, Long userId) throws Exception;

	List<Object> getsmfinalincenivereport(Integer year, Integer month, Long userId, String statecode, String distcode,
			String hospitalCode, Long smid) throws Exception;

}
