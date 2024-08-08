package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.SNOConfigurationLog;

/**
 * @author ronauk
 *
 */
@Repository
public interface SNOConfigurationLogRepository extends JpaRepository<SNOConfigurationLog, Integer> {

}
