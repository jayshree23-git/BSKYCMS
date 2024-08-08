/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.FoRemarkLog;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface FoRemarkLogRepository extends JpaRepository<FoRemarkLog, Long> {

}
