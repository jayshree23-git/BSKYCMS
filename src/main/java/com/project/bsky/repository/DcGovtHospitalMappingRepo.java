package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.DcCdmoMapping;
import com.project.bsky.model.DcGovtHospitalMapping;

@Repository
public interface DcGovtHospitalMappingRepo extends JpaRepository<DcGovtHospitalMapping, Long>{

	@Query("select count(1) from DcGovtHospitalMapping where dcuserId=:dcUserId and hospitalId=:hospitalId and statusflag=0")
	Integer checkduplicate(Long hospitalId, Long dcUserId);
	
	@Query("select statusflag from DcGovtHospitalMapping where dcuserId=:dcUserId and hospitalId=:hospitalId")
	Integer getstatus(Long hospitalId, Long dcUserId);

	@Query("from DcGovtHospitalMapping where dcuserId=:dcUserId and hospitalId=:hospitalId")
	DcGovtHospitalMapping getrecord(Long hospitalId, Long dcUserId);

	@Query("from DcGovtHospitalMapping where dcuserId=:dcUserId and hospitalId=:hospitalId")
	DcGovtHospitalMapping findrecord(Long hospitalId, Long dcUserId);

	@Query("from DcGovtHospitalMapping where dcuserId=:dcUserId and statusflag=0")
	List<DcGovtHospitalMapping> findBydcuserId(Long dcUserId);

}
