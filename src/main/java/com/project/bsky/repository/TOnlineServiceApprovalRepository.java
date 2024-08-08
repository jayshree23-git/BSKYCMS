package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.TOnlineServiceApproval;
@Repository
public interface TOnlineServiceApprovalRepository extends JpaRepository<TOnlineServiceApproval, Integer> {

	/**
	 * @param intprofileid
	 * @return
	 */
	TOnlineServiceApproval getByIntProfileId(Integer intprofileid);
	
	@Query(value = "select TS.TXTNoting,TS.dtActionTaken,TS.intNotingsId,MP.vchProcessName from T_ONLINE_SERV_APP_NOTINGS TS join M_PROCESS_NAME MP ON TS.intProcessId=MP.intProcessId where TS.bitDeletedFlag=0 AND TS.intOnlineServiceId=:intOnlineServiceId AND TS.intProcessId=:intProcessId", nativeQuery = true)
	List<Object[]> getQueryDetails(String intOnlineServiceId, String intProcessId);
	
	@Query(value ="select * from T_ONLINE_SERVICE_APPROVAL where INTPROCESSID=:processId AND INTONLINESERVICEID=:serviceId AND BITDELETEDFLAG=0", nativeQuery = true)
	TOnlineServiceApproval getAllDetails(Integer processId, Integer serviceId);
	
	@Query("From TOnlineServiceApproval where intOnlineServiceId=:onlineServiceId and bitDeletedFlag=0")
	TOnlineServiceApproval findByOnlineServiceId(Integer onlineServiceId);
	
	@Query("From TOnlineServiceApproval where intProfileId=:intprofileid and bitDeletedFlag=0")
	TOnlineServiceApproval getByIntProfileId2(Integer intprofileid);

}
