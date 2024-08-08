package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.model.PackageDetailsHospital;
import com.project.bsky.model.Ward;

public interface WardService {

	Response saveWard(Ward ward);

	List<Ward> getward();

	Response deleteWard(Long wardMasterId);

	Ward getbywardMasterId(Long wardMasterId);

	Response update(Long wardMasterId, Ward ward);

	Long checkDuplicateWardName(String wardName);

	Long checkDuplicateWardCode(String wardCode);

	List<PackageDetailsHospital> getpackageDetailsDescrition(Integer hospitalCategoryId);

}
