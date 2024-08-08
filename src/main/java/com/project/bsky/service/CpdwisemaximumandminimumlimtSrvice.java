package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.Response;

public interface CpdwisemaximumandminimumlimtSrvice {

	Response savecpdwisemaximumandminimumlimt(Long cpdid, Long maxlimit, Long puserid, String Assigneduptodate);

	List<Object> cpdwisemaximumandminimumlimtview(String cpdid);

	Map<String, Object> cpdwisemaximumandminimumlimttogetupdatedata(String cpduserid);

	Response cpdwisemaximumandminimumlimtupdaterecord(Long cpdid, Long maxlimit, Long userid,
			String updatedassigneduptodate);

}
