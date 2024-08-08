/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.BskyDocumentmasterLog;

/**
 * 
 */

@Repository
public interface BskyDocumentmasterLogRepository extends JpaRepository<BskyDocumentmasterLog, Long>{

}
