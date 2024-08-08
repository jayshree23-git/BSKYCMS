package com.project.bsky.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.OnlineServiceApprovalNotings;


@Repository
public interface OnlineServiceApprovalNotingsRepository extends JpaRepository<OnlineServiceApprovalNotings, Integer> {

	@Query("select INTONLINESERVICEID, INTPROCESSID from OnlineServiceApprovalNotings")
	List<Object[]> getIdFromNoting();

	@Query(value = "from OnlineServiceApprovalNotings where INTPROCESSID =:prId and INTONLINESERVICEID =:serId AND BITDELETEDFLAG = 0 ORDER By DTACTIONTAKEN DESC")
	List<OnlineServiceApprovalNotings> getRemark(Integer prId, Integer serId);

	@Query(value = "select GROUP_TYPE_NAME from GROUP_TYPE where type_id =:tId", nativeQuery = true)
	public String getGroupTypeName(Integer tId);

	@Query(value = "SELECT nvl(COUNT(T2.INTONLINESERVICEID),0) CNT FROM t_online_service_approval T1 \r\n"
			+ "INNER JOIN t_online_serv_app_notings T2 ON T1.INTONLINESERVICEID=T2.INTONLINESERVICEID AND T2.BITDELETEDFLAG=0\r\n"
			+ "where T2.intprocessid=583 AND T2.INTFROMAUTHORITY=:authType AND T2.INTSTATUS=6 AND T1.BITDELETEDFLAG=0 AND T2.INTONLINESERVICEID=:serviceId", nativeQuery = true)
	Integer getQueryCount(Integer serviceId,Integer authType);
}
