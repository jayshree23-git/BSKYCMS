/**
 * 
 */
package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.CheackCardBalancebean;
import com.project.bsky.model.AuthRequest;
import com.project.bsky.model.RationCardUser;

/**
 * @author rajendra.sahoo
 *
 */
public interface CheckCardBalanceService {

	CheackCardBalancebean checkcardbalance(String urn, String search, Long schemeId, Long schemeCategoryId);

	List<RationCardUser> getaccessuserlist();

	AuthRequest generateotp(Long accessid);

	AuthRequest validateotpchkbalance(Long accessid, String otp);

	CheackCardBalancebean checkbeneficry(String urn, String search, Long accessid, Long schemeId,
			Long schemeCategoryId);

	List<Object> beneficiarysearchbyname(String distid, String searchtype, String textvalue, Integer schemeId,
			String schemeCategoryId) throws Exception;

	List<Map<String, Object>> getDistrictListofnfsa() throws Exception;

	List<Object> getlistthroughurn(String urn, Integer schemeId, String schememCategoryId) throws Exception;

	Map<String, Object> getcarddetailsthroughurn(String urn, String search, Integer schemeidvalue,
			String schemeCategoryIdValue) throws Exception;

	String getCardBalanceLog() throws Exception;

}
