package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.GroupDetails;

public interface GroupDetailsRepo extends JpaRepository<GroupDetails, Integer> {

	@Query("from GroupDetails order by GroupId desc")
	List<GroupDetails> findAllGroupDetails();

}
