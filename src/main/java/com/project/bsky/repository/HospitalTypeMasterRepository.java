/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.HospitalTypeMaster;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface HospitalTypeMasterRepository extends JpaRepository<HospitalTypeMaster, Integer> {

	@Query("from HospitalTypeMaster where status=0 order by hospitaltypename asc")
	List<HospitalTypeMaster> getallbydesc();

	@Query("select count(*) from HospitalTypeMaster where hospitaltypename=:hospitaltypename")
	Integer checkduplicate(String hospitaltypename);
	
	@Query("from HospitalTypeMaster where hospitaltypename=:hospitaltypename")
	HospitalTypeMaster getduplicate(String hospitaltypename);

}
