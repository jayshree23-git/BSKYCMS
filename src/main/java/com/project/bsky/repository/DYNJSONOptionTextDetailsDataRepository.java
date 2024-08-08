/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.DYNJSONOptionTextDetailsData;

/**
 * @author preetam.mishra
 *
 */
@Repository
public interface DYNJSONOptionTextDetailsDataRepository extends JpaRepository<DYNJSONOptionTextDetailsData, Integer> {

	@Query(value="from DYNJSONOptionTextDetailsData where intId=:intId1 and tableName=:tableName and onlineServiceId=:intOnlineServiceId")
	DYNJSONOptionTextDetailsData getDataByintIdTableNameServiceId(Integer intId1, String tableName,
			Integer intOnlineServiceId);

}
