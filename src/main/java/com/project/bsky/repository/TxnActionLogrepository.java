/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.Subgroup;
import com.project.bsky.model.TxnClaimActionLog;

/**
 * @author rajendra.sahoo
 *
 */
@Repository
public interface TxnActionLogrepository extends JpaRepository<TxnClaimActionLog, Long> {

	@Query(" from TxnClaimActionLog where claimid=:claim And actiontype=:type And statusflag=0" )
	TxnClaimActionLog getviewremark(Long claim, Integer type);

}
