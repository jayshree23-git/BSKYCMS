package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.GrievanceMedium;

public interface GrievanceMediumService {
	
	Response saveGrivanceMediumData(GrievanceMedium grivancemedium);

	List<GrievanceMedium> getGrivanceMediumData();

	Response updateGrivanceMediumData(GrievanceMedium grivancemedium);

	GrievanceMedium getgrievanceMediumbyId(Long userid);

	List<GrievanceMedium> getGrivanceMediumList();

}
