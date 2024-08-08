package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.project.bsky.model.OTPAuth;

@Repository
public interface OTPAuthRepository extends JpaRepository<OTPAuth, Long> {

	OTPAuth findByUserNameIgnoreCaseAndVerifyStatus(String userName, Integer verifyStatus);

	OTPAuth findTop1ByUserNameIgnoreCaseAndVerifyStatusAndUpdatedOnIsNullOrderByCreatedOnDesc(String userName, Integer verifyStatus);
	
	//@Query("SELECT s FROM OTPAuth s ORDER BY s.userName DESC LIMIT 1", nativeQuery=true)
	//@Query(nativeQuery = true, value = "SELECT OTP FROM USER_OTP_AUTH  where USERNAME=:userName and  OTP_VERIFY = 0 ORDER BY USERNAME Desc limit 1")
	
	@Query(value = "select otp from user_otp_auth where username=userName and otp_verify=0 order by username desc limit 1", nativeQuery = true)
	OTPAuth getLatestOTPByUserName(String userName);
	
	@Query(value = "select otp from (select * from USER_OTP_AUTH where lower(username)=lower(userName) order by CREATED_ON desc) where rownum=1", nativeQuery = true)
	String getOTPLatest(String userName);

	@Query(value = "select * from (select * from USER_OTP_AUTH where lower(username)=lower(userName) order by CREATED_ON desc) where rownum=1", nativeQuery = true)
	OTPAuth getOTPAuthLatest(String userName);
	
	@Query(value = "select max(AUTH_ID) from USER_OTP_AUTH", nativeQuery = true)
	Integer getMaxAuthId();
	
}
