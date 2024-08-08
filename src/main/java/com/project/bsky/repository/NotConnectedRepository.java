package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.NotConnected;
@Repository

public interface NotConnectedRepository extends JpaRepository<NotConnected, Long>{

}
