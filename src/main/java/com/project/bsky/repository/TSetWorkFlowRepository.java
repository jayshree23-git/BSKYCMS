package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.TSetWorkFlow;
@Repository
public interface TSetWorkFlowRepository extends JpaRepository<TSetWorkFlow, Integer> {

	@Query(value="select GROUP_TYPE_NAME from GROUP_TYPE where TYPE_ID=:value",nativeQuery=true)
	String getRoleName(Integer value);

}
