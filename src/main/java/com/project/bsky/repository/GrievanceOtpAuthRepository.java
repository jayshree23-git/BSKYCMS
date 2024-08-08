/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.entity.GrievanceOtpAuth;


/**
 * @author arabinda.guin
 *
 */
@Repository
public interface GrievanceOtpAuthRepository extends JpaRepository<GrievanceOtpAuth,Long> {

	@Query(value = "select * from (select * from tbl_grievance_otp_auth where MOBILE_NO=?1 order by CREATED_ON desc) where rownum=1", nativeQuery = true)
	GrievanceOtpAuth getOTPAuthLatest(Long mobileNo);

}
