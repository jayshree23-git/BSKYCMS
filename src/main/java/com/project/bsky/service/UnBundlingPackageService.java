package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.UnBundlingPackage;

/**
 * @author rajendra.sahoo
 *	DT- 06-11-2023
 */
public interface UnBundlingPackageService {

	Response SubmitdunamicConfiguration(UnBundlingPackage unBundlingPackage);

	List<UnBundlingPackage> getdynamicconfigurationlist();

	UnBundlingPackage getunBundlingPackagebyid(Long slno);

	Response updatedunamicConfiguration(UnBundlingPackage unBundlingPackage);

}
