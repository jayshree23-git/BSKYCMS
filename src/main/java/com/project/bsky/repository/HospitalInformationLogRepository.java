package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.HospitalInformationLog;

/**
 * @author ronauk
 *
 */
@Repository
public interface HospitalInformationLogRepository extends JpaRepository<HospitalInformationLog, Integer> {

	@Query("from HospitalInformationLog where hospitalId=:hospid order by logId desc")
	List<HospitalInformationLog> findByhospitalId(Integer hospid);

	@Query(value = "SELECT to_char(HCL.HC_VALID_FROM_DATE,'DD-MON-YYYY') fromdate,to_char(HCL.HC_VALID_TO_DATE,'DD-MON-YYYY') todate,\r\n"
			+ "to_char(HCL.NONNABHCHANGEDATE,'DD-MON-YYYY') createon ,hcm.hospitalcategoryname typename\r\n"
			+ "FROM hospitalcategory_log HCL\r\n"
			+ "LEFT JOIN hospitalcategorymaster HCM ON hcm.hospitalcategoryid=hcl.hospital_categoryid\r\n"
			+ "WHERE HCL.hospitalinfo_id=142\r\n"
			+ "order by ID desc", nativeQuery=true)
	List<Object[]> getincentivelogdata(Integer hospid);

}
