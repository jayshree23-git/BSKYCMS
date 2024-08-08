/**
 * 
 */
package com.project.bsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bsky.model.State;

/**
 * @author satyabrata.s
 *
 */
@Repository
public interface StateRepository extends JpaRepository<State, Integer>{

	@Query("FROM State where stateCode=:stateCode")
	State findStateByCode(String stateCode);

	@Query("FROM State WHERE stateCode=:stateId")
	State findBycode(String stateId);

	@Query("SELECT stateName FROM State WHERE stateCode=:stateCode")
	String getStateNameByStateCode(@Param("stateCode") String stateCode);



	

	


}
