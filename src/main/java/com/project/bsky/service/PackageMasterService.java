package com.project.bsky.service;

import java.sql.SQLException;
import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageMasterBSKY;

public interface PackageMasterService {

	List<PackageMasterBSKY> getDetails();

	PackageMasterBSKY getbyId(Long userid);

	Response savePackageMasterData(PackageMasterBSKY packageMasterBSKY);

	Response updatePackageData(PackageMasterBSKY packageMasterBSKY);

	public String getPackageByProcedure(String procedureCode) throws SQLException;

}
