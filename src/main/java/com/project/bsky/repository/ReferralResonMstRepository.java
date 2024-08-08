package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.ReferralResonMst;

@Repository
public interface ReferralResonMstRepository extends JpaRepository<ReferralResonMst, Long> {

	@Query("select count(*) from ReferralResonMst where referaldesc=:referaldesc")
	Integer checkduplicate(String referaldesc);

	@Query("from ReferralResonMst order by referralid desc")
	List<ReferralResonMst> getalldata();

	ReferralResonMst findByreferaldesc(String referaldesc);

}
