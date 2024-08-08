package com.project.bsky.service;

import com.project.bsky.bean.HospitalMappingBean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.HospitalPackageMapping;
import com.project.bsky.model.PackageDetailsHospital;
import com.project.bsky.model.PackageSubCategory;

import java.util.List;
import java.util.Map;

public interface HospitalPackageMappingService {

	List<PackageDetailsHospital> getpackageDetailsDescrition(Long packageSubcategoryId, Integer hospitalcategoryid);

	Response saveHospitalPackageMapping(HospitalMappingBean hospitalMappingBean);

	List<HospitalPackageMapping> getAllHospitalPackageMapping();

	HospitalPackageMapping getByHospitalpackageMapping(Long id);

	Response deleteHospitalMappingById(Long id);

	Response updateHospitalMappingById(Long id, HospitalMappingBean hospitalMappingBean);

	List<PackageSubCategory> getPackageSubcategory(String packageheadercode);

	Map<String, Object> getPackageDetailByCode(String packageCode, String subPackageCode, String procedureCode,
			String hospitalCode) throws Exception;

	String getAuthenticationDetails(String urn, String memberid, Integer flag, String Hospitalcode, String caseno) throws Exception;

	String getOverridecodeDetails(String overridecode, String urn, Long memberid, String hospitalcode) throws Exception;

}
