/**
 * 
 */
package com.project.bsky.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.project.bsky.bean.BulkDateExtensionBean;
import com.project.bsky.bean.CPDApproveRequestBean;
import com.project.bsky.bean.ClaimLogBean;
import com.project.bsky.bean.CpdQueryToHospitalBean;
import com.project.bsky.bean.DateExtensionBean;
import com.project.bsky.bean.NonComplianceBean;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.Snawiserununprocessedupdate;
import com.project.bsky.bean.Snawiseunprocessedbean;
import com.project.bsky.bean.SystemRejQueryCpdBean;

/**
 * @author hrusikesh.mohanty
 *
 */
public interface CpdSystemRejectedListService {

	List<CpdQueryToHospitalBean> getRejetedData(String hospitalCode, String fromDate, String toDate, String package1,
			String packageCodedata, String uRN, String schemeid, String schemecategoryid);

	String getRequestByDetailId(Integer txnId) throws Exception;

	public Response saveRejectRequestCPD(SystemRejQueryCpdBean rejBean) throws ParseException;

	List<Object> getRejetedDataCPDToSNA(CPDApproveRequestBean requestBean);

	String getNonComplianceClaimListById(Integer txnId) throws Exception;

	public Response saveClaimSNANonComplianceDetails(ClaimLogBean logBean) throws Exception;

	List<Object> getNonComplianceExtn(NonComplianceBean requestBean) throws ParseException;

	public Response saveNonComplianceDateExtension(DateExtensionBean logBean);

	List<Object> getRejetedDataSNAToSNA(CPDApproveRequestBean requestBean);

	List<Object> getUnprocessedCountData(NonComplianceBean requestBean);

	Response RunUnprocessed(NonComplianceBean requestBean) throws Exception;

	List<Object> getBulkNonComplianceExtn(NonComplianceBean requestBean) throws ParseException;

	public Response saveBulkNonComplianceDateExtension(BulkDateExtensionBean logBean) throws ParseException;

	List<Object> getSnawiseunprocessedcountdetails(Snawiseunprocessedbean requestBean);

	Response getSnawiseunprocesseupdatet(Snawiserununprocessedupdate snawiseunprocessed);

	List<Object> getNonComplianceExtensionview(Integer action, Long userid);

	List<Object> getunprocessedsummarydetails(Date fromdate, Date todate, String state, String state2, String dist,
			String hospital, Integer flag);
}
