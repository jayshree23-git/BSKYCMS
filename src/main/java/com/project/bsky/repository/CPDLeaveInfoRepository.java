package com.project.bsky.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.bean.CpdLeaveInfoBean;
import com.project.bsky.model.CPDLeaveInfo;
import com.project.bsky.model.UserDetailsCpd;

public interface CPDLeaveInfoRepository extends JpaRepository<CPDLeaveInfo, Long> {
	
	@Query("from CPDLeaveInfo where assignedsna=:userId and status=0 order by leaveId asc")
	List<CPDLeaveInfo> getallcpdleaveapplication(Long userId);
	
	

	@Query("from CPDLeaveInfo where actiontakenby=:userId")
	List<CPDLeaveInfo> findAlldata(Long userId);



	@Query("from CPDLeaveInfo where   CPD_USER_ID=:bskyUserId  order by leaveId desc")
	List<CPDLeaveInfo> findhistory(Integer bskyUserId);


	@Query("from CPDLeaveInfo where status=0")
	CPDLeaveInfo findByLeaveId(Long userId);


	@Query("from CPDLeaveInfo where status<2 and cpduserId=:userDetailsCpd order by leaveId desc")
	List<CPDLeaveInfo> findBycpduserId(UserDetailsCpd userDetailsCpd);


	@Transactional
	@Modifying
	@Query("UPDATE CPDLeaveInfo cd SET cd.status=3 where cd.leaveId=:leaveId")
	void deleteByleaveId(Long leaveId);


	@Query("from CPDLeaveInfo  where status<4  and createdby=:userId order by leaveId desc")
	List<CPDLeaveInfo> getcpdAllData(Long userId);
	
	

	@Query(value="select * from  cpd_leave_info WHERE ((?1 BETWEEN FROM_DATE AND TO_DATE) OR "
			+ "(?2 BETWEEN FROM_DATE AND TO_DATE) OR (?1 <= FROM_DATE AND ?2 >= TO_DATE) )"
			+ "AND CPD_USER_ID=?3 AND STATUS IN(0,1)" ,nativeQuery=true)
	List<CPDLeaveInfo> getLeaveBetweenTwoDate(Date fromDate1, Date todate1, Integer bskyUserId);



//	List<CPDLeaveInfo> findFilterAdminData(Long bskyUserId, Date fromDate, Date toDate1);


//	@Query("from CPDLeaveInfo  where   cpduserId.bskyUserId=nvl(:userDetailsCpd,cpduserId.bskyUserId) and formdate=:fromDate and todate=:toDate1")
//	List<CPDLeaveInfo> findFilterAdminData(Date fromDate, Date toDate1, Integer userDetailsCpd);


//	@Query("from CPDLeaveInfo  where   formdate=:formDate and todate=:toDate")
//	List<CPDLeaveInfo> findFilterDataCPD(Date formDate, Date toDate);
	
	@Query(value="select * from  cpd_leave_info WHERE ((?1 BETWEEN FROM_DATE AND TO_DATE) OR "
			+ "(?2 BETWEEN FROM_DATE AND TO_DATE) OR (?1 <= FROM_DATE AND ?2 >= TO_DATE) )"
			+ "AND CPD_USER_ID=?3",nativeQuery=true) 
	List<CPDLeaveInfo> findFilterDataCPD(Date formDate, Date toDate,Integer bskyUserId);

	@Query(value="select * from  cpd_leave_info WHERE ((?1 BETWEEN FROM_DATE AND TO_DATE) OR "
			+ "(?2 BETWEEN FROM_DATE AND TO_DATE) OR (?1 <= FROM_DATE AND ?2 >= TO_DATE) ) order by  CREATED_ON DESC",nativeQuery=true) 
	List<CPDLeaveInfo> findFilterDataadmin(Date fromDate, Date toDate1);
	
	
	
	

	
//	@Query("from CPDLeaveInfo where cpduserId=:bskyUserId and  fromdate=:formdate and todate=:todate")
//	List<CPDLeaveInfo> findFilterAdminData(Long bskyUserId, String fromdate, String toDate1);






}
