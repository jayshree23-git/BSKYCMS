package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.UserDetailsProfileLog;

/**
 * @author ronauk
 *
 */
@Repository
public interface UserDetailsProfileLogRepository extends JpaRepository<UserDetailsProfileLog, Integer> {

}
