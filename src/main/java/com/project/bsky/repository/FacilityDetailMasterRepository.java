/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.FacilityDetails;

/**
 * @author priyanka.singh
 *
 */
@Repository
public interface FacilityDetailMasterRepository extends JpaRepository<FacilityDetails, Integer> {

	@Query("select count(*) from FacilityDetails where facilityName=:facilityName")
	Integer checkFacility(String facilityName);

	@Query("FROM FacilityDetails  order by facilityName asc")
	List<FacilityDetails> findFacilityDetail();


}
