/**
 * 
 */
package com.project.bsky.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.project.bsky.bean.Hospital;
import com.project.bsky.bean.Response;
import com.project.bsky.bean.Smattendancereportbean;
import com.project.bsky.bean.SwasthyaMitraBean;
import com.project.bsky.bean.SwasthyaMitraGeoTagBean;

/**
 * @author rajendra.sahoo
 *
 */
public interface SwasthyamitraAttendanceService {

	List<Smattendancereportbean> getsmattendancereport(Integer year, Integer month);

	Response saveswasthyamitra(Long userid, List<Hospital> hospital, Integer created);

	List<Object> getsmmappinglist();

	List<Object> getsmtaggedhospital(Long userid);

	Response updateswasthyamitra(Long userid, List<Hospital> hospital, Integer updated, Integer status);

	Response inactiveSwasthyaMitra(Long userid, Long bskyid, Integer statusflag);

	List<Object> getDistinctSwasthyaMitra(Integer userId);

	List<SwasthyaMitraBean> getswasthyaMitraDetails(Integer groupId, String stateId, String districtId);

	List<SwasthyaMitraBean> getswasthyaMitraFilter(String stateId, String districtId);

	List<Object> getsmlistbyhospital(String hospital);

	List<Object> getsmlistforregistaration(String state, String dist, String hospital, String smid);

	Map<String, Object> getsmdetailsforregister(Long smid);

	Response allowsmforregister(Integer smid, Integer updateby);

	List<Object> getapprovesmlistforregistaration(String state, String dist, String hospital, String smid);

	List<Object> getsmlogdetails(Long smid);

	Map<String, Object> swasthyaMitraGeoTagUpdate(SwasthyaMitraGeoTagBean geoTagBean);

}
