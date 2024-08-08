/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.model.UnprocessConfigurationLog;

/**
 * @author priyanka.singh
 *
 */
public interface UnprocessConfigurationLogRepository extends JpaRepository<UnprocessConfigurationLog, Integer> {

}
