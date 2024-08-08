/**
 * 
 */
package com.project.bsky.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.project.bsky.bean.Response;
import com.project.bsky.model.DynamicReportConfiguration;
import com.project.bsky.model.DynamicReportDetailsmodel;
import com.project.bsky.model.TblMeAction;

/**
 * @author rajendra.sahoo
 *
 */
public interface DynamicReportService {

	Response SubmitdunamicConfiguration(DynamicReportConfiguration dynamicReport);

	List<DynamicReportConfiguration> getdynamicconfigurationlist();

	DynamicReportConfiguration getdynamicbyid(Long slno);

	Response updatedunamicConfiguration(DynamicReportConfiguration dynamicReport);

	List<Object> getdynamicreport(String fromdate, String todate, Integer trigger);

	Map<String, Object> getdynamicreportdetails(String fromdate, String todate, Integer flag, String report,
			String value);

	String getmeTreatmentHistoryoverpackgae(Long txnId, String urnnumber, String hospitalcode, String caseno,
			String uidreferencenumber, Long userid) throws Exception;

	Response sumbitmeremark(Long txnId, String remark, Long userid, String urn, Long claimid, Integer flag,
			Integer year, Integer month);

	List<DynamicReportDetailsmodel> getmeactiontakendetails(Long userid, Date fromdate, Date todate, Integer trigger);

	TblMeAction getmanderemark(Long txnid);

	List<Object[]> getsnoidbytxnid(Long txnid);

	List<Object> getmeactionlog(Long txnid);

	List<Object> getspecificcaseremarklist(Integer searchby, String fieldvalue, Long userId);

	public List<DynamicReportConfiguration> findAllActiveTrigger();

	List<Object> getmeabstractreport(String formdate, String todate, Integer trigger) throws Exception;

	Map<String, Object> getdynamicreportsubdetails(String fromdate, String todate, Integer flag, String report);

	List<DynamicReportDetailsmodel> getMeTriggerGrievancedetails(Long snaUserId, Date fromdate, Date todate,
			Integer trigger, String stateCode, String districtCode, String hospitalCode);

	Map<String, Object> getdynamicreportforexceldownload(String formdate, String todate, Integer trigger)throws Exception;

}
