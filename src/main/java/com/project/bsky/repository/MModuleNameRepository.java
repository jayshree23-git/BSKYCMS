package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.MModuleName;


@Repository
public interface MModuleNameRepository extends JpaRepository<MModuleName, Integer> {

	
	@Query(value=":finalQuery",nativeQuery = true)
	void insertValue(String finalQuery);
	
//	@Query("from MModuleName where bitDeletedFlag = 0 and (:moduleId=0 or intModuleId=:moduleId)")
//	List<MModuleName> getByModuleId(Integer moduleId);

	
}