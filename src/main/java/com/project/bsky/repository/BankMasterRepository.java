/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.BankMaster;

/**
 * @author priyanka.singh
 *
 */
@Repository
public interface BankMasterRepository extends JpaRepository<BankMaster, Integer> {
	
	@Query("SELECT count(*) FROM BankMaster g WHERE g.bankName=:bankName")
	Integer countRowForCheckDuplicateType(String bankName);

	@Query("FROM BankMaster order by  bankName desc")
	List<BankMaster> findDetails();

	
	
}
