/**
 * 
 */
package com.project.bsky.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.Userswasthyamitradetails;

/**
 * @author rajendra.sahoo
 *
 */
public interface UserswasthyamitradetailsRepository extends JpaRepository<Userswasthyamitradetails, Long> {

	@Transactional
	@Modifying
	@Query("update Userswasthyamitradetails set statusflag=1, updatedBy=:updateBy, updatedOn=sysdate where userid=:userId and statusflag=0")
	void inActivatesmdetails(Integer userId, Integer updateBy);
}
