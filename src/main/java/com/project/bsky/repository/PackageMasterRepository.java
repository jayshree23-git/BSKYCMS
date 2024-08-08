package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.PackageMasterBSKY;

@Repository
public interface PackageMasterRepository extends JpaRepository<PackageMasterBSKY, Long> {

	@Query("FROM PackageMasterBSKY order by id desc")
	List<PackageMasterBSKY> getDetails();

	@Query("select count(*) from PackageMasterBSKY where procedureCode=:procedureCode")
	Integer checkduplicateProcedureCode(String procedureCode);

	@Query("select count(*) from PackageMasterBSKY where procedures=:procedures")
	Integer checkduplicateProcedure(String procedures);

	PackageMasterBSKY findByprocedureCode(String procedureCode);

	PackageMasterBSKY findByprocedures(String procedures);

}
