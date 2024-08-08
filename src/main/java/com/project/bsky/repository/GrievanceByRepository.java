package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.GrievanceBy;
import com.project.bsky.model.PackageMasterBSKY;
@Repository
public interface GrievanceByRepository  extends JpaRepository<GrievanceBy, Long>  {
	@Query("select count(*) from GrievanceBy where grivancename=:grivancename")
	Integer checkduplicateGrivancename(String grivancename);
	@Query("FROM GrievanceBy order by id desc")
	List<GrievanceBy> getDetails();
//	@Query("select count(*) from GrievanceBy where grivancename=:grivancename")
	GrievanceBy findByGrivancename(String grivancename);
}
