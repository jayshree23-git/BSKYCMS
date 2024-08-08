package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.MDynamicSmsMail;
@Repository
public interface MDynamicSmsMailRepository extends JpaRepository<MDynamicSmsMail, Integer> {
	@Transactional
	@Modifying
	@Query(value="update m_dynamic_sms_mail set tinSendStatus=1 where intDynamicSendId=:intDynamicSendId",nativeQuery=true)
	void update(Integer intDynamicSendId);

	@Query("From MDynamicSmsMail where bitDeletedFlag=0 and tinSendStatus=0 and tinTypeId=1")
	List<MDynamicSmsMail> getMailDetails();

	@Query("From MDynamicSmsMail where bitDeletedFlag=0 and tinSendStatus=0 and tinTypeId=2")
	List<MDynamicSmsMail> getMailDetailsForSms();

}
