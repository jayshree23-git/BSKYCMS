package com.project.bsky.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.MessagePassingEntity;

@Repository
public interface CommunicationRepository extends JpaRepository<MessagePassingEntity, Integer> {
	
	@Query("from MessagePassingEntity where bitDeletedFlag=0")
	List<MessagePassingEntity> findAllData();

	@Transactional
	@Modifying
	@Query(value="update m_dyn_message_configuration set SchedularStatus=2 where intMessageConfigId=:messageConfigId and intProcessId=:processId and bitDeletedFlag=0",nativeQuery = true)
	void updateDataForExecute(Integer messageConfigId, Integer processId);

	@Transactional
	@Modifying
	@Query(value="update m_dyn_message_configuration set SchedularStatus=1,stopExecutionDate=:date where intMessageConfigId=:messageConfigId and intProcessId=:processId and bitDeletedFlag=0",nativeQuery = true)
	void updateDataForStop(Integer messageConfigId, Integer processId, Date date);

	@Transactional
	@Modifying
	@Query(value="update m_dyn_message_configuration set intMessageConfigType=:messageConfigType,dtmStartDate=:startDate,dtmEndDate=:endDate,frequencyType=:frequencyType,frequencyDuration=:frequencyDuration,schedularUrl=:schedularUrl,SchedularStatus=1 where intMessageConfigId=:messageConfigId and intProcessId=:processId and bitDeletedFlag=0",nativeQuery = true)
	void updateDataForCreate(Integer messageConfigId, Integer processId, Integer messageConfigType, String startDate,
			String endDate, String frequencyType, String frequencyDuration, String schedularUrl);

	@Query(value="select intMessageConfigType,vchSmsTemplateId,vchMessageContent,intMailTemplate from m_dyn_message_configuration where intProcessId=:intProcessId and intEventType=1 and bitDeletedFlag=0",nativeQuery = true)
	List<Object[]> getDataForSMSPassing(int intProcessId);

//	List<MessagePassingDto> getByMessageId(String string);

}
