/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.UserDetailsProfile;
import com.project.bsky.model.hospitalOperatorLog;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface HospitalOperatorLogRepository extends JpaRepository<hospitalOperatorLog, Long>{

}
