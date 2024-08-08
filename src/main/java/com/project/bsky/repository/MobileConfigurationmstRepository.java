/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.MobileConfigurationmst;

/**
 * 
 */
@Repository
public interface MobileConfigurationmstRepository extends JpaRepository<MobileConfigurationmst, Long> {

	@Query(value="SELECT  G.TYPE_ID,\r\n"
			+ "        G.GROUP_TYPE_NAME,\r\n"
			+ "        CASE WHEN T.CONFIGURATION_ID IS NOT NULL THEN 0\r\n"
			+ "                ELSE 1\r\n"
			+ "        END STATUS\r\n"
			+ "    FROM GROUP_TYPE G\r\n"
			+ "LEFT JOIN TBL_MOBILE_ATTENDANCE_GROUP T ON T.GROUPID=G.TYPE_ID \r\n"
			+ "    AND T.STATUSFLAG=0 AND T.MOBILE_ATTENDANCE_STATUS=0\r\n"
			+ "WHERE G.STATUS=0\r\n"
			+ "ORDER BY STATUS,G.GROUP_TYPE_NAME",nativeQuery = true)
	List<Object[]> getconfigGroupList();

	@Query("from MobileConfigurationmst where groupid=:groupid and statusflag=0")
	MobileConfigurationmst getconfigid(Long groupid);

}
