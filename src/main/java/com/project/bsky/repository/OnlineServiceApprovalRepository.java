package com.project.bsky.repository;

import javax.persistence.Column;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.OnlineServiceApproval;


@Repository
public interface OnlineServiceApprovalRepository extends JpaRepository<OnlineServiceApproval, Integer>{

	OnlineServiceApproval findByIntProcessIdAndIntOnlineServiceIdAndIntStageNo(Integer processId, Integer serviceId,
			Integer stageNo);

	@Query(value="select * from T_ONLINE_SERVICE_APPROVAL where intProcessId = :intProcessId and intOnlineServiceId = :intOnlineServiceId "
			+ "and intStageNo = :intStageNo and bitDeletedFlag = :bitDeletedFlag", nativeQuery = true)
//	@Query(value="From OnlineServiceApproval where intProcessId = :intProcessId and intOnlineServiceId = :intOnlineServiceId "
//			+ "and intStageNo = :intStageNo and bitDeletedFlag = :bitDeletedFlag")
	OnlineServiceApproval findByIntProcessIdAndIntOnlineServiceIdAndIntStageNoAndBitDeletedFlag(Integer intProcessId,
			Integer intOnlineServiceId, Integer intStageNo, int bitDeletedFlag);

	OnlineServiceApproval findByIntProcessIdAndIntOnlineServiceIdAndBitDeletedFlag(Integer valueOf, Integer valueOf2,
			int i);

}
