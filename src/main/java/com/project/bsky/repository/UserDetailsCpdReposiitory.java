/**
 * 
 */
package com.project.bsky.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.bsky.model.UserDetails;
import com.project.bsky.model.UserDetailsCpd;

/**
 * @author rajendra.sahoo
 *
 */
public interface UserDetailsCpdReposiitory extends JpaRepository<UserDetailsCpd, Integer> {

	@Query("select count(*) from UserDetailsCpd where lower(userName)=:userName")
	Integer checkusername(String userName);

	@Query("select fullName from UserDetailsCpd where bskyUserId=:bskyUserId")
	String getFullName(int bskyUserId);

	@Query("SELECT u.isActive FROM UserDetailsCpd u WHERE u.bskyUserId=:bskyUserId")
	Integer getstatus(@Param("bskyUserId") int bskyUserId);

	@Query("from UserDetailsCpd where userName=:userName")
	UserDetailsCpd findname(String userName);

	@Query("select count(*) from UserDetailsCpd where doctorLicenseNo=:doctorLicenseNo and lower(userName)!=:userName")
	Integer checklicense(String doctorLicenseNo, String userName);

	@Query("from UserDetailsCpd where doctorLicenseNo=:doctorLicenseNo")
	UserDetailsCpd findname1(String doctorLicenseNo);

	@Query("from UserDetailsCpd order by bskyUserId desc")
	List<UserDetailsCpd> findAlldata();

	@Query("from UserDetailsCpd where userid.userId=:createdby")
	UserDetailsCpd findByuserid(Long createdby);

	@Query("from UserDetailsCpd where userid=:userdetails")
	UserDetailsCpd findByuserid(UserDetails userdetails);

	@Query("FROM UserDetailsCpd udfsc WHERE udfsc.userid.userId=:userId")
	UserDetailsCpd getuserDeatilsForSaveCpdByUserId(@Param(value = "userId") Long userId);

	@Query("from UserDetailsCpd where isActive=0")
	List<UserDetailsCpd> getCpdName();
	
	Optional<UserDetailsCpd> findByUserName(String userName);
	
	@Transactional
	@Modifying
	@Query("UPDATE UserDetailsCpd SET isActive=1,lastUpdatedBy=:updatedBy,lastUpdatedDate=sysdate where userid.userId=:userId")
	void inactivate(Long userId, Integer updatedBy);
	
	@Transactional
	@Modifying
	@Query("UPDATE UserDetailsCpd SET isActive=0,lastUpdatedBy=:updatedBy,lastUpdatedDate=sysdate where userid.userId=:userId")
	void activate(Long userId, Integer updatedBy);

	@Query("from UserDetailsCpd where dateofjoining BETWEEN :fromdate AND :toDate")
	List<UserDetailsCpd> findAllCpd(Date fromdate, Date toDate);

	@Query("from UserDetailsCpd where userid.userId=:l")
	UserDetailsCpd findByuserdetails(Long l);

	@Query("from UserDetailsCpd where isActive=:status and dateofjoining BETWEEN :fromdate AND :toDate")
	List<UserDetailsCpd> findAllCpdbystatus(Date fromdate, Date toDate, Integer status);

}
