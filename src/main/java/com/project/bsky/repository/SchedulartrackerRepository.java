/**
 * 
 */
package com.project.bsky.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.Schedulartracker;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface SchedulartrackerRepository extends JpaRepository<Schedulartracker, Long> {
	

	List<Schedulartracker> findBystarttimeBetween(Date fromdate, Date todate);

	@Query("from Schedulartracker where to_char(starttime,'YYYY')=:year And to_char(starttime,'Mon')=:month And procedurename=:procedure  order by starttime asc")
	List<Schedulartracker> getdbschedularlist(String procedure, String month, String year);

}
