/**
 * 
 */
package com.project.bsky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.bsky.model.RationCardUser;

/**
 * @author rajendra.sahoo
 *
 */
public interface RationCardUserRepository extends JpaRepository<RationCardUser, Long> {

	@Query("from RationCardUser where statusflag=0")
	List<RationCardUser> getall();

}
