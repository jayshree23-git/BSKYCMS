package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsProfile;

/**
 * @author ronauk
 *
 */

@Repository
public interface UserDetailsProfileRepository extends JpaRepository<UserDetailsProfile, Integer> {

	@Query("SELECT u.bskyUserId FROM UserDetailsProfile u WHERE u.userName=:userName")
	Integer getIdByUserName(@Param("userName") String userName);

	@Query("SELECT u.fullName FROM UserDetailsProfile u WHERE u.userId.userId=:userId")
	String getFullName(@Param("userId") long userId);

	@Query("SELECT u.status FROM UserDetailsProfile u WHERE u.userId.userId=:userId")
	Integer getstatus(@Param("userId") long userId);

	@Query("select count(*) from UserDetailsProfile where lower(userName)=:userName")
	Integer checkusername(String userName);

	@Transactional
	@Modifying
	@Query("UPDATE UserDetailsProfile SET status=1,updatedBy=:updatedBy,updatedOn=sysdate where userId.userId=:userId")
	void inactivate(Long userId, Long updatedBy);

	@Transactional
	@Modifying
	@Query("UPDATE UserDetailsProfile SET status=0,updatedBy=:updatedBy,updatedOn=sysdate where userId.userId=:userId")
	void activate(Long userId, Long updatedBy);

	@Query("FROM UserDetailsProfile where userId.userId=:userId")
	UserDetailsProfile getUserProfileFromUserId(Long userId);

	@Query("FROM UserDetailsProfile order by bskyUserId desc")
	List<UserDetailsProfile> findAllActiveSNOUser();

	@Query("FROM UserDetailsProfile where groupId.typeId = decode(:groupId, NULL, groupId.typeId, :groupId) "
			+ "and districtCode.statecode.stateCode = decode(:stateId, NULL, districtCode.statecode.stateCode, :stateId) "
			+ "and districtCode.districtcode = decode(:districtId, NULL, districtCode.districtcode, :districtId) "
			+ "order by fullName")
	List<UserDetailsProfile> findUsers(String groupId, String stateId, String districtId);

	@Query("FROM UserDetailsProfile bskyUserId WHERE districtCode.districtcode=:districtId")
	List<UserDetailsProfile> findAllActiveSNOUserForFilterByDistrict(String districtId);

	@Query(" From UserDetailsProfile U where U.userId.GroupId=11 and status=0")
	List<UserDetailsProfile> findAllHospitalAuthority();

	@Query(" From UserDetailsProfile where userId=:userid")
	UserDetailsProfile getbyuserid(UserDetails userid);

	@Query(" From UserDetailsProfile where userId.userId=:userId")
	UserDetailsProfile findByuserId(Long userId);

	@Transactional
	@Modifying
	@Query("UPDATE UserDetailsProfile SET status=:statusflag,updatedBy=:updatedBy,updatedOn=sysdate where userId.userId=:userId")
	void inactivateactive(Long userId, Long updatedBy, Integer statusflag);

	@Query(value = "SELECT bskyuserid,uesrname,user_id,(full_name||' ('||uesrname||')') as full_name FROM USER_DETAILS_PROFILE \r\n"
			+ "WHERE STATUS_FLAG=0 AND GROUP_TYPE_ID=6 \r\n"
			+ "AND STATE_CODE=(select STATE_CODE from user_cdmo_mapping where CDMO_USER_ID=?1)\r\n"
			+ "AND DISTRICTCODE=(select DISTRICT_CODE from user_cdmo_mapping where CDMO_USER_ID=?1)\r\n"
			+ "ORDER BY full_name", nativeQuery = true)
	List<Object[]> FindDCList(Long userId);

	@Query(value = "SELECT bskyuserid,uesrname,user_id,(full_name||' ('||uesrname||')') as full_name FROM USER_DETAILS_PROFILE \r\n"
			+ "WHERE STATUS_FLAG=0 AND GROUP_TYPE_ID=6 \r\n" + "AND STATE_CODE=?1\r\n" + "AND DISTRICTCODE=?2\r\n"
			+ "ORDER BY full_name", nativeQuery = true)
	List<Object[]> FindDCListByStateAndDist(String stateId, String distId);

}
