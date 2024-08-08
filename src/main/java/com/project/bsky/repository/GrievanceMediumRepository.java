package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.GrievanceMedium;
@Repository
public interface GrievanceMediumRepository extends JpaRepository<GrievanceMedium, Long> {
	
	@Query("Select Count(*) from GrievanceMedium where grivancemediumname=:grivancemediumname")
	Integer findBymediumname(String grivancemediumname);
	
	GrievanceMedium findBygrivancemediumname(String grivancemediumname);
	
	@Query("FROM GrievanceMedium order by id desc")
	List<GrievanceMedium> getDetails();
	
	List<GrievanceMedium> findByStatusFlagOrderByIdDesc(Integer statusFlag);

}
