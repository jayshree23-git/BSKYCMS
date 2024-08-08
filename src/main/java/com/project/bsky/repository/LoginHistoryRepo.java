package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bsky.model.LoginHistory;

public interface LoginHistoryRepo extends JpaRepository<LoginHistory, Long> {

}
