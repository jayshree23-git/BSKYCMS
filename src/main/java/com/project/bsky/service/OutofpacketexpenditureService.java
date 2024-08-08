package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.BskyDocumentmaster;
import com.project.bsky.model.OutofpacketexpenditureMaster;

public interface OutofpacketexpenditureService {
	Response savemst(OutofpacketexpenditureMaster outofpacketexpendituremaster) throws Exception;

	List<Object> getexpendituremst() throws Exception;

	Response update(OutofpacketexpenditureMaster outofpacketexpendituremaster) throws Exception;


}
