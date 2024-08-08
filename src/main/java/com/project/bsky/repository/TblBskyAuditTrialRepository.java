/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.TblBskyAuditTrial;

/**
 * @author rajendra.sahoo
 *
 */
public interface TblBskyAuditTrialRepository extends JpaRepository<TblBskyAuditTrial, Long> {

	@Query("from TblBskyAuditTrial where claimno=:urn order by auditId desc")
	List<TblBskyAuditTrial> getbyclaimno(String urn);

	@Query("from TblBskyAuditTrial where urn=:urn order by auditId desc")
	List<TblBskyAuditTrial> getbyurn(String urn);

}
