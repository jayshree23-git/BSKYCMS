/**
 * 
 */
package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.PrimaryLinkBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.FunctionMaster;
import com.project.bsky.model.GlobalLink;
import com.project.bsky.model.PrimaryLink;
import com.project.bsky.model.UserMenuMapping;

/**
 * @author rajendra.sahoo
 *
 */
public interface PrimaryLinkService {

	List<GlobalLink> getgloballinklist();

	List<FunctionMaster> getgloballink();

	Response save(PrimaryLinkBean primarylinkbean);

	List<PrimaryLink> findall();

	Response deleteprimarylink(Long userid);

	PrimaryLink getprimarylinkbyid(Long userid);

	Response update(PrimaryLinkBean primarylinkbean);

	List<PrimaryLink> filterdata(String globalid, String primaryid, String functionid);

	List<PrimaryLink> getrespmlist(Long gid);

	Map<String, Object> checkPrimaryLinkAccess(Map<String, String> mapRequest);

	List<UserMenuMapping> getMISReportList(Integer userId, Integer globalId);

}
