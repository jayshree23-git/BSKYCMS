package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.GrievanceBy;
import com.project.bsky.model.PackageMasterBSKY;

public interface GrievanceByService {
	Response saveGrievancebyData(GrievanceBy grievanceby);

	List<GrievanceBy> getDetails();
	GrievanceBy getgrievancebyId(Long userid);
	Response updateGrievanceBy(GrievanceBy grievanceby);


}
