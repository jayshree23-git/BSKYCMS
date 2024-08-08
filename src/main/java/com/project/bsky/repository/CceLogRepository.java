package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.CceLog;

import java.util.List;

@Repository
public interface CceLogRepository extends JpaRepository<CceLog, Long> {
    CceLog findByTRANSACTIONID(Long transactionId);

}
