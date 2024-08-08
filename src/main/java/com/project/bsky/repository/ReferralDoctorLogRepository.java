/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.ReferralDoctorLog;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface ReferralDoctorLogRepository extends JpaRepository<ReferralDoctorLog, Long> {

}
