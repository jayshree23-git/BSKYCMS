package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.CPDConfigurationLog;

/**
 * @author ronauk
 *
 */
@Repository
public interface CPDConfigurationLogRepository extends JpaRepository<CPDConfigurationLog, Integer> {

}
