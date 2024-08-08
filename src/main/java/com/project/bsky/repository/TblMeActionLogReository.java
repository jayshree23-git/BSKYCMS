/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.TblMeActionLog;

/**
 * @author rajendra.sahoo
 *
 */
public interface TblMeActionLogReository extends JpaRepository<TblMeActionLog, Long>{

	List<Object> findBytransactionid(Long txnid);

	@Query(value = "select u.full_name,ACTIONON,REMARKS \r\n"
			+ "from \r\n"
			+ "tbl_me_action_log tl\r\n"
			+ "inner join userdetails u on u.userid=tl.ACTIONBY\r\n"
			+ "where tl.TRANSACTIONDETAILSID=?1 \r\n"
			+ "order by tl.ME_REMARK_LOGID desc",nativeQuery= true)
	List<Object[]> findBytxnid(Long txnid);

}
