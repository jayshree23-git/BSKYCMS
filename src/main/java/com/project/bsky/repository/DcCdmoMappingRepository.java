/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.DcCdmoMapping;

/**
 * 
 */
public interface DcCdmoMappingRepository extends JpaRepository<DcCdmoMapping, Long> {

	
	@Query("select count(1) from DcCdmoMapping where cdmouserId=:cdmoid and dcuserId=:dcUserId and statusflag=0")
	Integer checkduplicate(Long cdmoid, Long dcUserId);
	
	@Query("select statusflag from DcCdmoMapping where cdmouserId=:cdmoid and dcuserId=:dcUserId")
	Integer getstatus(Long cdmoid, Long dcUserId);

	@Query("from DcCdmoMapping where dcuserId=:dcUserId and statusflag=0")
	List<DcCdmoMapping> findBydcuserId(Long dcUserId);

	@Query("from DcCdmoMapping where cdmouserId=:cdmoid and dcuserId=:dcUserId")
	DcCdmoMapping findrecord(Long cdmoid, Long dcUserId);
	
	@Query("select count(1) from DcCdmoMapping where dcuserId=:dcUserId and statusflag=0")
	Integer checkdctagged(Long dcUserId);

	@Query("from DcCdmoMapping where dcuserId=:dcUserId and statusflag=0")
	DcCdmoMapping findBydcuserIdrecord(Long dcUserId);

}
