package com.project.bsky.service;

import java.util.List;

import com.project.bsky.bean.Response;
import com.project.bsky.bean.Submitspecialitybean;
import com.project.bsky.model.PackageHeader;

public interface PackageHeaderService {

	Response savepackageHeader(PackageHeader packageHeader);

	List<PackageHeader> getpackageHeader();

	Response deletepackageheader(Long headerId);

	PackageHeader getbypackageHeader(Long id);

	Response update(Long headerId, PackageHeader packageHeader);

	Long checkPackageHeaderName(String packageheadername);

	Long checkDuplicatePackageheadercode(String packageheadercode);

	List<Object> getAllpackageheaderdata();

	List<Object> getAllpackagesubctaegorydata(String packageheadercode);

	List<Object> getAllprocedurecodedata(String packagesubcode);

	List<Object> getviewspecialitydetails(String packageheadercode, String packagesubcode, String procedurecode,
			Integer searchtype);

	Response savespecilaityRequest(Submitspecialitybean resbean);

	List<Object> getpackagedetailslist(String procedurecode);

	Response activepackageheader(Long headerId, Long userid);

}
