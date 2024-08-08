package com.project.bsky.service;

import com.project.bsky.bean.QueryTypeBean;
import com.project.bsky.bean.Response;

import java.util.List;

import com.project.bsky.model.QueryType;

public interface QueryTypeService {

	Response saveQueryType(QueryTypeBean queryTypeBean);

	List<QueryType> getDetails();

	QueryType getQueryById(Long typeId);

	Response updateQueryType(QueryTypeBean queryTypeBean);

}
