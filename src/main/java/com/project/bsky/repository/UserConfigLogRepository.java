/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.model.OtpConfigLog;

/**
 * @author santanu.barad
 *
 */
public interface UserConfigLogRepository extends JpaRepository<OtpConfigLog, Long> {
	List<OtpConfigLog> findAllByUserIdAndStatusFlag(Long userId, Long statusFlag);
}
