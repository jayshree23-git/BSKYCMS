package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.PackageDetailsHospital;
import com.project.bsky.model.Ward;


@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
	@Query("SELECT g.wardMasterId FROM Ward g WHERE g.wardName=:wardName")
	Long getWardMasterIdByWardName(String wardName);
	@Query("SELECT g.wardMasterId FROM Ward g WHERE g.implantCode=:wardCode")
	Long getWardMasterIdByWardCode(String wardCode);
//	@Query("SELECT g.wardMasterId FROM Ward g WHERE g.wardCode=:wardCode")
//	List<Ward> findDetails(Long wardMasterId);
	 @Query("FROM PackageDetailsHospital where hospitalCategoryId=:hospitalCategoryId")
	List<PackageDetailsHospital> findDetails(Integer hospitalCategoryId);
 
	
}
