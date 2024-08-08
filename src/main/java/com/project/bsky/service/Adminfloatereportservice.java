/**
 * 
 */
package com.project.bsky.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.project.bsky.bean.Response;
import com.project.bsky.model.TxnclamFloateDetails;

/**
 * @author rajendra.sahoo
 *
 */
public interface Adminfloatereportservice {

	List<Object> snaFloatDataforrevert(Date formdate, Date todate, Long userid, String floateno) throws Exception;

	List<Object> snaFloatrevertData(Date formdate, Date todate, Long userid) throws Exception;

	Response snaFloatrevert(Date formdate, Date todate, Long userid, String floateno) throws Exception;

	List<Object> getsnafreezelist(Date formdate, Date todate, Long userid, String state, String dist, String hospital) throws Exception;

	Response applyforunfreeze(Date formdate, Date todate, Long userid, String claimid) throws Exception;

	List<Object> getsnafreezelistforapprove(Date fromdate, Date todate, Long userid, String state, String dist, String hospital) throws Exception;

	Response approveforunfreeze(Long userid, String claimid) throws Exception;

	List<Object> getspecialfloatereport(Date todate, Date formdate, Long userId, Long snaid) throws Exception;

	List<Object> gethospwisependingclaimdetails(String floatNumber, String hospcode) throws Exception;

}
