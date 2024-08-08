package com.project.bsky.repository;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.UnlinkedFunctionMaster;
import com.project.bsky.model.UserDetails;

@Repository
public interface UnlinkedFunctionMasterRepository extends JpaRepository<UnlinkedFunctionMaster, Long> {

	@Query("from UnlinkedFunctionMaster order by functionId desc")
	List<UnlinkedFunctionMaster> findAlldata();

	@Query("from UnlinkedFunctionMaster where bitStatus=0 order by functionName asc")
	List<UnlinkedFunctionMaster> allactivefunctiondata();

	@Query("select count(*) from UnlinkedFunctionMaster where fileName=:fileName")
	Integer cheakduplicate(String fileName);

	UnlinkedFunctionMaster findByfileName(String fileName);

	@Query("select functionId from UnlinkedFunctionMaster where fileName=:url and bitStatus=0")
	Long findIdByUrl(String url);

	@Query("from UnlinkedFunctionMaster where fileName=:url")
	UnlinkedFunctionMaster findByUrl(String url);

	@Query("select count(*) from UnlinkedFunctionMaster where fileName=:fileName and userId=:userDetails")
	Integer cheakUnlinkedDuplicateFunctionMaster(@NotNull String fileName, UserDetails userDetails);

}
