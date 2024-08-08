/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.model.ReferralDoctorMappingLog;

/**
 * @author rajendra.sahoo
 *
 */
public interface ReferralDoctorMappingLogRepository extends JpaRepository<ReferralDoctorMappingLog, Long> {

}
