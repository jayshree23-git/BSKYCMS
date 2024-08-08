/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.QueryLoginMater;

/**
 * @author santanu.barad
 *
 */
@Repository
public interface QueryLoginRepository extends JpaRepository<QueryLoginMater, Integer> {
	QueryLoginMater findByUserName(String username);
}
