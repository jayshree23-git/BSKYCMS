/**
 * 
 */
package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import com.project.bsky.bean.Dccdmomappingbean;
import com.project.bsky.bean.Response;
import com.project.bsky.model.AllowUserForHospitalVisit;
import com.project.bsky.model.MobileConfigurationmst;
import com.project.bsky.model.MobileUserConfiguration;

/**
 * 
 */
public interface DcCdmoMappingSevice {

	List<Object> getuserDetailsbygroup(Integer groupid) throws Exception;

	Response saveDcCdmomapping(Dccdmomappingbean bean) throws Exception;

	List<Object> getdccdmomaplist(Long dcId, Integer group) throws Exception;

	List<Object> getmapingbydcid(Long dcId) throws Exception;

	Response updateDcCdmomapping(Dccdmomappingbean bean) throws Exception;

	List<Object> getdccdmomapcount(Long dcId, Integer group) throws Exception;

	List<Object> taggedlogdetails(Long dcId) throws Exception;

	Response saveDcHospitalmapping(Dccdmomappingbean bean) throws Exception;

	List<Object> getdcgovthospmapcount(Long dcId, Integer group) throws Exception;

	List<Object> getgovthospbydcid(Long dcId, Integer group) throws Exception;

	List<Object> taggedHOSDClogdetails(Long dcId) throws Exception;

	Response updateDcHospitalmapping(Dccdmomappingbean bean) throws Exception;

	List<Object> getdcfacelist(Long dcId, Integer group) throws Exception;
	
	Map<String, Object> getdctaggeddetails(Long dcid) throws Exception;

	Map<String, Object> updatedcfacelist(Long faceid,Long userid) throws Exception;

	List<Object> getfacelogdetails(Long dcid, Integer group) throws Exception;

	List<Object> allowhospitalmobileactivitylist() throws Exception;

	Map<String, Object> allowhospitalmobileactivitylist(List<AllowUserForHospitalVisit> list) throws Exception;

	List<Object> getconfigGroupList()throws Exception;

	Map<String, Object> savegroupmobilemast(List<MobileConfigurationmst> list) throws Exception;

	List<Object> getconfiggroupdata(Long userId) throws Exception;

	Map<String, Object> saveusermobileconfig(List<MobileUserConfiguration> list) throws Exception;

	List<Object> getconfiggroupalldata() throws Exception;

	List<Object> getusermobileconfiglist(Long userId) throws Exception;

	Map<String, Object> savegroupwisemobileconfig(List<MobileUserConfiguration> list) throws Exception;

}
