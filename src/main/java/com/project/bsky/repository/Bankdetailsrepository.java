/**
 * 
 */
package com.project.bsky.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.BankDetails;
import com.project.bsky.model.UserDetails;

/**
 * @author rajendra.sahoo
 *
 */
public interface Bankdetailsrepository extends JpaRepository<BankDetails, Integer> {
	
	@Query("From BankDetails where userid=:userdetails")
	BankDetails getByid(UserDetails userdetails);
	
	@Query("From BankDetails where userid.userId=:userId")
	BankDetails getByUserId(Long userId);
	
	@Transactional
	@Modifying
	@Query("UPDATE BankDetails SET IsActive=1,UpdatedBy=:updatedBy,UpdatedON=sysdate where userid.userId=:userId")
	void inactivate(Long userId, Integer updatedBy);
	
	@Transactional
	@Modifying
	@Query("UPDATE BankDetails SET IsActive=0,UpdatedBy=:updatedBy,UpdatedON=sysdate where userid.userId=:userId")
	void activate(Long userId, Integer updatedBy);

}
