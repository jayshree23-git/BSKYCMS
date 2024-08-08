/**
 * 
 */
package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.ClaimsQueriedToHospitalBySNOBean;

/**
 * @author rajendra.sahoo
 *
 */
public interface Noncompleincequeryreportservice {

	List<ClaimsQueriedToHospitalBySNOBean> getnoncompleincequerysno(String sno, String fromDate, String toDate,
			String package1, String packageName, String uRN);

}
