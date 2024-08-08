package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsPassResetLog;

@Repository
public interface UserDetailsPassResetLogRepository extends JpaRepository<UserDetailsPassResetLog, Long> {

	
	
//	@Query("FROM UserDetailsPassResetLog u order by logId desc")
//	List<UserDetailsPassResetLog> findAllResetData();

	@Query("FROM UserDetailsPassResetLog u where u.userId=:userDetails order by logId desc")
	List<UserDetailsPassResetLog> findByuserId(UserDetails userDetails);

}
