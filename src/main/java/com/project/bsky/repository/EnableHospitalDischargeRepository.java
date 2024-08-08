package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.EnableHospitalDischargeModel;

@Repository
public interface EnableHospitalDischargeRepository extends JpaRepository<EnableHospitalDischargeModel, Long>{

	 @Query("select count(*) from EnableHospitalDischargeModel where hospitalcode=:hospitalCode")
	 Integer checkduplicate(String hospitalCode);
     EnableHospitalDischargeModel findByhospitalcode(String hospitalCode);

}
