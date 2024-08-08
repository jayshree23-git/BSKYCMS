package com.project.bsky.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.GroupTypeDetails;
import com.project.bsky.model.NotificationDetails;

@Repository
public interface NotificationDetailsRepository extends JpaRepository<NotificationDetails, Long> {


	//List<NotificationDetails> findBycreateonBetween(Date fromDate1, Date todate1);

	@Query("from NotificationDetails where fromDate >=:fromDate1 And toDate<=:todate1 order by createdOn asc")
	List<NotificationDetails> findBydataBetween(Date fromDate1, Date todate1);

	@Query("from NotificationDetails where fromDate >=:fromDate1 And toDate<=:todate1 And groupId=:groupTypeDetails order by createdOn asc")
	List<NotificationDetails> findBydataBetween1(Date fromDate1, Date todate1, GroupTypeDetails groupTypeDetails);

//	@Query("from NotificationDetails where fromDate >=:fromDate1 And toDate<=:todate1 And groupId=:groupTypeDetails")
//	List<NotificationDetails> findBygroupId(Date fromDate1, Date todate1, GroupTypeDetails groupTypeDetails);
	
	@Query("from NotificationDetails where Sysdate between fromDate And toDate+1 And groupid=:groupId And statusFlag=0 order by notificationId desc")
	List<NotificationDetails> getnotificationshow(Integer groupId);
	
	@Query("from NotificationDetails order by notificationId desc")
	List<NotificationDetails> notificationdata();

}
