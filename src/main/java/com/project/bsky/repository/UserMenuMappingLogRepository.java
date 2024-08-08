/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.UserMenuMappingLog;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface UserMenuMappingLogRepository extends JpaRepository<UserMenuMappingLog, Long>{

}
