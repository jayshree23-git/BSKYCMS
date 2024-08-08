package com.project.bsky.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.UserDetails;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
	
	@Query("FROM UserDetails where lower(UserName)=lower(:username) and status=0")
	UserDetails findByUserName(String username);

	@Query("select count(*) from UserDetails where lower(UserName)=:userName")
	Integer checkusername(String userName);
	
	@Query("select count(*) from UserDetails where upper(UserName)=:userName")
	Integer checkusernameforhosp(String userName);
	
	@Transactional
	@Modifying
	@Query("UPDATE UserDetails SET status=1 where userId=:userId")
	void inactivate(Long userId);
	
	@Transactional
	@Modifying
	@Query("UPDATE UserDetails SET status=:statusflag where userId=:userId")
	void inactivateactive(Long userId,Integer statusflag);
	
	@Transactional
	@Modifying
	@Query("UPDATE UserDetails SET status=0 where userId=:userId")
	void activate(Long userId);
	
	@Query("SELECT u.userId FROM UserDetails u WHERE u.UserName=:userName")
	Integer getUserIdbyUserName(@Param("userName")String userName);

	@Query("FROM UserDetails u WHERE u.status=0 order by userId desc")
	List<UserDetails> findAllActive();

	@Query("SELECT u.fullname FROM UserDetails u WHERE u.userId=:userId")
	String getUserName(@Param("userId")long userId);
	
	@Query("FROM UserDetails u where u.UserName=:userName order by userId desc")
	List<UserDetails> getByUserName(@Param("userName")String userName);

	@Query("FROM UserDetails WHERE GroupId.typeId=:groupId order by userId desc")
	List<UserDetails> findAllUserForFilterByGroup(Integer groupId);
	
	@Query("FROM UserDetails WHERE status=0 and GroupId.typeId=:groupId order by userId")
	List<UserDetails> findAllActiveUserForFilterByGroup(Integer groupId);
	
	@Query("select userId FROM UserDetails WHERE status=0 and GroupId.typeId=:groupId order by userId")
	List<Long> getAllActiveUserIdForFilterByGroup(Integer groupId);

	@Query(" FROM UserDetails WHERE status=0 and GroupId=14 ")
	List<UserDetails> finddetailsSwasthyaMitra();

	@Query(" FROM UserDetails WHERE status = 0 AND attemptedStatus >= 3 ORDER BY userId DESC")
	List<UserDetails> getLockedUserList();

	@Query(" FROM UserDetails WHERE userId = :userId")
	UserDetails findByUserId(@Param("userId") long userId);

	@Query("FROM UserDetails WHERE status=0 and GroupId.typeId=:typeid order by fullname asc")
	List<UserDetails> getintuserdetails(Integer typeid);
	
	@Query(" FROM UserDetails WHERE status=0 and GroupId=15 order by fullname asc")
	List<UserDetails> findDetails();
	
	@Query("SELECT u.profilePhoto FROM UserDetails u WHERE u.userId=:userId")
	String getProfilePhoto(@Param("userId") Long userId);

	@Query(value = "FROM UserDetails U WHERE U.GroupId.typeId = 3 AND U.status = 0")
	List<UserDetails> getCPDUserList();
	
}