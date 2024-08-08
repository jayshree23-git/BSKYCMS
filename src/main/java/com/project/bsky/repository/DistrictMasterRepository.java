/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.bsky.model.DistrictMaster;

/**
 * @author satyabrata.s
 *
 */
public interface DistrictMasterRepository extends JpaRepository<DistrictMaster, Integer> {

	// SELECT h.hospitalId FROM HospitalInformation h WHERE
	// h.hospitalCode=:hospitalCode"
//	@Query("SELECT c.districtcode FROM DistrictMaster WHERE c.districtcode=:districtcode")
//	DistrictMaster findBycode(DistrictMaster districtMaster);

//	
	@Query("FROM DistrictMaster WHERE districtcode=:DistrictId")
	DistrictMaster findBycode(String DistrictId);

	@Query("FROM DistrictMaster where districtcode=:districtCode")
	DistrictMaster finddistrictBycode(String districtCode);

	@Query("FROM DistrictMaster where districtcode=:districtCode and statecode.stateCode=:stateCode")
	DistrictMaster getdistrict(String stateCode, String districtCode);

	@Query("SELECT d.districtname FROM DistrictMaster d WHERE d.statecode.stateCode=:stateCode AND d.districtcode=:districtCode")
	String getDistrictNameByStateCodeAnAndDistrictCode(@Param("stateCode") String stateCode,
			@Param("districtCode") String districtCode);

	@Query("FROM DistrictMaster d where d.statecode.stateCode = :stateCode order by d.districtname")
	List<DistrictMaster> getDistrictDetailsByStateId(@Param("stateCode") String stateCode);

	@Query("SELECT districtname FROM DistrictMaster where statecode.stateCode=:statecd and districtcode=:districtcd")
	String getDistrictName(String statecd, String districtcd);

	@Query(value="SELECT distinct\r\n"
			+ "                     d.districtid,\r\n"
			+ "                     d.statecode,\r\n"
			+ "                     d.districtcode,\r\n"
			+ "                     d.districtname\r\n"
			+ "                FROM\r\n"
			+ "                     district d\r\n"
			+ "                     inner join hospital_info u on u.DISTRICT_CODE=d.districtcode AND u.STATE_CODE=d.statecode AND u.status_flag=0\r\n"
			+ "                     where u.ASSIGNED_DC=?1\r\n"
			+ "                     and u.STATE_CODE=?2",nativeQuery=true)
	List<Object[]> getDistrictListByStateIddcid(Long dcid, String stateid);

	@Query(value="select STATECODE,DISTRICTCODE,BLOCKCODE,BLOCKNAME from block where STATECODE=?2 and DISTRICTCODE=?1",nativeQuery=true)
	List<Object[]> getblockByDistrictId(String districtCode, String stateCode);

	@Query(value="select D.DISTRICTID,D.DISTRICTNAME,D.LGDDISTCODE from DISTRICT_NFSA D",nativeQuery=true)
	List<Object[]> getDistrictListofnfsa();

//	@Query("SELECT districtname FROM DistrictMaster where statecode=:stateCode and districtcode=:districtCode")
//	DistrictMaster getDistrictName(String stateCode, String districtCode);
	

}
