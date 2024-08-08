/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.MstSchedularLog;

/**
 * @author rajendra.sahoo
 *
 */
public interface MstSchedularLogRepository extends JpaRepository<MstSchedularLog, Long> {

	@Query(value = "select sl.SCHEDULAR_NAME,\r\n"
			+ "        sl.CREATED_BY,U.FULL_NAME,\r\n"
			+ "        To_char(sl.CREATED_ON,'DD-MON-YYYY HH:MI:SS AM'),\r\n"
			+ "        sl.PURPOSE,\r\n"
			+ "        (case when sl.status=0 then 'Active' else 'In-Active' End),sl.REMARKS \r\n"
			+ "    from mst_scheduler_log sl \r\n"
			+ "    LEFT JOIN userdetails U ON U.USERID=sl.CREATED_BY\r\n"
			+ "    where sl.id=?1\r\n"
			+ "    order by log_id desc",nativeQuery = true)
	List<Object[]> getloghistory(Long scheduler);

}
