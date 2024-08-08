package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.UserDetails;

public interface UsermanulaRepository extends JpaRepository<UserDetails, Long>{

	
	@Query("select userId FROM UserDetails WHERE status=0 and GroupId.typeId=:groupId order by userId")
	List<Long> getAllGroup(Integer groupId);
}
