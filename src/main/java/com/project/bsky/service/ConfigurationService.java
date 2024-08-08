/**
 * 
 */
package com.project.bsky.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.project.bsky.bean.Cpdconfigbean;
import com.project.bsky.bean.Cpdspecialitymappingbean;
import com.project.bsky.bean.HospitalBean;
import com.project.bsky.bean.Response;

/**
 * @author rajendra.sahoo
 *
 */
public interface ConfigurationService {

	List<Cpdconfigbean> gettaggedhospitalofcpd(Long userid);

	Response applyforexclusion(String hospitalcode, Integer bskyuserid);

	List<Cpdconfigbean> getappliedexclusionlistadmin();

	List<Cpdconfigbean> getappliedexclusionlistSNA(Long snoid);
	
	List<Cpdconfigbean> getappliedinclusionlistadmin();

	List<Cpdconfigbean> getappliedinclusionlistSNA(Long snoid);

	Response approveofexclusion(String hospitalcode, Integer bskyuserid,Integer userid, String ipaddress);

	List<HospitalBean> gethospitalforinclusion(Long userid, String state, String dist);

	Response applyhospitalforinclusion(String hospitalcode, Integer userid);

	Response approveofinclusion(String hospitalcode, Integer bskyuserid, Integer userid, String ipaddress);

	List<HospitalBean> getcpdtagginglog(Long userid);

	List<HospitalBean> getcpdtaggedhospital(Long userid, Integer type);

	Response cancelrequest(String hospitalcode, Long userid);

	List<Object> cpdhospitaltaglist(Long cpdId, Long cpduserid, Integer status, Long userid) throws Exception;

	List<Object> getcpdmappedPackageList(Long cpdId, Long cpduserid) throws Exception;

	Map<String, Object> saveCPDSpecialityMapping(Cpdspecialitymappingbean bean) throws Exception;

	void downloadcpdspecdoc(String fileName, String userid, HttpServletResponse response);

	List<Object> getcpdtaggedPackageList(Long cpduserid) throws Exception;

	List<Object> getspecilitywisecpdcount(Long userId, String speclty) throws Exception;

	List<Object> getspecilitywisecpdlist(Long userId, String packageid) throws Exception;
	

}
