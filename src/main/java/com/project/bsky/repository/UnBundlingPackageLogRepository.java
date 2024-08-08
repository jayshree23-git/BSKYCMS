/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.model.UnBundlingPackageLog;

/**
 * @author rajendra.sahoo
 *
 */
public interface UnBundlingPackageLogRepository extends JpaRepository<UnBundlingPackageLog, Long> {

}
