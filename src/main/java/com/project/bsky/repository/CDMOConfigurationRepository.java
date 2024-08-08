package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.CDMOConfiguration;



@Repository
public interface CDMOConfigurationRepository extends JpaRepository<CDMOConfiguration, Integer> {
	
	
	@Query("From CDMOConfiguration where cdmoUserId=:userid and status=0")
	CDMOConfiguration getCDMOByUserId(Integer userid);
	
	@Query("select c.mappingId,u.fullname,c.status,s.stateName,d.districtname,c.cdmoUserId,c.stateCode,c.districtCode from CDMOConfiguration c\r\n"
			+ "inner join UserDetails u on u.userId=c.cdmoUserId\r\n"
			+ "inner join State s on c.stateCode=s.stateCode\r\n"
			+ "inner join DistrictMaster d on d.districtcode=c.districtCode\r\n"
			+ "where d.statecode.stateCode=c.stateCode")
     List<Object[]> getDetails();
     
     
     @Query("select count(*) from CDMOConfiguration where STATE_CODE=:stateCode and DISTRICT_CODE=:districtCode")

	Integer checkduplicate(String stateCode, String districtCode);

     @Query("select count(*) from CDMOConfiguration where cdmoUserId=:cdmoId")
	Integer cheakduplicateorder(Integer cdmoId);

     @Query("from CDMOConfiguration where STATE_CODE=:stateCode and DISTRICT_CODE=:districtCode")
     CDMOConfiguration checkduplicate1(String stateCode, String districtCode);

      @Query("from CDMOConfiguration where cdmoUserId=:cdmoId")
      CDMOConfiguration cheakduplicateorder1(Integer cdmoId);

}
