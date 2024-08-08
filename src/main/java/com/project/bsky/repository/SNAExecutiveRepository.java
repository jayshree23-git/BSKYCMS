package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.SNAExecutive;

public interface SNAExecutiveRepository extends JpaRepository<SNAExecutive, Integer> {

	@Query("FROM SNAExecutive WHERE snauserid=:snoid and snaexecutiveId=:snaExecutive and status=0")
	SNAExecutive findBySNAID(Integer snoid,Integer snaExecutive);
	
	@Query("FROM SNAExecutive WHERE snauserid=:snoid and status=0")
	List<SNAExecutive> findListBySNAID(Integer snoid);
	
	@Query("FROM SNAExecutive WHERE snauserid=:snoid and snaexecutiveId=:snaExecutive")
	List<SNAExecutive> findBySNAInactiveID(Integer snoid,Integer snaExecutive);

}
