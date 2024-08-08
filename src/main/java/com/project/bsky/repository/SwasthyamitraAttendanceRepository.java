/**
 * 
 */
package com.project.bsky.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.SwasthyamitraAttendance;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface SwasthyamitraAttendanceRepository extends JpaRepository<SwasthyamitraAttendance,Long> {
	@Query("from SwasthyamitraAttendance where to_char(date,'DD-Mon-YYYY')=:date And statusflag=0 And userId=:userId")
	SwasthyamitraAttendance finddate(String date, Integer userId);

	@Query("from SwasthyamitraAttendance where userId=:userId And to_char(date,'YYYY')=:year And to_char(date,'Mon')=:month  And statusflag=0 order by date desc")
	List<SwasthyamitraAttendance> attendancereport(String month, String year, Integer userId);

//	@Query("SELECT  userId,COUNT(*) FROM SwasthyamitraAttendance \r\n"
//			+ "WHERE  attendancestatus=1\r\n"
//			+ "AND to_char(date,'MON')=:month And to_char(date,'YYYY')=:year\r\n"
//			+ "And trim(TO_CHAR(date,'DAY')) != 'SUNDAY'\r\n"
//			+ "GROUP BY userId")
	
//	@Query("select t1.userId,t2.COUNT from\r\n"
//			+ "(select distinct userId from UserDetails where GroupId.typeId=14) t1\r\n"
//			+ "left join\r\n"
//			+ "(SELECT  userId,COUNT(*) FROM SwasthyamitraAttendance \r\n"
//			+ "WHERE  attendancestatus=1\r\n"
//			+ "AND to_char(date,'MON')=:month And to_char(date,'YYYY')=:year\r\n"
//			+ "And trim(TO_CHAR(date,'DAY')) != 'SUNDAY'\r\n"
//			+ "GROUP BY userId) t2\r\n"
//			+ "on t1.userid = t2.userId")
//	List<Object[]> getsmattendancereport(String year, String month);

}
