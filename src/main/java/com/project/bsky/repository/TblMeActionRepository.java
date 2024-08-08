/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.TblMeAction;

/**
 * @author rajendra.sahoo
 *
 */
public interface TblMeActionRepository extends JpaRepository<TblMeAction, Long>{

	@Query("Select Count(*) from TblMeAction where transactionid=:txnId and status=0 and meTriggerId=:flag")
	Integer checkduplicatetxnid(Long txnId, Integer flag);

	TblMeAction findBytransactionid(Long txnid);

	@Query(value = "select us.sno_user_id,u.full_name from TXNTRANSACTIONDETAILS T\r\n"
			+ "Left join user_sna_mapping us on us.HOSPITAL_CODE=t.HOSPITALCODE and us.status_flag=0\r\n"
			+ "inner join userdetails u on u.userid=us.sno_user_id\r\n"
			+ "where T.TRANSACTIONDETAILSID=?1",nativeQuery=true)
	List<Object[]> getsnoidbytxnid(Long txnid);

	@Query("from TblMeAction where transactionid=:txnId and status=0 and meTriggerId=:flag")
	TblMeAction getduplicatetxnid(Long txnId, Integer flag);

}
