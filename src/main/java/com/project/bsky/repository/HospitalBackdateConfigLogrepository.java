/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.HospitalBackdateConfigLog;

/**
 * @author rajendra.sahoo
 *
 */
public interface HospitalBackdateConfigLogrepository extends JpaRepository<HospitalBackdateConfigLog, Long> {

	@Query("From HospitalBackdateConfigLog where status=0 order by hospitalconfigid desc")
	List<HospitalBackdateConfigLog> getallhospitallogdata();

}
