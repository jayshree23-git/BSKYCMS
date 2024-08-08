package com.project.bsky.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.TOnlineServiceApplication;
@Repository
public interface TOnlineServiceApplicationRepository extends JpaRepository<TOnlineServiceApplication, Integer> {
	@Query("From TOnlineServiceApplication where intProfileId=:intprofileid and bitDeletedFlag=0")
	TOnlineServiceApplication getByIntProfileId2(Integer intprofileid);

//	@Transactional
//	@Modifying
//	@Query(value="update t_online_service_application set INTUPDATEDBY=:userId,STMUPDATEDON=:format where INTONLINESERVICEID=:intOnlineServiceId and BITDELETEDFLAG=0",nativeQuery=true)
//	void updateBy(Integer userId,String format, Integer intOnlineServiceId);
	
	@Transactional
	@Modifying
	@Query(value="update t_online_service_application set STMUPDATEDON=:format where INTONLINESERVICEID=:intOnlineServiceId and BITDELETEDFLAG=0",nativeQuery=true)
	void updateBy(String format, Integer intOnlineServiceId);
	
	
	
	@Transactional
	@Modifying
	@Query(value="update T_ONLINE_SERVICE_APPLICATION set VCHAPPLICATIONNO=:registrationNumber where INTONLINESERVICEID=:onlineServiceId and BITDELETEDFLAG=0",nativeQuery=true)
	void setRegdNumber(String registrationNumber, Integer onlineServiceId);

}
