/**
 * 
 */
package com.project.bsky.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.project.bsky.bean.MEDischargeReportBean;
import com.project.bsky.bean.Response;

/**
 * @author priyanka.singh
 *
 */
public interface MEDischargeMonthlyReportService {

	MEDischargeReportBean MEDischargeMonthly(Integer userId, String fromdate, String todate, String stateId,
			String districtId, String hospitalCode, Integer serchtype);

	String getMonthWiseDischargeDetaiMe(Integer userId, String fromdate, String todate, String stateId,
			String districtId, String hospitalCode, Integer serchtype, String Package, String packageName);

	String getAdmissionBlockedDetails(String txnid, String pkgid, String userid);

	Response saveDischargeReport(MultipartFile pdf);

	void downloadDischargeRpts(String fileCode, HttpServletResponse response);

}
