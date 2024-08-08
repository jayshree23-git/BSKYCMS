/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.ReferalDoctor;

/**
 * @author rajendra.sahoo
 *
 */
public interface ReferralDoctorRepository extends JpaRepository<ReferalDoctor, Long> {

	@Query("from ReferalDoctor where userid.userId=:cpdId and status=0")
	ReferalDoctor findByuserid(Long cpdId);

}
