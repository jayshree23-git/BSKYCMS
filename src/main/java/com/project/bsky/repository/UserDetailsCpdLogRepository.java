package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.UserDetailsCpdLog;

/**
 * @author ronauk
 *
 */
@Repository
public interface UserDetailsCpdLogRepository extends JpaRepository<UserDetailsCpdLog, Integer> {

}
