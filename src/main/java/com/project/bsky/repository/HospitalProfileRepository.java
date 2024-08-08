/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.bsky.model.HospitalProfile;

/**
 * @author ipsita.shaw
 *
 */
public interface HospitalProfileRepository extends JpaRepository<HospitalProfile, Integer> {

	@Query("FROM HospitalProfile U WHERE U.uploadUserID =:userId ")
	HospitalProfile getHospitalProfileRepository(@Param("userId") Integer userId);

}
