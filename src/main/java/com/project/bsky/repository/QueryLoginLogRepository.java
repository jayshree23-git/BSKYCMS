/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.QueryLoginMaterLog;

/**
 * @author santanu.barad
 *
 */
@Repository
public interface QueryLoginLogRepository extends JpaRepository<QueryLoginMaterLog, Long> {

}
