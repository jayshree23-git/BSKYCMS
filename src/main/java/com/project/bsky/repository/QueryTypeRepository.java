package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.bean.QueryTypeBean;
import com.project.bsky.model.QueryType;

@Repository
public interface QueryTypeRepository extends JpaRepository<QueryType, Long> {

	@Query("SELECT count(*) FROM QueryType g WHERE g.typeName=:typeName")
	Integer countRowForCheckDuplicateType(String typeName);
	
	@Query("FROM QueryType where typeId.typeId=:typeId")
	QueryTypeBean findByUserId(Long typeId);

	@Query("FROM QueryType order by  typeId desc")
	List<QueryType> findDetails();

	QueryType findBytypeName(String typeName);

}
