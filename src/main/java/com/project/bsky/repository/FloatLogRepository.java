/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.TxnclaimFloatActionLog;

/**
 * @author santanu.barad
 *
 */
@Repository
public interface FloatLogRepository extends JpaRepository<TxnclaimFloatActionLog, Long> {
	List<TxnclaimFloatActionLog> findAllByFloateIdAndStatusflagOrderByActionLogIdDesc(Long floatId,Integer statusFlag);
}
