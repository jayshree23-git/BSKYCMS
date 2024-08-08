package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.AllowUserForHospitalVisit;

@Repository
public interface AllowUserForHospitalVisitRepository extends JpaRepository<AllowUserForHospitalVisit, Long>{

	@Query(value = "select G.TYPE_ID,\r\n"
			+ "G.GROUP_TYPE_NAME,\r\n"
			+ "NVL(TU.HOSPITAL_VISIT_STATUS,1),\r\n"
			+ "NVL(TU.FACE_ATTENDANCE_STATUS,1)\r\n"
			+ "from group_type G\r\n"
			+ "LEFT JOIN TBL_USER_MOBILE_ACTIVITY_STATUS TU ON G.TYPE_ID=TU.GROUPID AND TU.STATUSFLAG=0\r\n"
			+ "where G.TYPE_ID IN (6,27)\r\n"
			+ "order by G.GROUP_TYPE_NAME",nativeQuery = true)
	List<Object[]> allowhospitalmobileactivitylist();

	@Query("select count(*) from AllowUserForHospitalVisit where groupid=:groupid")
	Integer checkduplicate(Long groupid);

	@Query("from AllowUserForHospitalVisit where groupid=:groupid")
	AllowUserForHospitalVisit getexistrecord(Long groupid);

}
