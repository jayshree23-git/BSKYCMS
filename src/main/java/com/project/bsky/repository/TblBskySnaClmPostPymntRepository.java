package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.TblBskySnaClaimPostPayment;

/**
 * @author ronauk
 *
 */
@Repository
public interface TblBskySnaClmPostPymntRepository extends JpaRepository<TblBskySnaClaimPostPayment, Integer> {
	
	@Query("FROM TblBskySnaClaimPostPayment where transId=:transId")
	TblBskySnaClaimPostPayment findBytransId(Long transId);
	
	@Query("FROM TblBskySnaClaimPostPayment where transId in :transList")
	List<TblBskySnaClaimPostPayment> findByTransIdIn(List<Long> transList);

}
