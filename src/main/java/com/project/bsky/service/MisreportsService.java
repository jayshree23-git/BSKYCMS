/**
 * 
 */
package com.project.bsky.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.project.bsky.bean.Cpdperformancebean;
import com.project.bsky.bean.MisreportsBean;
import com.project.bsky.bean.Snawiseunprocessedbean;
import com.project.bsky.bean.Treatmenthistorybeandetails;
import com.project.bsky.model.IcdSearchLog;

/**
 * @author rajendra.sahoo
 *
 */
public interface MisreportsService {

	Map<String, Object> treatmenthistory(String actioncode, Long treatmentid, Long packageid);

	Cpdperformancebean getcpdwiseperformace(String actioncode, Date fromdate, Date todate, Long cpdid, Long userid);

	List<Object> getcpdwiseperformacedetails(Integer actioncode, String fromdate, String todate, Long cpdid,
			Long userid, Integer serchtype);

	List<Object> getSnawiseunprocessedcountdetails(Snawiseunprocessedbean requestBean);

	List<Object> getDistrictListByStateIddcid(Long dcid, String stateid);

	List<Object> getHospitalbyDistrictIddcid(Long dcid, String stateid, String distid);

	Map<String, Object> gethospongingtreatmentdtls(Date formdate, Date toDate, Integer flag, String hospital,
			Long userId);

	Map<String, Object> getauthlivestatus(Date formdate, String state, String dist, String hospital, Long userId);

	List<Object> gethospitalauthdetails(Date formdate, Date todate, String flag, Integer type, String hospital);

	Treatmenthistorybeandetails blockedcaselogdetailsof1(Long txnid, Long pkgid, Long userid);

	void saveicdsearchlog(IcdSearchLog icdsearchlog);

}
