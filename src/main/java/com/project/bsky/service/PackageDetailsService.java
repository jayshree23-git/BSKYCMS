package com.project.bsky.service;

import java.util.List;
import com.project.bsky.bean.PackageDetailsMasterBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageDetailsMaster;

/**
 * @author Bhasha
 *
 */
public interface PackageDetailsService {

	Response savePackageDetails(PackageDetailsMasterBean packageDetailsMasterBean);

	List<PackageDetailsMaster> getPackageDetails();

	PackageDetailsMaster getByPackageDetailsId(Long id);

	String getByPackageDetailsIds(Long id);

	Response deletePackageDetails(Long id);

	Response updatePackageDetails(Long id, PackageDetailsMasterBean packageDetailsMasterBean);

}
