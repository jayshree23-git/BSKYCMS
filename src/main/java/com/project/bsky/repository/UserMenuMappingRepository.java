package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.PrimaryLink;
import com.project.bsky.model.UserMenuMapping;

/**
 * @author ronauk
 *
 */

@Repository
public interface UserMenuMappingRepository extends JpaRepository<UserMenuMapping, Integer>{

	List<UserMenuMapping> findByUserIdAndBitStatus(Integer userId,Integer bitStatus);
	
	@Transactional
	@Modifying
	@Query("update UserMenuMapping set bitStatus=1, updatedOn=sysdate, updatedBy=:createdby where userId=:userId")
	void setAllInactive(@Param("userId") Integer userId, @Param("createdby") Integer createdby);

	@Transactional
	@Modifying
	@Query("update UserMenuMapping set bitStatus=0, updatedOn=sysdate, updatedBy=:createdby where userId=:userId and primaryLink.primaryLinkId=:primaryLinkId")
	void setStatusActive(@Param("userId") Integer userId, @Param("createdby") Integer createdby, @Param("primaryLinkId") Long primaryLinkId);
	
	@Query("select count(*) from UserMenuMapping where userId=:userId and primaryLink.primaryLinkId=:primaryLinkId")
	Integer checkUserPrimaryLinkMapping(@Param("userId") Integer userId, @Param("primaryLinkId") Long primaryLinkId);

	@Query("from UserMenuMapping where userId=:userId and primaryLink.primaryLinkId=:primaryLinkId")
	UserMenuMapping PrimaryLinkMappingforLog(@Param("userId") Integer userId, @Param("primaryLinkId") Long primaryLinkId);

	@Query("from UserMenuMapping where userId=:userId and bitStatus=0")
	List<UserMenuMapping> getuserspecifcprimarylink(@Param("userId") Integer userId);
	
	@Query("from UserMenuMapping where userId=:userId")
	List<UserMenuMapping> getuserspecifcprimarylinkforlog(@Param("userId") Integer userId);
	
	@Query("select count(*) from UserMenuMapping where PRIMARY_LINK_ID=:id And bitStatus=0")
	Integer countassignpmlink(Long id);

	@Query("select count(*) from UserMenuMapping where global_Link_Id=:globalLinkId And bitStatus=0")
	Integer countassigngllink(Integer globalLinkId);
	
	@Query("select bitStatus from UserMenuMapping where userId=:userId and primaryLink.primaryLinkId=:primaryLinkId")
	Integer getUserStatus(@Param("userId") Integer userId, @Param("primaryLinkId") Long primaryLinkId);
	
	@Query("FROM UserMenuMapping where userId=:userId and bitStatus=0")
	List<UserMenuMapping> getPrimaryLinksFromUserId(@Param("userId") Integer userId);
	
	@Query("select count(*) from UserMenuMapping where userId=:userId and bitStatus=0")
	Integer checkPrimaryLinksFromUserId(@Param("userId") Integer userId);
	
	@Query("select max(userMappingId) from UserMenuMapping")
	Integer getMaxMappingId();
	
	@Query("select count(*) from UserMenuMapping where userId=:userId and primaryLink.primaryLinkId=:primaryLinkId and bitStatus=0")
	Integer checkPrimaryLinkAccess(@Param("userId") Integer userId, @Param("primaryLinkId") Long primaryLinkId);
	
	@Query("From UserMenuMapping U where U.userId=:userId and U.global_Link_Id=:globalId and U.bitStatus=0 and U.primaryLink.bitStatus=0 and U.primaryLink.functionMaster.bitStatus=0 order by U.primaryLink.primaryLinkName")
	List<UserMenuMapping> findMISReportsLink(@Param("userId") Integer userId,@Param("globalId") Integer globalId);

}
