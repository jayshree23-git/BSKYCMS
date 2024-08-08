package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.PackageTaggingReportBean;
import com.project.bsky.bean.TaggingHistoryBean;

public interface TaggingHistoryService {

	List<TaggingHistoryBean> gettaggedhistory(String state, String dist, String hospital
			);

}
