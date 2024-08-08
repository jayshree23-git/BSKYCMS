package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.MobilenoActiveStatus;

@Repository

public interface MobileNoActiveStatusRepository extends JpaRepository<MobilenoActiveStatus,Long> {

	List<MobilenoActiveStatus> findByStatus(String status);

}
