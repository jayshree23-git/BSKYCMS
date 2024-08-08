/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.IcdSearchLog;

/**
 * 
 */
@Repository
public interface IcdSearchLogRepository extends JpaRepository<IcdSearchLog, Long>{

}
