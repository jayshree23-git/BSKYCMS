package com.project.bsky.repository;

import com.project.bsky.model.ClaimFileErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimFileErrorLogRepository extends JpaRepository<ClaimFileErrorLog, Long> {
}