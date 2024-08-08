package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.model.UserDetails;
import org.json.JSONArray;
import org.json.JSONObject;

import com.project.bsky.bean.PrimaryLinkBean;
import com.project.bsky.bean.Response;

/**
 * @author ronauk
 *
 */
public interface AdminConsoleService {

	JSONArray getGlobalLinks(int userId);

	List<JSONObject> getPrimaryLinksFromGlobalLink(int userId);

	Response setPrimaryLinks(int userId, int createdby, List<PrimaryLinkBean> list) throws Exception;

	Response setPrimaryLinksForGroup(int groupId, int createdby, List<PrimaryLinkBean> list);

	List<PrimaryLinkBean> getPrimaryLinksFromUserId(int userId);

	Integer checkPrimaryLinksFromUserId(int userId);

	Response copyPrimaryLinksForCPD(int userId, int createdby);

	Response copyPrimaryLinksForHosp(int userId, int createdby);

	Response copyPrimaryLinksForUser(int userId, int createdby, int groupId);

	List<UserDetails> getLockedUserList();

	Map<String, Object> unlockUserByUserId(int userId);

}
