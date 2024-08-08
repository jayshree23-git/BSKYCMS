/**
 * 
 */
package com.project.bsky.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.TxnclamFloateDetails;
import com.project.bsky.model.Txnclaimapplication;

/**
 * @author rajendra.sahoo
 *
 */
@Repository

public interface TxnclaimFloatdetailsrepository extends JpaRepository<TxnclamFloateDetails, Long> {

//	@Query("from TxmclamFloateDetails where createon >=:fromDate1 And createon<=:todate1") isVerified ='Y' And
	List<TxnclamFloateDetails> findBycreateonBetween(Date fromDate1, Date todate1);

	@Query(" FROM TxnclamFloateDetails T where T.isVerified ='Y' And T.statusflag=0")
	List<TxnclamFloateDetails> AllNotVerifiedFloatList();

	@Query(" FROM TxnclamFloateDetails T where T.isVerified =1 And T.statusflag=0")
	List<TxnclamFloateDetails> getVerifiedFloatList();

	@Query(" FROM TxnclamFloateDetails T where T.pendingAt in (2,6) And T.isVerified =0 And T.statusflag=0 And T.assignedauthority=:assignedAuth")
	List<TxnclamFloateDetails> getAssignedFO(Integer assignedAuth);

	List<TxnclamFloateDetails> findByfloateno(String floateno);

}
