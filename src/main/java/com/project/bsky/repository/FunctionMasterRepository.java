package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.FunctionMaster;

@Repository
public interface FunctionMasterRepository extends JpaRepository<FunctionMaster, Long>{

	@Query("from FunctionMaster order by functionId desc")
	List<FunctionMaster> findAlldata();

	@Query("from FunctionMaster where bitStatus=0 order by functionName asc")
	List<FunctionMaster> allactivefunctiondata();

	@Query("select count(*) from FunctionMaster where fileName=:fileName")
	Integer cheakduplicate(String fileName);

	FunctionMaster findByfileName(String fileName);
	
	@Query("select functionId from FunctionMaster where fileName=:url and bitStatus=0")
	Long findIdByUrl(String url);
	
	@Query("from FunctionMaster where fileName=:url")
	FunctionMaster findByUrl(String url);

}
